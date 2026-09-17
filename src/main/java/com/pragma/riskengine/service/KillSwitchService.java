package com.pragma.riskengine.service;

import com.pragma.riskengine.audit.AuditLogger;
import com.pragma.riskengine.model.RiskModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class KillSwitchService {
    private static final Logger logger = LoggerFactory.getLogger(KillSwitchService.class);
    private static final int DEFAULT_CONSECUTIVE_FAILURES_THRESHOLD = 5;
    private static final int DEFAULT_ERROR_RATE_WINDOW = 60;
    private static final BigDecimal DEFAULT_ERROR_RATE_THRESHOLD = new BigDecimal("0.3");
    private static final Duration DEFAULT_RESET_DURATION = Duration.ofMinutes(5);
    private static final int MAX_CONSECUTIVE_PINGS_FAILURES = 3;
    private static final BigDecimal DEFAULT_CRITICAL_EXPOSURE_THRESHOLD = new BigDecimal("500000");

    private final AuditLogger auditLogger;
    private final RiskModel riskModel;
    private final ScheduledExecutorService scheduler;
    private final Map<String, StrategyKillSwitch> strategyKillSwitches;
    private final Map<String, AtomicInteger> consecutiveFailuresByStrategy;
    private final Map<String, AtomicInteger> errorCountByStrategy;
    private final Map<String, Instant> lastErrorTimestampByStrategy;
    private final AtomicBoolean globalKillSwitchActive;
    private volatile boolean enableAutoRecovery;
    private volatile Instant lastGlobalCheck;
    private volatile BigDecimal criticalExposureThreshold;
    private volatile boolean healthyState = true;
    private final Map<String, List<OrderSubmissionRecord>> orderSubmissionsByTrader;
    private final Map<String, String> activationReasons;
    private final Object stateLock;

    public KillSwitchService(AuditLogger auditLogger) {
        this(auditLogger, null);
    }

    public KillSwitchService(AuditLogger auditLogger, RiskModel riskModel) {
        this.auditLogger = auditLogger;
        this.riskModel = riskModel;
        this.scheduler = Executors.newScheduledThreadPool(2);
        this.strategyKillSwitches = new ConcurrentHashMap<>();
        this.consecutiveFailuresByStrategy = new ConcurrentHashMap<>();
        this.errorCountByStrategy = new ConcurrentHashMap<>();
        this.lastErrorTimestampByStrategy = new ConcurrentHashMap<>();
        this.globalKillSwitchActive = new AtomicBoolean(false);
        this.enableAutoRecovery = true;
        this.criticalExposureThreshold = DEFAULT_CRITICAL_EXPOSURE_THRESHOLD;
        this.orderSubmissionsByTrader = new ConcurrentHashMap<>();
        this.activationReasons = new ConcurrentHashMap<>();
        this.stateLock = new Object();
        startMonitoringTasks();
    }

    private void startMonitoringTasks() {
        scheduler.scheduleAtFixedRate(this::performGlobalHealthCheck, 30, 30, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(this::cleanupStaleErrorCounts, 60, 60, TimeUnit.SECONDS);
    }

    public void recordFailure(String strategyId, String reason) {
        consecutiveFailuresByStrategy.computeIfAbsent(strategyId, k -> new AtomicInteger(0)).incrementAndGet();
        errorCountByStrategy.computeIfAbsent(strategyId, k -> new AtomicInteger(0)).incrementAndGet();
        lastErrorTimestampByStrategy.put(strategyId, Instant.now());

        if (consecutiveFailuresByStrategy.get(strategyId).get() >= DEFAULT_CONSECUTIVE_FAILURES_THRESHOLD) {
            activateKillSwitch(strategyId, reason);
        }
        checkErrorRateThreshold(strategyId);
    }

    public void recordSuccess(String strategyId) {
        AtomicInteger failures = consecutiveFailuresByStrategy.get(strategyId);
        if (failures != null) {
            failures.set(0);
        }
    }

    private void checkErrorRateThreshold(String strategyId) {
        AtomicInteger errorCount = errorCountByStrategy.get(strategyId);
        Instant lastError = lastErrorTimestampByStrategy.get(strategyId);

        if (errorCount != null && lastError != null) {
            long windowSeconds = Duration.between(lastError, Instant.now()).getSeconds();
            if (windowSeconds < DEFAULT_ERROR_RATE_WINDOW) {
                double errorRate = (double) errorCount.get() / DEFAULT_ERROR_RATE_WINDOW;
                if (errorRate > DEFAULT_ERROR_RATE_THRESHOLD.doubleValue()) {
                    activateKillSwitch(strategyId, "HIGH_ERROR_RATE");
                }
            }
        }
    }

    public void activateKillSwitch(String strategyId, String reason) {
        activateKillSwitch(strategyId, reason, BigDecimal.ZERO);
    }

    public void activateKillSwitch(String strategyId, String reason, BigDecimal exposure) {
        synchronized (stateLock) {
            StrategyKillSwitch switchObj = strategyKillSwitches.computeIfAbsent(
                strategyId, k -> new StrategyKillSwitch(strategyId, false, null, Instant.now()));
            switchObj = new StrategyKillSwitch(strategyId, true, reason, Instant.now());
            strategyKillSwitches.put(strategyId, switchObj);
            activationReasons.put(strategyId, reason);
            logger.warn("Kill switch activated for strategy: {}, reason: {}, exposure: {}", strategyId, reason, exposure);
        }
    }

    public void deactivateKillSwitch(String strategyId) {
        synchronized (stateLock) {
            strategyKillSwitches.remove(strategyId);
            activationReasons.remove(strategyId);
            consecutiveFailuresByStrategy.get(strategyId).set(0);
            logger.info("Kill switch deactivated for strategy: {}", strategyId);
        }
    }

    public boolean isKillSwitchActive(String strategyId) {
        StrategyKillSwitch sw = strategyKillSwitches.get(strategyId);
        return sw != null && sw.active();
    }

    public void activateGlobalKillSwitch(String reason) {
        globalKillSwitchActive.set(true);
        logger.error("GLOBAL KILL SWITCH ACTIVATED: {}", reason);
    }

    public void deactivateGlobalKillSwitch() {
        globalKillSwitchActive.set(false);
        logger.info("Global kill switch deactivated");
    }

    private void performGlobalHealthCheck() {
        lastGlobalCheck = Instant.now();
        if (riskModel != null) {
            BigDecimal totalExposure = riskModel.calculateTotalExposure();
            if (totalExposure.compareTo(criticalExposureThreshold) > 0) {
                activateGlobalKillSwitch("CRITICAL_EXPOSURE: " + totalExposure);
            }
        }
        checkAutoRecoveryConditions();
    }

    private void checkAutoRecoveryConditions() {
        if (!enableAutoRecovery) return;
        strategyKillSwitches.keySet().forEach(this::attemptAutoRecovery);
    }

    private void attemptAutoRecovery(String strategyId) {
        StrategyKillSwitch sw = strategyKillSwitches.get(strategyId);
        if (sw != null && sw.active()) {
            Instant activatedAt = sw.activatedAt();
            if (activatedAt != null && Duration.between(activatedAt, Instant.now()).compareTo(DEFAULT_RESET_DURATION) > 0) {
                deactivateKillSwitch(strategyId);
                logger.info("Auto-recovered kill switch for strategy: {}", strategyId);
            }
        }
    }

    private void cleanupStaleErrorCounts() {
        Instant cutoff = Instant.now().minusSeconds(DEFAULT_ERROR_RATE_WINDOW * 2);
        lastErrorTimestampByStrategy.entrySet().removeIf(entry -> entry.getValue().isBefore(cutoff));
    }

    public Map<String, StrategyKillSwitch> getActiveKillSwitches() {
        return Collections.unmodifiableMap(strategyKillSwitches);
    }

    public boolean isGlobalKillSwitchActive() {
        return globalKillSwitchActive.get();
    }

    public void setEnableAutoRecovery(boolean enable) {
        this.enableAutoRecovery = enable;
    }

    public void shutdown() {
        scheduler.shutdown();
    }

    // Métodos adicionales requeridos por los tests

    public void setCriticalExposureThreshold(BigDecimal threshold) {
        this.criticalExposureThreshold = threshold;
    }

    public void checkAndUpdateKillSwitch() {
        if (riskModel != null) {
            BigDecimal totalExposure = riskModel.calculateTotalExposure();
            if (totalExposure.compareTo(criticalExposureThreshold) > 0) {
                activateGlobalKillSwitch("EXPOSURE_THRESHOLD_EXCEEDED");
            }
            Map<String, Position> positions = riskModel.getAllPositions();
            for (Position pos : positions.values()) {
                BigDecimal exposure = riskModel.calculateExposureByInstrument(pos.instrumentId());
                if (exposure.compareTo(criticalExposureThreshold.multiply(new BigDecimal("0.5"))) > 0) {
                    activateKillSwitch(pos.instrumentId(), "CONCENTRATION_EXCEEDED", exposure);
                }
            }
        }
    }

    public boolean checkAnomalyPattern(String traderId, BigDecimal orderSize, BigDecimal accountBalance) {
        if (accountBalance.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal ratio = orderSize.divide(accountBalance, 4, java.math.RoundingMode.HALF_UP);
            if (ratio.compareTo(new BigDecimal("0.3")) > 0) {
                activateKillSwitch(traderId, "ANOMALOUS_ORDER_SIZE");
                return false;
            }
        }
        return true;
    }

    public void recordOrderSubmission(String traderId, BigDecimal orderSize) {
        orderSubmissionsByTrader.computeIfAbsent(traderId, k -> Collections.synchronizedList(new ArrayList<>()))
            .add(new OrderSubmissionRecord(orderSize, Instant.now()));
        cleanupOldOrderSubmissions(traderId);
    }

    private void cleanupOldOrderSubmissions(String traderId) {
        List<OrderSubmissionRecord> records = orderSubmissionsByTrader.get(traderId);
        if (records != null) {
            Instant cutoff = Instant.now().minusSeconds(60);
            records.removeIf(r -> r.timestamp().isBefore(cutoff));
        }
    }

    public boolean hasRapidOrderAnomaly(String traderId, int threshold, Duration window) {
        List<OrderSubmissionRecord> records = orderSubmissionsByTrader.get(traderId);
        if (records == null) return false;
        Instant cutoff = Instant.now().minus(window);
        long recentCount = records.stream().filter(r -> r.timestamp().isAfter(cutoff)).count();
        return recentCount >= threshold;
    }

    public boolean hasExcessiveConcentration(String instrumentId, BigDecimal exposure, BigDecimal threshold) {
        return exposure.compareTo(threshold) > 0;
    }

    public String getLastActivationReason() {
        return activationReasons.values().stream()
            .reduce((first, second) -> second)
            .orElse(null);
    }

    public boolean manualReset(String adminId) {
        int count = strategyKillSwitches.size();
        strategyKillSwitches.clear();
        activationReasons.clear();
        globalKillSwitchActive.set(false);
        logger.info("Manual reset performed by admin: {}, cleared {} kill switches", adminId, count);
        return count > 0;
    }

    public boolean detectAlgorithmAnomaly(String strategyId, BigDecimal[] prices) {
        if (prices == null || prices.length < 3) return false;
        BigDecimal totalChange = prices[prices.length - 1].subtract(prices[0]);
        BigDecimal avgPrice = Arrays.stream(prices).reduce(BigDecimal.ZERO, BigDecimal::add)
            .divide(new BigDecimal(prices.length), 4, java.math.RoundingMode.HALF_UP);
        BigDecimal percentChange = totalChange.abs().divide(avgPrice, 4, java.math.RoundingMode.HALF_UP);
        return percentChange.compareTo(new BigDecimal("0.5")) > 0;
    }

    public boolean detectPriceManipulation(String instrumentId, BigDecimal[] prices) {
        if (prices == null || prices.length < 4) return false;
        for (int i = 1; i < prices.length - 1; i++) {
            BigDecimal prev = prices[i - 1];
            BigDecimal curr = prices[i];
            BigDecimal next = prices[i + 1];
            BigDecimal change1 = curr.subtract(prev).abs();
            BigDecimal change2 = next.subtract(curr).abs();
            if (change1.compareTo(prev.multiply(new BigDecimal("0.3"))) > 0 &&
                change2.compareTo(curr.multiply(new BigDecimal("0.3"))) > 0) {
                if ((prev.compareTo(curr) > 0 && next.compareTo(curr) < 0) ||
                    (prev.compareTo(curr) < 0 && next.compareTo(curr) > 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setHealthyState(boolean healthy) {
        this.healthyState = healthy;
    }

    public boolean preCheckApproval() {
        return healthyState && !globalKillSwitchActive.get();
    }

    public record StrategyKillSwitch(String strategyId, boolean active, String reason, Instant activatedAt) {}

    private record OrderSubmissionRecord(BigDecimal size, Instant timestamp) {}

    public record Position(String instrumentId, BigDecimal quantity, BigDecimal notional) {}
}