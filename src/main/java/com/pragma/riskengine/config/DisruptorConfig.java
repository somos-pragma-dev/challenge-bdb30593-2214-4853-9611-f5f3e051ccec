package com.pragma.riskengine.config;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.pragma.riskengine.disruptor.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.function.Supplier;

public class DisruptorConfig {
    private static final Logger logger = LoggerFactory.getLogger(DisruptorConfig.class);
    private static final int DEFAULT_BUFFER_SIZE = 8192;
    private static final int MIN_BUFFER_SIZE = 1024;
    private static final int MAX_BUFFER_SIZE = 65536;

    private final int bufferSize;
    private final WaitStrategy waitStrategy;
    private final boolean multiProducer;
    private final ExecutorService executorService;

    public DisruptorConfig() {
        this(DEFAULT_BUFFER_SIZE, WaitStrategyType.BLOCKING);
    }

    public DisruptorConfig(int bufferSize, WaitStrategyType strategyType) {
        this.bufferSize = normalizeBufferSize(bufferSize);
        this.waitStrategy = createWaitStrategy(strategyType);
        this.multiProducer = true;
        this.executorService = createExecutorService();
        logger.info("DisruptorConfig initialized: bufferSize={}, strategy={}, multiProducer={}",
                bufferSize, strategyType, multiProducer);
    }

    public Disruptor<OrderEvent> createOrderDisruptor(EventHandler<OrderEvent> handler) {
        Supplier<EventFactory<OrderEvent>> factory = OrderEvent::new;

        Disruptor<OrderEvent> disruptor = new Disruptor<>(
                factory.get(),
                bufferSize,
                executorService,
                multiProducer ? ProducerType.MULTI : ProducerType.SINGLE,
                waitStrategy
        );

        disruptor.handleEventsWith(handler);
        disruptor.setDefaultExceptionHandler(new LoggingExceptionHandler());

        logger.info("OrderDisruptor created with buffer size: {}", bufferSize);
        return disruptor;
    }

    public Disruptor<OrderEvent> createOrderDisruptorWithBatchHandler(EventHandler<OrderEvent>[] handlers) {
        Supplier<EventFactory<OrderEvent>> factory = OrderEvent::new;

        Disruptor<OrderEvent> disruptor = new Disruptor<>(
                factory.get(),
                bufferSize,
                executorService,
                multiProducer ? ProducerType.MULTI : ProducerType.SINGLE,
                waitStrategy
        );

        disruptor.handleEventsWith(handlers);
        disruptor.setDefaultExceptionHandler(new LoggingExceptionHandler());

        logger.info("OrderDisruptor created with {} handlers, buffer size: {}",
                handlers.length, bufferSize);
        return disruptor;
    }

    private int normalizeBufferSize(int size) {
        if (size < MIN_BUFFER_SIZE) {
            logger.warn("Buffer size {} too small, using minimum {}", size, MIN_BUFFER_SIZE);
            return MIN_BUFFER_SIZE;
        }
        if (size > MAX_BUFFER_SIZE) {
            logger.warn("Buffer size {} too large, using maximum {}", size, MAX_BUFFER_SIZE);
            return MAX_BUFFER_SIZE;
        }
        if ((size & (size - 1)) != 0) {
            int normalized = Integer.highestOneBit(size) << 1;
            logger.warn("Buffer size {} not power of 2, using {}", size, normalized);
            return normalized;
        }
        return size;
    }

    private WaitStrategy createWaitStrategy(WaitStrategyType type) {
        return switch (type) {
            case BLOCKING -> new BlockingWaitStrategy();
            case YIELDING -> new YieldingWaitStrategy();
            case SLEEPING -> new SleepingWaitStrategy();
            case BUSY_SPIN -> new BusySpinWaitStrategy();
            case PHASED_BACKOFF -> new PhasedBackoffWaitStrategy(1, 1, TimeUnit.MILLISECONDS);
        };
    }

    private ExecutorService createExecutorService() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public WaitStrategy getWaitStrategy() {
        return waitStrategy;
    }

    public boolean isMultiProducer() {
        return multiProducer;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void shutdown() {
        logger.info("Shutting down Disruptor executor service");
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public enum WaitStrategyType {
        BLOCKING,
        YIELDING,
        SLEEPING,
        BUSY_SPIN,
        PHASED_BACKOFF
    }

    private static class LoggingExceptionHandler implements ExceptionHandler<OrderEvent> {
        private static final Logger exceptionLogger = LoggerFactory.getLogger("DisruptorExceptionHandler");

        @Override
        public void handleEventException(Throwable ex, long sequence, OrderEvent event) {
            exceptionLogger.error("Exception processing event at sequence {}: {}",
                    sequence, ex.getMessage(), ex);
        }

        @Override
        public void handleOnStartException(Throwable ex) {
            exceptionLogger.error("Exception starting disruptor: {}", ex.getMessage(), ex);
        }

        @Override
        public void handleOnShutdownException(Throwable ex) {
            exceptionLogger.error("Exception shutting down disruptor: {}", ex.getMessage(), ex);
        }
    }
}