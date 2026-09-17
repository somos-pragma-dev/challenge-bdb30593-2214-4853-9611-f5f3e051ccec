package com.pragma.riskengine.exception;

import java.math.BigDecimal;
import java.time.Instant;

public class RiskThresholdExceededException extends RuntimeException {
    
    private final String orderId;
    private final String traderId;
    private final String strategyId;
    private final String instrumentId;
    private final String thresholdId;
    private final ThresholdType thresholdType;
    private final BigDecimal currentValue;
    private final BigDecimal thresholdValue;
    private final BigDecimal exceedPercentage;
    private final Instant timestamp;
    private final String riskMetric;
    
    public enum ThresholdType {
        TRADER_EXPOSURE,
        STRATEGY_EXPOSURE,
        INSTRUMENT_EXPOSURE,
        VAR_LIMIT,
        POSITION_LIMIT,
        NOTIONAL_LIMIT,
        CONCENTRATION_LIMIT,
        CORRELATION_LIMIT
    }
    
    public RiskThresholdExceededException(String orderId, String traderId, String strategyId,
            String instrumentId, String thresholdId, ThresholdType thresholdType,
            BigDecimal currentValue, BigDecimal thresholdValue, String riskMetric) {
        super(buildMessage(orderId, thresholdId, currentValue, thresholdValue, riskMetric));
        this.orderId = orderId;
        this.traderId = traderId;
        this.strategyId = strategyId;
        this.instrumentId = instrumentId;
        this.thresholdId = thresholdId;
        this.thresholdType = thresholdType;
        this.currentValue = currentValue;
        this.thresholdValue = thresholdValue;
        this.riskMetric = riskMetric;
        this.timestamp = Instant.now();
        
        if (thresholdValue.compareTo(BigDecimal.ZERO) > 0) {
            this.exceedPercentage = currentValue
                .subtract(thresholdValue)
                .divide(thresholdValue, 4, java.math.RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
        } else {
            this.exceedPercentage = BigDecimal.ZERO;
        }
    }
    
    private static String buildMessage(String orderId, String thresholdId, 
            BigDecimal currentValue, BigDecimal thresholdValue, String riskMetric) {
        return String.format("Risk threshold exceeded for order %s: %s value %.2f exceeds threshold %.2f (limit: %s)",
                orderId, riskMetric, currentValue, thresholdValue, thresholdId);
    }
    
    public String getOrderId() {
        return orderId;
    }
    
    public String getTraderId() {
        return traderId;
    }
    
    public String getStrategyId() {
        return strategyId;
    }
    
    public String getInstrumentId() {
        return instrumentId;
    }
    
    public String getThresholdId() {
        return thresholdId;
    }
    
    public ThresholdType getThresholdType() {
        return thresholdType;
    }
    
    public BigDecimal getCurrentValue() {
        return currentValue;
    }
    
    public BigDecimal getThresholdValue() {
        return thresholdValue;
    }
    
    public BigDecimal getExceedPercentage() {
        return exceedPercentage;
    }
    
    public Instant getTimestamp() {
        return timestamp;
    }
    
    public String getRiskMetric() {
        return riskMetric;
    }
    
    public boolean isCritical() {
        return exceedPercentage.compareTo(BigDecimal.valueOf(50)) > 0;
    }
    
    public boolean isModerate() {
        return exceedPercentage.compareTo(BigDecimal.valueOf(20)) > 0 
            && exceedPercentage.compareTo(BigDecimal.valueOf(50)) <= 0;
    }
    
    public String toAuditString() {
        return String.format("REJECTED|orderId=%s|traderId=%s|strategyId=%s|instrumentId=%s|" +
                "thresholdId=%s|type=%s|current=%.2f|threshold=%.2f|exceedPct=%.2f%%|riskMetric=%s|timestamp=%s",
                orderId, traderId, strategyId, instrumentId, thresholdId, thresholdType,
                currentValue, thresholdValue, exceedPercentage, riskMetric, timestamp);
    }
}