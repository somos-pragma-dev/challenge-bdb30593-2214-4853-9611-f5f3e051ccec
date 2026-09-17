package com.pragma.riskengine.adapter.marketdata;

import com.pragma.riskengine.audit.AuditLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class MarketDataFeedAdapter {
    private static final Logger logger = LoggerFactory.getLogger(MarketDataFeedAdapter.class);
    private static final int MAX_ORDERBOOK_DEPTH = 10;
    private static final Duration RECONNECT_DELAY = Duration.ofSeconds(5);
    private static final int MAX_RECONNECT_ATTEMPTS = 3;
    private static final BigDecimal DEFAULT_SPREAD = new BigDecimal("0.01");

    private final AuditLogger auditLogger;
    private final ScheduledExecutorService scheduler;
    private final Map<String, OrderBook> orderBooksByInstrument;
    private final Map<String, List<Trade>> recentTradesByInstrument;
    private final Map<String, AtomicLong> sequenceNumbersByInstrument;
    private volatile boolean connected;
    private volatile Instant lastHeartbeat;
    private volatile FeedConnectionStatus connectionStatus;
    private final Object feedLock = new Object();
    private int reconnectAttempts;
    private MarketDataListener listener;

    public MarketDataFeedAdapter(AuditLogger auditLogger) {
        this.auditLogger = auditLogger;
        this.scheduler = Executors.newScheduledThreadPool(2, r -> {
            Thread t = new Thread(r, "marketdata-feed");
            t.setDaemon(true);
            return t;
        });
        this.orderBooksByInstrument = new ConcurrentHashMap<>();
        this.recentTradesByInstrument = new ConcurrentHashMap<>();
        this.sequenceNumbersByInstrument = new ConcurrentHashMap<>();
        this.connected = false;
        this.connectionStatus = FeedConnectionStatus.DISCONNECTED;
        initializeFeedConnection();
        startHeartbeatMonitor();
    }

    private void initializeFeedConnection() {
        scheduler.submit(() -> {
            logger.info("Inicializando conexión al feed de market data");
            simulateConnection();
        });
    }

    private void simulateConnection() {
        synchronized (feedLock) {
            try {
                Thread.sleep(100);
                connected = true;
                connectionStatus = FeedConnectionStatus.CONNECTED;
                logger.info("Conexión al feed de market data establecida");
                auditLogger.logMarketDataEvent("FEED_CONNECTED", 
                        Map.of("timestamp", Instant.now().toString()));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                connectionStatus = FeedConnectionStatus.ERROR;
            }
        }
    }

    private void startHeartbeatMonitor() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                checkFeedHealth();
            } catch (Exception e) {
                logger.error("Error en verificación de salud del feed", e);
            }
        }, 10, 10, TimeUnit.SECONDS);
    }

    private void checkFeedHealth() {
        if (!connected) {
            logger.warn("Feed desconectado, intentando reconectar");
            attemptReconnect();
            return;
        }
        Instant now = Instant.now();
        if (lastHeartbeat != null && Duration.between(lastHeartbeat, now).getSeconds() > 30) {
            logger.warn("Heartbeat del feed expirado");
            handleFeedDisconnection();
        }
    }

    private void attemptReconnect() {
        if (reconnectAttempts >= MAX_RECONNECT_ATTEMPTS) {
            logger.error("Máximo de intentos de reconexión alcanzado");
            connectionStatus = FeedConnectionStatus.FAILED;
            return;
        }
        reconnectAttempts++;
        logger.info("Intentando reconexión al feed (intento {}/{})", 
                   reconnectAttempts, MAX_RECONNECT_ATTEMPTS);
        scheduler.schedule(this::simulateConnection, 
                RECONNECT_DELAY.toMillis(), TimeUnit.MILLISECONDS);
    }

    private void handleFeedDisconnection() {
        connected = false;
        connectionStatus = FeedConnectionStatus.DISCONNECTED;
        logger.error("Feed de market data desconectado");
        auditLogger.logMarketDataEvent("FEED_DISCONNECTED",
                Map.of("timestamp", Instant.now().toString()));
        attemptReconnect();
    }

    public void subscribeToInstrument(String instrumentId) {
        if (!connected) {
            logger.warn("No se puede suscribir a {}: feed desconectado", instrumentId);
            return;
        }
        orderBooksByInstrument.putIfAbsent(instrumentId, new OrderBook(instrumentId));
        recentTradesByInstrument.putIfAbsent(instrumentId, new ArrayList<>());
        sequenceNumbersByInstrument.putIfAbsent(instrumentId, new AtomicLong(0));
        logger.info("Suscrito a instrument: {}", instrumentId);
        auditLogger.logMarketDataEvent("SUBSCRIBED", 
                Map.of("instrumentId", instrumentId));
    }

    public void unsubscribeFromInstrument(String instrumentId) {
        orderBooksByInstrument.remove(instrumentId);
        recentTradesByInstrument.remove(instrumentId);
        sequenceNumbersByInstrument.remove(instrumentId);
        logger.info("Desuscrito de instrument: {}", instrumentId);
    }

    public void processOrderBookUpdate(OrderBookUpdate update) {
        if (!connected) {
            logger.debug("Ignorando actualización de orderbook: feed desconectado");
            return;
        }
        OrderBook orderBook = orderBooksByInstrument.get(update.instrumentId());
        if (orderBook == null) {
            logger.warn("Orderbook no encontrado para instrument: {}", update.instrumentId());
            return;
        }
        for (OrderBookLevel level : update.bids()) {
            orderBook.updateBid(level.price(), level.quantity());
        }
        for (OrderBookLevel level : update.asks()) {
            orderBook.updateAsk(level.price(), level.quantity());
        }
        AtomicLong seq = sequenceNumbersByInstrument.get(update.instrumentId());
        if (seq != null) {
            seq.incrementAndGet();
        }
        lastHeartbeat = Instant.now();
        if (listener != null) {
            listener.onOrderBookUpdate(update.instrumentId(), orderBook);
        }
    }

    public void processTrade(Trade trade) {
        if (!connected) {
            logger.debug("Ignorando trade: feed desconectado");
            return;
        }
        List<Trade> trades = recentTradesByInstrument.get(trade.instrumentId());
        if (trades == null) {
            logger.warn("No hay registro de trades para instrument: {}", trade.instrumentId());
            return;
        }
        synchronized (trades) {
            trades.add(trade);
            if (trades.size() > 1000) {
                trades.remove(0);
            }
        }
        lastHeartbeat = Instant.now();
        auditLogger.logMarketDataEvent("TRADE_RECEIVED",
                Map.of(
                        "instrumentId", trade.instrumentId(),
                        "price", trade.price().toString(),
                        "quantity", trade.quantity().toString()
                ));
        if (listener != null) {
            listener.onTrade(trade);
        }
    }

    public OrderBook getOrderBook(String instrumentId) {
        return orderBooksByInstrument.get(instrumentId);
    }

    public BigDecimal getMidPrice(String instrumentId) {
        OrderBook ob = orderBooksByInstrument.get(instrumentId);
        if (ob == null) {
            return null;
        }
        BigDecimal bestBid = ob.getBestBid();
        BigDecimal bestAsk = ob.getBestAsk();
        if (bestBid == null || bestAsk == null) {
            return null;
        }
        return bestBid.add(bestAsk).divide(BigDecimal.valueOf(2), 
                java.math.RoundingMode.HALF_UP);
    }

    public BigDecimal getSpread(String instrumentId) {
        OrderBook ob = orderBooksByInstrument.get(instrumentId);
        if (ob == null) {
            return DEFAULT_SPREAD;
        }
        BigDecimal bestBid = ob.getBestBid();
        BigDecimal bestAsk = ob.getBestAsk();
        if (bestBid == null || bestAsk == null) {
            return DEFAULT_SPREAD;
        }
        return bestAsk.subtract(bestBid);
    }

    public List<Trade> getRecentTrades(String instrumentId, int limit) {
        List<Trade> trades = recentTradesByInstrument.get(instrumentId);
        if (trades == null) {
            return List.of();
        }
        synchronized (trades) {
            int size = trades.size();
            int fromIndex = Math.max(0, size - limit);
            return new ArrayList<>(trades.subList(fromIndex, size));
        }
    }

    public void setListener(MarketDataListener listener) {
        this.listener = listener;
    }

    public boolean isConnected() {
        return connected;
    }

    public FeedConnectionStatus getConnectionStatus() {
        return connectionStatus;
    }

    public void shutdown() {
        connected = false;
        connectionStatus = FeedConnectionStatus.SHUTDOWN;
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
        logger.info("MarketDataFeedAdapter detenido");
    }

    public interface MarketDataListener {
        void onOrderBookUpdate(String instrumentId, OrderBook orderBook);
        void onTrade(Trade trade);
    }

    public record OrderBookUpdate(
            String instrumentId,
            List<OrderBookLevel> bids,
            List<OrderBookLevel> asks,
            long sequenceNumber,
            Instant timestamp
    ) {}

    public record OrderBookLevel(BigDecimal price, BigDecimal quantity) {}

    public record Trade(
            String instrumentId,
            BigDecimal price,
            BigDecimal quantity,
            String side,
            Instant timestamp,
            String tradeId
    ) {}

    public static class OrderBook {
        private final String instrumentId;
        private final ConcurrentHashMap<BigDecimal, BigDecimal> bids;
        private final ConcurrentHashMap<BigDecimal, BigDecimal> asks;

        public OrderBook(String instrumentId) {
            this.instrumentId = instrumentId;
            this.bids = new ConcurrentHashMap<>();
            this.asks = new ConcurrentHashMap<>();
        }

        public void updateBid(BigDecimal price, BigDecimal quantity) {
            if (quantity.compareTo(BigDecimal.ZERO) == 0) {
                bids.remove(price);
            } else {
                bids.put(price, quantity);
            }
        }

        public void updateAsk(BigDecimal price, BigDecimal quantity) {
            if (quantity.compareTo(BigDecimal.ZERO) == 0) {
                asks.remove(price);
            } else {
                asks.put(price, quantity);
            }
        }

        public BigDecimal getBestBid() {
            return bids.keySet().stream()
                    .max(BigDecimal::compareTo)
                    .orElse(null);
        }

        public BigDecimal getBestAsk() {
            return asks.keySet().stream()
                    .min(BigDecimal::compareTo)
                    .orElse(null);
        }

        public List<OrderBookLevel> getTopBids(int depth) {
            return bids.entrySet().stream()
                    .sorted(Map.Entry.<BigDecimal, BigDecimal>comparingByKey().reversed())
                    .limit(depth)
                    .map(e -> new OrderBookLevel(e.getKey(), e.getValue()))
                    .toList();
        }

        public List<OrderBookLevel> getTopAsks(int depth) {
            return asks.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .limit(depth)
                    .map(e -> new OrderBookLevel(e.getKey(), e.getValue()))
                    .toList();
        }

        public String getInstrumentId() {
            return instrumentId;
        }

        public void clear() {
            bids.clear();
            asks.clear();
        }
    }

    public enum FeedConnectionStatus {
        DISCONNECTED,
        CONNECTING,
        CONNECTED,
        RECONNECTING,
        ERROR,
        SHUTDOWN
    }
}