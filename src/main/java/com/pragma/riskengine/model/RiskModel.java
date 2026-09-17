package com.pragma.riskengine.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Modelo de riesgo que encapsula el cálculo de VaR intraday y exposición por trader/estrategia/instrumento.
 * Implementa un modelo de riesgo paramétrico con actualización en tiempo real.
 */
public class RiskModel {

    private final ConcurrentMap<String, Position> positionsByInstrument;
    private final ConcurrentMap<String, BigDecimal> exposureByTrader;
    private final ConcurrentMap<String, BigDecimal> exposureByStrategy;
    private final ConcurrentMap<String, BigDecimal> exposureByInstrument;
    private final Map<String, List<PricePoint>> priceHistory;
    private final ReentrantReadWriteLock lock;
    private final BigDecimal confidenceLevel;
    private final int lookbackPeriods;
    private volatile BigDecimal currentVar;
    private volatile Instant lastCalculationTime;

    public RiskModel(BigDecimal confidenceLevel, int lookbackPeriods) {
        this.positionsByInstrument = new ConcurrentHashMap<>();
        this.exposureByTrader = new ConcurrentHashMap<>();
        this.exposureByStrategy = new ConcurrentHashMap<>();
        this.exposureByInstrument = new ConcurrentHashMap<>();
        this.priceHistory = new ConcurrentHashMap<>();
        this.lock = new ReentrantReadWriteLock();
        this.confidenceLevel = Objects.requireNonNull(confidenceLevel, "confidenceLevel cannot be null");
        this.lookbackPeriods = lookbackPeriods > 0 ? lookbackPeriods : 252;
        this.currentVar = BigDecimal.ZERO;
        this.lastCalculationTime = Instant.now();
    }

