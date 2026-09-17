package com.pragma.riskengine.audit;

import com.pragma.riskengine.exception.KillSwitchActivatedException;
import com.pragma.riskengine.exception.RiskThresholdExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public class AuditLogger {
    
    private static final Logger logger = LoggerFactory.getLogger(AuditLogger.class);
    private static final DateTimeFormatter ISO_FORMATTER = 
            DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.of("UTC"));
    
    private final String componentId;
    private final String componentType;
    private final ConcurrentMap<String, AuditEntry> recentEntries;
    private final AtomicLong sequenceNumber;
    private final boolean structuredLogging;
    private final int maxRecentEntries;
    
    public AuditLogger(String componentId, String componentType) {
        this(componentId, componentType, true, 10000);
    }
    
    public AuditLogger(String componentId, String componentType, 
            boolean structuredLogging, int maxRecentEntries) {
        this.componentId = componentId;
        this.componentType = componentType;
        this.structuredLogging = structuredLogging;
        this.maxRecentEntries = maxRecentEntries;
        this.recentEntries = new ConcurrentHashMap<>();
        this.sequenceNumber = new AtomicLong(0);
    }
    
    public void logMarketDataEvent(String eventType, Map<String, String> data) {
        AuditEntry entry = new AuditEntry(
                generateSequenceId(),
                eventType,
                Instant.now(),
                null, null, null, null
        );
        data.forEach(entry::addField);
        writeEntry(entry);
    }
    
    public void logRiskDecision(String orderId, String traderId, String strategyId,
            String instrumentId, String decision, BigDecimal riskScore, 
            BigDecimal exposure, BigDecimal threshold) {
        AuditEntry entry = new AuditEntry(
                generateSequenceId(),
                "RISK_DECISION",
                Instant.now(),
                orderId, traderId, strategyId, instrumentId
        );
        entry.addField("decision", decision);
        entry.addField("riskScore", riskScore != null ? riskScore.toPlainString() : "N/A");
        entry.addField("exposure", exposure != null ? exposure.toPlainString() : "N/A");
        entry.addField("threshold", threshold != null ? threshold.toPlainString() : "N/A");
        entry.addField("mifidIICompliance", "true");
        entry.addField("decisionTimestamp", ISO_FORMATTER.format(entry.timestamp));
        
        writeEntry(entry);
    }
    
    public void logOrderRejected(String orderId, String traderId, String strategyId,
            String instrumentId, RiskThresholdExceededException exception) {
        AuditEntry entry = new AuditEntry(
                generateSequenceId(),
                "ORDER_REJECTED",
                Instant.now(),
                orderId, traderId, strategyId, instrumentId
        );
        entry.addField("rejectionReason", "THRESHOLD_EXCEEDED");
        entry.addField("thresholdId", exception.getThresholdId());
        entry.addField("thresholdType", exception.getThresholdType().name());
        entry.addField("currentValue", exception.getCurrentValue().toPlainString());
        entry.addField("thresholdValue", exception.getThresholdValue().toPlainString());
        entry.addField("exceedPercentage", exception.getExceedPercentage().toPlainString());
        entry.addField("riskMetric", exception.getRiskMetric());
        entry.addField("isCritical", String.valueOf(exception.isCritical()));
        entry.addField("auditTrail", exception.toAuditString());
        
        writeEntry(entry);
    }
    
    public void logKillSwitchActivated(KillSwitchActivatedException exception) {
        AuditEntry entry = new AuditEntry(
                generateSequenceId(),
                "KILL_SWITCH_ACTIVATED",
                exception.getActivationTime(),
                null, null, exception.getStrategyId(), null
        );
        entry.addField("algorithmId", exception.getAlgorithmId());
        entry.addField("reason", exception.getReason().name());
        entry.addField("totalExposure", exception.getTotalExposure().toPlainString());
        entry.addField("thresholdBreached", exception.getThresholdBreached().toPlainString());
        entry.addField("consecutiveBreaches", String.valueOf(exception.getConsecutiveBreaches()));
        entry.addField("triggerEventId", exception.getTriggerEventId());
        entry.addField("autoResetEnabled", String.valueOf(exception.isAutoResetEnabled()));
        entry.addField("affectedInstruments", String.join(",", exception.getAffectedInstruments()));
        entry.addField("auditTrail", exception.toAuditString());
        
        writeEntry(entry);
    }
    
    public void logThresholdAdjustment(String thresholdId, String instrumentId,
            BigDecimal oldValue, BigDecimal newValue, BigDecimal volatility, String reason) {
        AuditEntry entry = new AuditEntry(
                generateSequenceId(),
                "THRESHOLD_ADJUSTMENT",
                Instant.now(),
                null, null, null, instrumentId
        );
        entry.addField("thresholdId", thresholdId);
        entry.addField("oldValue", oldValue.toPlainString());
        entry.addField("newValue", newValue.toPlainString());
        entry.addField("adjustmentPercentage", 
                newValue.subtract(oldValue).divide(oldValue, 4, java.math.RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100)).toPlainString());
        entry.addField("volatilityFactor", volatility != null ? volatility.toPlainString() : "N/A");
        entry.addField("reason", reason);
        
        writeEntry(entry);
    }
    
    public void logVarCalculation(String calculationId, BigDecimal var, 
            BigDecimal confidenceLevel, int lookbackPeriods, Instant calculationTime) {
        AuditEntry entry = new AuditEntry(
                generateSequenceId(),
                "VAR_CALCULATION",
                calculationTime,
                null, null, null, null
        );
        entry.addField("calculationId", calculationId);
        entry.addField("var", var.toPlainString());
        entry.addField("confidenceLevel", confidenceLevel.toPlainString());
        entry.addField("lookbackPeriods", String.valueOf(lookbackPeriods));
        entry.addField("calculationTimestamp", ISO_FORMATTER.format(calculationTime));
        
        writeEntry(entry);
    }
    
    public void logMarketDataUpdate(String instrumentId, String dataSource,
            BigDecimal bid, BigDecimal ask, int bidSize, int askSize) {
        if (!structuredLogging) {
            return;
        }
        
        AuditEntry entry = new AuditEntry(
                generateSequenceId(),
                "MARKET_DATA_UPDATE",
                Instant.now(),
                null, null, null, instrumentId
        );
        entry.addField("dataSource", dataSource);
        entry.addField("bid", bid.toPlainString());
        entry.addField("ask", ask.toPlainString());
        entry.addField("spread", ask.subtract(bid).toPlainString());
        entry.addField("bidSize", String.valueOf(bidSize));
        entry.addField("askSize", String.valueOf(askSize));
        
        writeEntry(entry);
    }
    
    private void writeEntry(AuditEntry entry) {
        recentEntries.put(entry.entryId, entry);
        cleanupOldEntries();
        
        if (structuredLogging) {
            logger.info("AUDIT|{}", entry.toStructuredString());
        } else {
            logger.info("[{}] {}: {}", entry.timestamp, entry.eventType, entry.toKeyValueString());
        }
    }
    
    private void cleanupOldEntries() {
        if (recentEntries.size() > maxRecentEntries) {
            String oldestKey = recentEntries.keys().nextElement();
            recentEntries.remove(oldestKey);
        }
    }
    
    private String generateSequenceId() {
        long seq = sequenceNumber.incrementAndGet();
        return String.format("%s-%s-%d", componentId, UUID.randomUUID().toString().substring(0, 8), seq);
    }
    
    public AuditEntry getRecentEntry(String entryId) {
        return recentEntries.get(entryId);
    }
    
    public int getRecentEntryCount() {
        return recentEntries.size();
    }
    
    public static class AuditEntry {
        private final String entryId;
        private final String eventType;
        private final Instant timestamp;
        private final String orderId;
        private final String traderId;
        private final String strategyId;
        private final String instrumentId;
        private final ConcurrentMap<String, String> fields;
        
        public AuditEntry(String entryId, String eventType, Instant timestamp,
                String orderId, String traderId, String strategyId, String instrumentId) {
            this.entryId = entryId;
            this.eventType = eventType;
            this.timestamp = timestamp;
            this.orderId = orderId;
            this.traderId = traderId;
            this.strategyId = strategyId;
            this.instrumentId = instrumentId;
            this.fields = new ConcurrentHashMap<>();
        }
        
        public void addField(String key, String value) {
            fields.put(key, value);
        }
        
        public String getEntryId() {
            return entryId;
        }
        
        public String getEventType() {
            return eventType;
        }
        
        public Instant getTimestamp() {
            return timestamp;
        }
        
        public String toStructuredString() {
            StringBuilder sb = new StringBuilder();
            sb.append("entryId=").append(entryId)
              .append("|eventType=").append(eventType)
              .append("|timestamp=").append(ISO_FORMATTER.format(timestamp));
            
            if (orderId != null) sb.append("|orderId=").append(orderId);
            if (traderId != null) sb.append("|traderId=").append(traderId);
            if (strategyId != null) sb.append("|strategyId=").append(strategyId);
            if (instrumentId != null) sb.append("|instrumentId=").append(instrumentId);
            
            fields.forEach((k, v) -> sb.append("|").append(k).append("=").append(v));
            
            return sb.toString();
        }
        
        public String toKeyValueString() {
            return fields.entrySet().stream()
                    .map(e -> e.getKey() + "=" + e.getValue())
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");
        }
    }
}