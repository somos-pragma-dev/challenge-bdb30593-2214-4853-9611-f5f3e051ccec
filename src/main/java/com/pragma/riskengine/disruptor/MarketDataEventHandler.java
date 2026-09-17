package com.pragma.riskengine.disruptor;

import com.lmax.disruptor.EventHandler;
import com.pragma.riskengine.model.RiskModel;
import com.pragma.riskengine.adapter.marketdata.MarketDataFeedAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class MarketDataEventHandler implements EventHandler<MarketDataEvent> {

    private static final Logger logger = LoggerFactory.getLogger(MarketDataEventHandler.class);
    private static final int MIN_PRICE_POINTS_FOR_VAR = 30;
    private static final BigDecimal VOLATILITY_SCALING_FACTOR = new BigDecimal("1.5");

    private final RiskModel riskModel;
    private final MarketDataFeedAdapter marketDataAdapter;
    private final AtomicLong eventsProcessed;
    private final AtomicLong lastUpdateTimestamp;

    public MarketDataEventHandler(RiskModel riskModel, MarketDataFeedAdapter marketDataAdapter) {
        this.riskModel = riskModel;
        this.marketDataAdapter = marketDataAdapter;
        this.eventsProcessed = new AtomicLong(0);
        this.lastUpdateTimestamp = new AtomicLong(0);
    }

    @Override
    public void onEvent(MarketDataEvent event, long sequence, boolean endOfBatch) throws Exception {
        processMarketDataEvent(event);
    }

    private void processMarketDataEvent(MarketDataEvent event) {
        long startTime = System.nanoTime();

        try {
            switch (event.getEventType()) {
                case PRICE_UPDATE -> handlePriceUpdate(event);
                case TRADE -> handleTrade(event);
                case ORDERBOOK_SNAPSHOT -> handleOrderBookSnapshot(event);
                case VOLATILITY_UPDATE -> handleVolatilityUpdate(event);
                default -> logger.debug("Unknown market data event type: {}", event.getEventType());
            }

            eventsProcessed.incrementAndGet();
            lastUpdateTimestamp.set(System.currentTimeMillis());

            long processingTime = System.nanoTime() - startTime;
            if (processingTime > 100_000) {
                logger.debug("Market data processing took {}us for instrument {}", 
                    processingTime / 1000, event.getInstrumentId());
            }

        } catch (Exception e) {
            logger.error("Error processing market data event for {}: {}", 
                event.getInstrumentId(), e.getMessage(), e);
        }
    }

    private void handlePriceUpdate(MarketDataEvent event) {
        String instrumentId = event.getInstrumentId();
        BigDecimal price = event.getPrice();
        Instant timestamp = event.getTimestamp();

        if (instrumentId == null || price == null) {
            logger.warn("Invalid price update event: instrument={}, price={}", 
                instrumentId, price);
            return;
        }

        riskModel.recordPrice(instrumentId, price, timestamp);

        BigDecimal currentVar = riskModel.getCurrentVar();
        if (currentVar != null) {
            logger.debug("Updated VaR for instrument {}: price={}, currentVar={}", 
                instrumentId, price, currentVar);
        }
    }

    private void handleTrade(MarketDataEvent event) {
        String instrumentId = event.getInstrumentId();
        BigDecimal price = event.getPrice();
        BigDecimal quantity = event.getQuantity();

        if (instrumentId == null || price == null || quantity == null) {
            logger.warn("Invalid trade event: instrument={}, price={}, quantity={}", 
                instrumentId, price, quantity);
            return;
        }

        riskModel.recordPrice(instrumentId, price, event.getTimestamp());

        BigDecimal notional = price.abs().multiply(quantity.abs());
        logger.debug("Processed trade for {}: quantity={}, price={}, notional={}", 
            instrumentId, quantity, price, notional);
    }

    private void handleOrderBookSnapshot(MarketDataEvent event) {
        String instrumentId = event.getInstrumentId();
        List<MarketDataEvent.PriceLevel> bidLevels = event.getBidLevels();
        List<MarketDataEvent.PriceLevel> askLevels = event.getAskLevels();

        if (bidLevels == null || askLevels == null || bidLevels.isEmpty() || askLevels.isEmpty()) {
            logger.debug("Empty orderbook snapshot for {}", instrumentId);
            return;
        }

        BigDecimal bestBid = bidLevels.get(0).price();
        BigDecimal bestAsk = askLevels.get(0).price();
        BigDecimal spread = bestAsk.subtract(bestBid);

        riskModel.recordPrice(instrumentId, bestAsk, event.getTimestamp());

        logger.debug("Orderbook snapshot for {}: bestBid={}, bestAsk={}, spread={}", 
            instrumentId, bestBid, bestAsk, spread);
    }

    private void handleVolatilityUpdate(MarketDataEvent event) {
        String instrumentId = event.getInstrumentId();
        BigDecimal volatility = event.getVolatility();

        if (instrumentId == null || volatility == null) {
            logger.warn("Invalid volatility update: instrument={}, volatility={}", 
                instrumentId, volatility);
            return;
        }

        logger.info("Volatility update for {}: {}", instrumentId, volatility);
    }

    public long getEventsProcessed() {
        return eventsProcessed.get();
    }

    public long getLastUpdateTimestamp() {
        return lastUpdateTimestamp.get();
    }

    public boolean isStale(long stalenessThresholdMs) {
        long now = System.currentTimeMillis();
        return (now - lastUpdateTimestamp.get()) > stalenessThresholdMs;
    }

    public static class MarketDataEvent {
        private String instrumentId;
        private EventType eventType;
        private BigDecimal price;
        private BigDecimal quantity;
        private Instant timestamp;
        private BigDecimal volatility;
        private List<PriceLevel> bidLevels;
        private List<PriceLevel> askLevels;

        public MarketDataEvent() {
            this.timestamp = Instant.now();
            this.eventType = EventType.PRICE_UPDATE;
        }

        public void reset() {
            this.instrumentId = null;
            this.eventType = EventType.PRICE_UPDATE;
            this.price = null;
            this.quantity = null;
            this.timestamp = Instant.now();
            this.volatility = null;
            this.bidLevels = null;
            this.askLevels = null;
        }

        public record PriceLevel(BigDecimal price, BigDecimal quantity, int orderCount) {}

        public enum EventType {
            PRICE_UPDATE, TRADE, ORDERBOOK_SNAPSHOT, VOLATILITY_UPDATE
        }

        public String getInstrumentId() { return instrumentId; }
        public void setInstrumentId(String instrumentId) { this.instrumentId = instrumentId; }
        public EventType getEventType() { return eventType; }
        public void setEventType(EventType eventType) { this.eventType = eventType; }
        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }
        public BigDecimal getQuantity() { return quantity; }
        public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
        public Instant getTimestamp() { return timestamp; }
        public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
        public BigDecimal getVolatility() { return volatility; }
        public void setVolatility(BigDecimal volatility) { this.volatility = volatility; }
        public List<PriceLevel> getBidLevels() { return bidLevels; }
        public void setBidLevels(List<PriceLevel> bidLevels) { this.bidLevels = bidLevels; }
        public List<PriceLevel> getAskLevels() { return askLevels; }
        public void setAskLevels(List<PriceLevel> askLevels) { this.askLevels = askLevels; }
    }
}