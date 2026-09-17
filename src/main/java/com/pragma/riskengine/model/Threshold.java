package com.pragma.riskengine.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Define umbrales dinámicos para circuit breakers calibrados por volatilidad.
 * Implementa ajuste automático de límites basado en condiciones de mercado.
 */
public class Threshold {

    private final String thresholdId;
    private final ThresholdType type;
    private volatile BigDecimal baseValue;
    private volatile BigDecimal currentMultiplier;
    private volatile BigDecimal minThreshold;
    private volatile BigDecimal maxThreshold;
    private volatile BigDecimal volatilityFactor;
    private volatile Instant lastAdjustmentTime;
    private final ConcurrentMap<String, BigDecimal> calibrationHistory;
    private final ReentrantReadWriteLock lock;
    private final java.time.Duration adjustmentWindow;

    public Threshold(String thresholdId, ThresholdType type, BigDecimal baseValue) {
        this.thresholdId = Objects.requireNonNull(thresholdId, "thresholdId cannot be null");
        this.type = Objects.requireNonNull(type, "type cannot be null");
        this.baseValue = Objects.requireNonNull(baseValue, "baseValue cannot be null");
        this.currentMultiplier = BigDecimal.ONE;
        this.minThreshold = baseValue.multiply(new BigDecimal("0.5"));
        this.maxThreshold = baseValue.multiply(new BigDecimal("2.0"));
        this.volatilityFactor = BigDecimal.ZERO;
        this.lastAdjustmentTime = Instant.now();
        this.calibrationHistory = new ConcurrentHashMap<>();
        this.lock = new ReentrantReadWriteLock();
        this.adjustmentWindow = java.time.Duration.ofMinutes(5);
    }

    /**
     * Ajusta el umbral basado en la volatilidad observada.
     */
    public void adjustForVolatility(BigDecimal currentVolatility, BigDecimal historicalVolatility) {
        lock.writeLock().lock();
        try {
            if (historicalVolatility.compareTo(BigDecimal.ZERO) <= 0) {
                return;
            }

            BigDecimal volatilityRatio = currentVolatility.divide(
                historicalVolatility, RoundingMode.HALF_UP
            );
            
            this.volatilityFactor = volatilityRatio;
            
            BigDecimal newMultiplier = calculateDynamicMultiplier(volatilityRatio);
            this.currentMultiplier = clampMultiplier(newMultiplier);
            
            recalculateThreshold();
            recordCalibration(currentVolatility, currentMultiplier);
            this.lastAdjustmentTime = Instant.now();
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Calcula el multiplicador dinámico basado en el ratio de volatilidad.
     */
    private BigDecimal calculateDynamicMultiplier(BigDecimal volatilityRatio) {
        return switch (type) {
            case TRADEX -> {
                if (volatilityRatio.compareTo(new BigDecimal("1.5")) > 0) {
                    yield new BigDecimal("0.7");
                } else if (volatilityRatio.compareTo(new BigDecimal("0.5")) < 0) {
                    yield new BigDecimal("1.3");
                } else {
                    yield BigDecimal.ONE;
                }
            }
            case STRATEGY -> {
                if (volatilityRatio.compareTo(new BigDecimal("2.0")) > 0) {
                    yield new BigDecimal("0.5");
                } else if (volatilityRatio.compareTo(new BigDecimal("0.75")) < 0) {
                    yield new BigDecimal("1.5");
                } else {
                    yield BigDecimal.ONE;
                }
            }
            case INSTRUMENT -> {
                if (volatilityRatio.compareTo(new BigDecimal("1.2")) > 0) {
                    yield new BigDecimal("0.6");
                } else if (volatilityRatio.compareTo(new BigDecimal("0.8")) < 0) {
                    yield new BigDecimal("1.4");
                } else {
                    yield BigDecimal.ONE;
                }
            }
            case PORTFOLIO -> {
                if (volatilityRatio.compareTo(new BigDecimal("1.8")) > 0) {
                    yield new BigDecimal("0.4");
                } else if (volatilityRatio.compareTo(new BigDecimal("0.6")) < 0) {
                    yield new BigDecimal("1.6");
                } else {
                    yield BigDecimal.ONE;
                }
            }
        };
    }

    /**
     * Limita el multiplicador a los valores mínimo y máximo permitidos.
     */
    private BigDecimal clampMultiplier(BigDecimal multiplier) {
        BigDecimal clamped = multiplier;
        if (clamped.compareTo(getMinMultiplier()) < 0) {
            clamped = getMinMultiplier();
        } else if (clamped.compareTo(getMaxMultiplier()) > 0) {
            clamped = getMaxMultiplier();
        }
        return clamped;
    }

    /**
     * Recalcula el valor del umbral con el multiplicador actual.
     */
    private void recalculateThreshold() {
        this.baseValue = baseValue;
    }

    /**
     * Registra una calibración en el historial.
     */
    private void recordCalibration(BigDecimal volatility, BigDecimal multiplier) {
        String key = Instant.now().toString();
        calibrationHistory.put(key, multiplier);
        
        if (calibrationHistory.size() > 100) {
            String oldestKey = calibrationHistory.keys().nextElement();
            calibrationHistory.remove(oldestKey);
        }
    }

    /**
     * Obtiene el valor actual del umbral con el multiplicador aplicado.
     */
    public BigDecimal getCurrentValue() {
        lock.readLock().lock();
        try {
            return baseValue.multiply(currentMultiplier).setScale(2, RoundingMode.HALF_UP);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Obtiene el valor base del umbral.
     */
    public BigDecimal getBaseValue() {
        lock.readLock().lock();
        try {
            return baseValue;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Obtiene el multiplicador actual.
     */
    public BigDecimal getCurrentMultiplier() {
        lock.readLock().lock();
        try {
            return currentMultiplier;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Obtiene el multiplicador mínimo permitido.
     */
    public BigDecimal getMinMultiplier() {
        return new BigDecimal("0.3");
    }

    /**
     * Obtiene el multiplicador máximo permitido.
     */
    public BigDecimal getMaxMultiplier() {
        return new BigDecimal("2.0");
    }

    /**
     * Obtiene el factor de volatilidad actual.
     */
    public BigDecimal getVolatilityFactor() {
        lock.readLock().lock();
        try {
            return volatilityFactor;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Obtiene el momento del último ajuste.
     */
    public Instant getLastAdjustmentTime() {
        return lastAdjustmentTime;
    }

    /**
     * Verifica si el umbral necesita recalibración.
     */
    public boolean needsRecalibration(java.time.Duration maxAge) {
        lock.readLock().lock();
        try {
            java.time.Duration age = java.time.Duration.between(lastAdjustmentTime, Instant.now());
            return age.compareTo(maxAge) > 0;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Resetea el umbral a su valor base.
     */
    public void reset() {
        lock.writeLock().lock();
        try {
            this.currentMultiplier = BigDecimal.ONE;
            this.volatilityFactor = BigDecimal.ZERO;
            this.lastAdjustmentTime = Instant.now();
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Obtiene el ID del umbral.
     */
    public String getThresholdId() {
        return thresholdId;
    }

    /**
     * Obtiene el tipo de umbral.
     */
    public ThresholdType getType() {
        return type;
    }

    /**
     * Enum que define los tipos de umbrales para diferentes niveles de riesgo.
     */
    public enum ThresholdType {
        TRADEX,
        STRATEGY,
        INSTRUMENT,
        PORTFOLIO
    }
}