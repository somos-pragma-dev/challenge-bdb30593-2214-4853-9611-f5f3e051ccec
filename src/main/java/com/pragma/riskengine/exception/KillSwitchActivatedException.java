package com.pragma.riskengine.exception;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public class KillSwitchActivatedException extends RuntimeException {
    private final String algorithmId;
    private final String strategyId;
    private final KillSwitchReason reason;
    private final Instant activationTime;
    private final List<String> affectedInstruments;
    private final Map<String, BigDecimal> exposureBeforeKillSwitch;
    private final BigDecimal totalExposure;
    private final BigDecimal thresholdBreached;
    private final int consecutiveBreaches;
    private final String triggerEventId;
    private final boolean autoResetEnabled;
    private final Duration autoResetDelay;
    
    public enum KillSwitchReason {
        CONSECUTIVE_FAILURES,
        ERROR_RATE_EXCEEDED,
        EXPOSURE_THRESHOLD_BREACHED,
        ANOMALY_DETECTED,
        MANUAL_TRIGGER,
        SYSTEM_HEALTH_CHECK_FAILED
    }
    
    public static class Duration {
        private final long millis;
        public Duration(long millis) { this.millis = millis; }
        public long toMillis() { return millis; }
        public static Duration ofMillis(long millis) { return new Duration(millis); }
        public static Duration ofSeconds(long seconds) { return new Duration(seconds * 1000); }
    }
    
    public KillSwitchActivatedException(String algorithmId, String strategyId,
            KillSwitchReason reason, Instant activationTime, List<String> affectedInstruments,
            Map<String, BigDecimal> exposureBeforeKillSwitch, BigDecimal totalExposure,
            BigDecimal thresholdBreached, int consecutiveBreaches, String triggerEventId,
            boolean autoResetEnabled, Duration autoResetDelay) {
        super(buildMessage(algorithmId, strategyId, reason));
        this.algorithmId = algorithmId;
        this.strategyId = strategyId;
        this.reason = reason;
        this.activationTime = activationTime;
        this.affectedInstruments = affectedInstruments;
        this.exposureBeforeKillSwitch = exposureBeforeKillSwitch;
        this.totalExposure = totalExposure;
        this.thresholdBreached = thresholdBreached;
        this.consecutiveBreaches = consecutiveBreaches;
        this.triggerEventId = triggerEventId;
        this.autoResetEnabled = autoResetEnabled;
        this.autoResetDelay = autoResetDelay;
    }
    
    private static String buildMessage(String algorithmId, String strategyId,
            KillSwitchReason reason) {
        return String.format("Kill switch activated for algorithm %s, strategy %s, reason: %s",
                algorithmId, strategyId, reason);
    }
    
    public String getAlgorithmId() { return algorithmId; }
    public String getStrategyId() { return strategyId; }
    public KillSwitchReason getReason() { return reason; }
    public Instant getActivationTime() { return activationTime; }
    public List<String> getAffectedInstruments() { return affectedInstruments; }
    public Map<String, BigDecimal> getExposureBeforeKillSwitch() { return exposureBeforeKillSwitch; }
    public BigDecimal getTotalExposure() { return totalExposure; }
    public BigDecimal getThresholdBreached() { return thresholdBreached; }
    public int getConsecutiveBreaches() { return consecutiveBreaches; }
    public String getTriggerEventId() { return triggerEventId; }
    public boolean isAutoResetEnabled() { return autoResetEnabled; }
    public Duration getAutoResetDelay() { return autoResetDelay; }
    
    public boolean canAutoReset() {
        return autoResetEnabled && autoResetDelay != null;
    }
    
    public String toAuditString() {
        return String.format("KILL_SWITCH|algorithmId=%s|strategyId=%s|reason=%s|totalExposure=%s|threshold=%s",
                algorithmId, strategyId, reason, totalExposure, thresholdBreached);
    }
}