    /**
     * Actualiza la posición para un instrumento específico.
     */
    public void updatePosition(String instrumentId, BigDecimal quantity, BigDecimal price) {
        lock.writeLock().lock();
        try {
            Position position = positionsByInstrument.computeIfAbsent(
                instrumentId,
                k -> new Position(instrumentId, BigDecimal.ZERO, BigDecimal.ZERO)
            );
            BigDecimal newQuantity = position.quantity().add(quantity);
            BigDecimal newNotional = newQuantity.multiply(price);
            positionsByInstrument.put(instrumentId, new Position(instrumentId, newQuantity, newNotional));
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Registra un precio histórico para el cálculo de volatilidad.
     */
    public void recordPrice(String instrumentId, BigDecimal price, Instant timestamp) {
        lock.writeLock().lock();
        try {
            List<PricePoint> history = priceHistory.computeIfAbsent(instrumentId, k -> new ArrayList<>());
            history.add(new PricePoint(price, timestamp));
            if (history.size() > lookbackPeriods) {
                history.remove(0);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Calcula el VaR intraday usando el método de varianza-covarianza.
     */
    public BigDecimal calculateVar() {
        lock.readLock().lock();
        try {
            if (positionsByInstrument.isEmpty()) {
                return BigDecimal.ZERO;
            }

            BigDecimal portfolioValue = calculateTotalExposure();
            BigDecimal portfolioVolatility = calculatePortfolioVolatility();
            BigDecimal zScore = getZScoreForConfidence(confidenceLevel);
            
            BigDecimal var = portfolioValue
                .multiply(portfolioVolatility)
                .multiply(zScore)
                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);

            currentVar = var;
            lastCalculationTime = Instant.now();
            return var;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Calcula la exposición total del portafolio.
     */
    public BigDecimal calculateTotalExposure() {
        lock.readLock().lock();
        try {
            return positionsByInstrument.values().stream()
                .map(Position::notional)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Calcula la exposición por trader específico.
     */
    public BigDecimal calculateExposureByTrader(String traderId) {
        lock.readLock().lock();
        try {
            return exposureByTrader.getOrDefault(traderId, BigDecimal.ZERO);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Calcula la exposición por estrategia específica.
     */
    public BigDecimal calculateExposureByStrategy(String strategyId) {
        lock.readLock().lock();
        try {
            return exposureByStrategy.getOrDefault(strategyId, BigDecimal.ZERO);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Calcula la exposición por instrumento específico.
     */
    public BigDecimal calculateExposureByInstrument(String instrumentId) {
        lock.readLock().lock();
        try {
            Position pos = positionsByInstrument.get(instrumentId);
            return pos != null ? pos.notional() : BigDecimal.ZERO;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Actualiza la exposición para un trader después de una operación.
     */
    public void updateTraderExposure(String traderId, BigDecimal notionalDelta) {
        lock.writeLock().lock();
        try {
            BigDecimal current = exposureByTrader.getOrDefault(traderId, BigDecimal.ZERO);
            exposureByTrader.put(traderId, current.add(notionalDelta));
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Actualiza la exposición para una estrategia después de una operación.
     */
    public void updateStrategyExposure(String strategyId, BigDecimal notionalDelta) {
        lock.writeLock().lock();
        try {
            BigDecimal current = exposureByStrategy.getOrDefault(strategyId, BigDecimal.ZERO);
            exposureByStrategy.put(strategyId, current.add(notionalDelta));
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Calcula la volatilidad del portafolio basada en el histórico de precios.
     */
    private BigDecimal calculatePortfolioVolatility() {
        if (priceHistory.isEmpty()) {
            return new BigDecimal("0.20");
        }

        List<BigDecimal> returns = new ArrayList<>();
        for (List<PricePoint> history : priceHistory.values()) {
            if (history.size() < 2) continue;
            
            for (int i = 1; i < history.size(); i++) {
                BigDecimal priceCurrent = history.get(i).price();
                BigDecimal pricePrevious = history.get(i - 1).price();
                if (pricePrevious.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal ret = priceCurrent
                        .subtract(pricePrevious)
                        .divide(pricePrevious, RoundingMode.HALF_UP);
                    returns.add(ret);
                }
            }
        }

        if (returns.isEmpty()) {
            return new BigDecimal("0.20");
        }

        BigDecimal mean = returns.stream()
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .divide(BigDecimal.valueOf(returns.size()), RoundingMode.HALF_UP);

        BigDecimal variance = returns.stream()
            .map(r -> r.subtract(mean).pow(2))
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .divide(BigDecimal.valueOf(returns.size()), RoundingMode.HALF_UP);

        return BigDecimal.valueOf(Math.sqrt(variance.doubleValue() * 252));
    }

    /**
     * Obtiene el Z-score correspondiente al nivel de confianza.
     */
    private BigDecimal getZScoreForConfidence(BigDecimal confidence) {
        return switch (confidence.compareTo(new BigDecimal("0.99"))) {
            case 0, 1 -> new BigDecimal("2.33");
            case -1 when confidence.compareTo(new BigDecimal("0.95")) >= 0 -> new BigDecimal("1.65");
            default -> new BigDecimal("1.28");
        };
    }

    /**
     * Obtiene el VaR actual calculado.
     */
    public BigDecimal getCurrentVar() {
        lock.readLock().lock();
        try {
            return currentVar;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Obtiene el momento del último cálculo de VaR.
     */
    public Instant getLastCalculationTime() {
        return lastCalculationTime;
    }

    /**
     * Obtiene todas las posiciones actuales.
     */
    public Map<String, Position> getAllPositions() {
        lock.readLock().lock();
        try {
            return Map.copyOf(positionsByInstrument);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Reinicia el modelo de riesgo.
     */
    public void reset() {
        lock.writeLock().lock();
        try {
            positionsByInstrument.clear();
            exposureByTrader.clear();
            exposureByStrategy.clear();
            exposureByInstrument.clear();
            priceHistory.clear();
            currentVar = BigDecimal.ZERO;
            lastCalculationTime = Instant.now();
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Record que representa una posición en un instrumento.
     */
    public record Position(String instrumentId, BigDecimal quantity, BigDecimal notional) {
        public Position {
            Objects.requireNonNull(instrumentId, "instrumentId cannot be null");
            if (quantity == null || notional == null) {
                throw new IllegalArgumentException("Quantity and notional cannot be null");
            }
        }
    }

    /**
     * Record que representa un punto de precio histórico.
     */
    public record PricePoint(BigDecimal price, Instant timestamp) {
        public PricePoint {
            Objects.requireNonNull(price, "price cannot be null");
            Objects.requireNonNull(timestamp, "timestamp cannot be null");
        }
    }
}