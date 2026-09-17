package com.pragma.riskengine.disruptor;

import com.pragma.riskengine.model.RiskModel;
import com.pragma.riskengine.model.Threshold;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class OrderEvent {
    private String orderId;
    private String traderId;
    private String strategyId;
    private String instrumentId;
    private OrderSide side;
    private OrderType orderType;
    private BigDecimal quantity;
    private BigDecimal limitPrice;
    private BigDecimal calculatedRisk;
    private RiskScore riskScore;
    private ProcessingStatus status;
    private Instant receivedTime;
    private Instant processedTime;
    private String rejectionReason;
    private volatile RiskModel snapshotRiskModel;
    private volatile Threshold applicableThreshold;

    public OrderEvent() {
        this.orderId = UUID.randomUUID().toString();
        this.receivedTime = Instant.now();
        this.status = ProcessingStatus.PENDING;
    }

    public void reset() {
        this.orderId = UUID.randomUUID().toString();
        this.traderId = null;
        this.strategyId = null;
        this.instrumentId = null;
        this.side = null;
        this.orderType = null;
        this.quantity = null;
        this.limitPrice = null;
        this.calculatedRisk = null;
        this.riskScore = null;
        this.status = ProcessingStatus.PENDING;
        this.receivedTime = Instant.now();
        this.processedTime = null;
        this.rejectionReason = null;
        this.snapshotRiskModel = null;
        this.applicableThreshold = null;
    }

    public void applyRiskScoring(BigDecimal risk, RiskScore score) {
        this.calculatedRisk = risk;
        this.riskScore = score;
        this.processedTime = Instant.now();
    }

    public void approve() {
        this.status = ProcessingStatus.APPROVED;
    }

    public void reject(String reason) {
        this.status = ProcessingStatus.REJECTED;
        this.rejectionReason = reason;
        this.processedTime = Instant.now();
    }

    public BigDecimal calculateNotional() {
        if (quantity == null || limitPrice == null) {
            return BigDecimal.ZERO;
        }
        return quantity.multiply(limitPrice);
    }

    public boolean isBuy() {
        return side == OrderSide.BUY;
    }

    public boolean isSell() {
        return side == OrderSide.SELL;
    }

    public boolean isLimitOrder() {
        return orderType == OrderType.LIMIT;
    }

    public boolean isMarketOrder() {
        return orderType == OrderType.MARKET;
    }

    public long getProcessingTimeNanos() {
        if (receivedTime == null || processedTime == null) {
            return 0L;
        }
        return java.time.Duration.between(receivedTime, processedTime).toNanos();
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getTraderId() { return traderId; }
    public void setTraderId(String traderId) { this.traderId = traderId; }
    public String getStrategyId() { return strategyId; }
    public void setStrategyId(String strategyId) { this.strategyId = strategyId; }
    public String getInstrumentId() { return instrumentId; }
    public void setInstrumentId(String instrumentId) { this.instrumentId = instrumentId; }
    public OrderSide getSide() { return side; }
    public void setSide(OrderSide side) { this.side = side; }
    public OrderType getOrderType() { return orderType; }
    public void setOrderType(OrderType orderType) { this.orderType = orderType; }
    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
    public BigDecimal getLimitPrice() { return limitPrice; }
    public void setLimitPrice(BigDecimal limitPrice) { this.limitPrice = limitPrice; }
    public BigDecimal getCalculatedRisk() { return calculatedRisk; }
    public void setCalculatedRisk(BigDecimal calculatedRisk) { this.calculatedRisk = calculatedRisk; }
    public RiskScore getRiskScore() { return riskScore; }
    public void setRiskScore(RiskScore riskScore) { this.riskScore = riskScore; }
    public ProcessingStatus getStatus() { return status; }
    public void setStatus(ProcessingStatus status) { this.status = status; }
    public Instant getReceivedTime() { return receivedTime; }
    public void setReceivedTime(Instant receivedTime) { this.receivedTime = receivedTime; }
    public Instant getProcessedTime() { return processedTime; }
    public void setProcessedTime(Instant processedTime) { this.processedTime = processedTime; }
    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
    public RiskModel getSnapshotRiskModel() { return snapshotRiskModel; }
    public void setSnapshotRiskModel(RiskModel snapshotRiskModel) { this.snapshotRiskModel = snapshotRiskModel; }
    public Threshold getApplicableThreshold() { return applicableThreshold; }
    public void setApplicableThreshold(Threshold applicableThreshold) { this.applicableThreshold = applicableThreshold; }

    public enum OrderSide {
        BUY, SELL
    }

    public enum OrderType {
        MARKET, LIMIT, STOP, STOP_LIMIT
    }

    public enum RiskScore {
        LOW, MEDIUM, HIGH, CRITICAL
    }

    public enum ProcessingStatus {
        PENDING, APPROVED, REJECTED, ERROR
    }
}