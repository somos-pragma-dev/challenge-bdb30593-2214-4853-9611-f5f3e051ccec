package com.pragma.riskengine.adapter.exchange;

import com.pragma.riskengine.config.ResilienceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class ExchangeAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ExchangeAdapter.class);
    private static final int DEFAULT_TIMEOUT_MS = 5000;
    private static final int MAX_PENDING_ORDERS = 10000;

    private final ExecutorService orderExecutor;
    private final ConcurrentHashMap<String, PendingOrder> pendingOrders;
    private final BlockingQueue<OrderResponse> responseQueue;
    private final AtomicBoolean isConnected;
    private final AtomicLong orderSequence;
    private final CircuitBreakerAdapter circuitBreaker;
    private final ResilienceConfig resilienceConfig;

    public ExchangeAdapter(ResilienceConfig resilienceConfig) {
        this.resilienceConfig = resilienceConfig;
        this.orderExecutor = Executors.newVirtualThreadPerTaskExecutor();
        this.pendingOrders = new ConcurrentHashMap<>();
        this.responseQueue = new LinkedBlockingQueue<>(MAX_PENDING_ORDERS);
        this.isConnected = new AtomicBoolean(false);
        this.orderSequence = new AtomicLong(0);
        this.circuitBreaker = new CircuitBreakerAdapter(resilienceConfig);
    }

    public String sendOrder(String orderId, String traderId, String strategyId,
            String instrumentId, String side, BigDecimal quantity, BigDecimal price) {
        if (!isConnected.get()) {
            throw new ExchangeNotConnectedException("Exchange connection is not established");
        }

        String sequenceId = generateSequenceId();
        long sequence = orderSequence.incrementAndGet();

        logger.info("Sending order to exchange: orderId={}, sequence={}, instrument={}, side={}, qty={}, price={}",
                orderId, sequence, instrumentId, side, quantity, price);

        PendingOrder pending = new PendingOrder(orderId, sequenceId, traderId, strategyId,
                instrumentId, side, quantity, price, Instant.now());
        pendingOrders.put(sequenceId, pending);

        CompletableFuture<OrderResponse> future = CompletableFuture.supplyAsync(() -> {
            return executeOrderWithResilience(pending);
        }, orderExecutor);

        future.thenAccept(response -> {
            pendingOrders.remove(sequenceId);
            responseQueue.offer(response);
            logger.info("Order response received: orderId={}, status={}, sequence={}",
                    orderId, response.status(), sequence);
        });

        return sequenceId;
    }

    private OrderResponse executeOrderWithResilience(PendingOrder order) {
        if (circuitBreaker.isOpen()) {
            logger.warn("Circuit breaker is OPEN, rejecting order: {}", order.orderId());
            return new OrderResponse(order.orderId(), order.sequenceId(),
                    OrderStatus.REJECTED, "Circuit breaker open", Instant.now());
        }

        try {
            return resilienceConfig.executeWithRetry(() -> {
                return submitToExchange(order);
            });
        } catch (Exception e) {
            logger.error("Order execution failed after retries: orderId={}, error={}",
                    order.orderId(), e.getMessage());
            circuitBreaker.recordFailure();
            return new OrderResponse(order.orderId(), order.sequenceId(),
                    OrderStatus.REJECTED, e.getMessage(), Instant.now());
        }
    }

    private OrderResponse submitToExchange(PendingOrder order) {
        try {
            Thread.sleep(10);
            circuitBreaker.recordSuccess();
            return new OrderResponse(order.orderId(), order.sequenceId(),
                    OrderStatus.ACCEPTED, "Order accepted by exchange", Instant.now());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new OrderResponse(order.orderId(), order.sequenceId(),
                    OrderStatus.ERROR, "Order submission interrupted", Instant.now());
        }
    }

    public OrderResponse waitForResponse(long timeoutMs) throws InterruptedException {
        OrderResponse response = responseQueue.poll(timeoutMs, TimeUnit.MILLISECONDS);
        if (response == null) {
            logger.warn("Timeout waiting for order response after {}ms", timeoutMs);
        }
        return response;
    }

    public boolean checkConnectivity() {
        try {
            return isConnected.get();
        } catch (Exception e) {
            logger.error("Connectivity check failed: {}", e.getMessage());
            return false;
        }
    }

    public void connect() {
        logger.info("Connecting to exchange...");
        isConnected.set(true);
        logger.info("Connected to exchange successfully");
    }

    public void disconnect() {
        logger.info("Disconnecting from exchange...");
        isConnected.set(false);
        pendingOrders.clear();
        logger.info("Disconnected from exchange");
    }

    private String generateSequenceId() {
        return UUID.randomUUID().toString();
    }

    public int getPendingOrdersCount() {
        return pendingOrders.size();
    }

    public void shutdown() {
        disconnect();
        orderExecutor.shutdown();
        try {
            if (!orderExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                orderExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            orderExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public record PendingOrder(String orderId, String sequenceId, String traderId,
            String strategyId, String instrumentId, String side,
            BigDecimal quantity, BigDecimal price, Instant submittedAt) {
    }

    public record OrderResponse(String orderId, String sequenceId, OrderStatus status,
            String message, Instant timestamp) {
    }

    public enum OrderStatus {
        ACCEPTED, REJECTED, FILLED, PARTIALLY_FILLED, CANCELLED, ERROR
    }

    public static class ExchangeNotConnectedException extends RuntimeException {
        public ExchangeNotConnectedException(String message) {
            super(message);
        }
    }

    private static class CircuitBreakerAdapter {
        private final AtomicBoolean isOpen = new AtomicBoolean(false);
        private final AtomicLong failureCount = new AtomicLong(0);
        private final AtomicLong successCount = new AtomicLong(0);
        private final ResilienceConfig config;
        private volatile Instant lastFailureTime;

        public CircuitBreakerAdapter(ResilienceConfig config) {
            this.config = config;
        }

        public boolean isOpen() {
            if (failureCount.get() >= config.getFailureThreshold()) {
                if (lastFailureTime != null &&
                        Duration.between(lastFailureTime, Instant.now()).toMillis() < config.getResetTimeoutMs()) {
                    return true;
                } else {
                    failureCount.set(0);
                    successCount.set(0);
                }
            }
            return false;
        }

        public void recordSuccess() {
            successCount.incrementAndGet();
            failureCount.set(0);
        }

        public void recordFailure() {
            failureCount.incrementAndGet();
            lastFailureTime = Instant.now();
        }
    }
}