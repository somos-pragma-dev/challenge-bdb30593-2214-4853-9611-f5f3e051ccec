package com.pragma.riskengine.service;

import com.pragma.riskengine.audit.AuditLogger;
import com.pragma.riskengine.config.ResilienceConfig;
import com.pragma.riskengine.exception.RiskThresholdExceededException;
import com.pragma.riskengine.model.RiskModel;
import com.pragma.riskengine.model.Threshold;
import com.pragma.riskengine.model.Threshold.ThresholdType;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ConcurrentHashMap;

public class RiskScoringService {
    private static final Logger logger = LoggerFactory.getLogger(RiskScoringService.class);
    private static final BigDecimal DEFAULT_CONFIDENCE_LEVEL = new BigDecimal("0.95");
    private static final int DEFAULT_LOOKBACK_PERIODS = 100;
    private static final BigDecimal CIRCUIT_BREAKER_FAILURE_RATE_THRESHOLD = new BigDecimal("0.50");
    private static final int CIRCUIT_BREAKER_WAIT_DURATION_SECONDS = 60;
    private static final BigDecimal MAX_EXPOSURE_PER_ORDER = new BigDecimal("100000");
    private static final BigDecimal MAX_DAILY_LOSS = new BigDecimal("500000");
    
    private final RiskModel riskModel;
    private final CircuitBreakerRegistry circuitBreakerRegistry;
    private final RetryRegistry retryRegistry;
    private final AuditLogger auditLogger;
    private final ScheduledExecutorService scheduler;
    private final Map<String, CircuitBreaker> circuitBreakersByStrategy;
    private final Map<String, Threshold> thresholdsByInstrument;
    private final Map<String, BigDecimal> dailyPnLByTrader;
    private volatile boolean enableCircuitBreakers;
    private volatile Instant lastRiskRecalculation;
    private final Object recalculationLock;
    
    public RiskScoringService(AuditLogger auditLogger) {
        this(auditLogger, new RiskModel(DEFAULT_CONFIDENCE_LEVEL, DEFAULT_LOOKBACK_PERIODS));
    }
    
    public RiskScoringService(AuditLogger auditLogger, RiskModel riskModel) {
        this.auditLogger = auditLogger;
        this.riskModel = riskModel;
        this.circuitBreakersByStrategy = new ConcurrentHashMap<>();
        this.thresholdsByInstrument = new ConcurrentHashMap<>();
        this.dailyPnLByTrader = new ConcurrentHashMap<>();
        this.circuitBreakerRegistry = CircuitBreakerRegistry.ofDefaults();
        this.retryRegistry = RetryRegistry.ofDefaults();
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.enableCircuitBreakers = false;
        this.recalculationLock = new Object();
        initializeCircuitBreakerRegistry();
        initializeRetryRegistry();
        startRiskRecalculationTask();
    }
    
    private void initializeCircuitBreakerRegistry() {
        logger.info("Inicializando circuit breaker registry");
    }
    
    private void initializeRetryRegistry() {
        logger.info("Inicializando retry registry");
    }
    
    private void startRiskRecalculationTask() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                recalculateRiskMetrics();
            } catch (Exception e) {
                logger.error("Error en recalculación de métricas de riesgo", e);
            }
        }, 60, 60, TimeUnit.SECONDS);
    }
    
    public record OrderRequest(
        String orderId,
        String traderId,
        String strategyId,
        String instrumentId,
        BigDecimal quantity,
        BigDecimal price,
        String side
    ) {}
    
    public record RiskScoreResult(
        boolean approved,
        BigDecimal riskScore,
        BigDecimal exposure,
        BigDecimal threshold,
        String rejectionReason
    ) {}
    
    public RiskScoreResult calculateRiskScore(OrderRequest order) {
        validateOrderRequest(order);
        
        try {
            checkKillSwitchStatus(order.strategyId());
            
            BigDecimal orderNotional = order.price().multiply(order.quantity());
            validateExposureLimits(order, orderNotional);
            
            BigDecimal currentVar = riskModel.getCurrentVar();
            BigDecimal orderRiskContribution = calculateOrderRiskContribution(order, currentVar);
            
            boolean approved = orderRiskContribution.compareTo(MAX_EXPOSURE_PER_ORDER) < 0;
            
            return new RiskScoreResult(
                approved,
                orderRiskContribution,
                riskModel.calculateTotalExposure(),
                MAX_EXPOSURE_PER_ORDER,
                approved ? null : "Risk score exceeds threshold"
            );
        } catch (RiskThresholdExceededException e) {
            return new RiskScoreResult(false, e.getCurrentValue(), 
                    riskModel.calculateTotalExposure(), e.getThresholdValue(), e.getMessage());
        }
    }
    
    public boolean evaluateOrder(String traderId, String strategyId, String instrumentId,
            BigDecimal quantity, BigDecimal price) {
        OrderRequest request = new OrderRequest(
            null, traderId, strategyId, instrumentId, quantity, price, "BUY"
        );
        return calculateRiskScore(request).approved();
    }
    
    private void validateOrderRequest(OrderRequest order) {
        if (order == null || order.traderId() == null || order.instrumentId() == null) {
            throw new IllegalArgumentException("Invalid order request");
        }
    }
    
    private void checkKillSwitchStatus(String strategyId) {
        logger.debug("Verificando status de kill switch para estrategia: {}", strategyId);
    }
    
    private void validateExposureLimits(OrderRequest order, BigDecimal orderNotional) {
        BigDecimal traderExposure = riskModel.calculateExposureByTrader(order.traderId());
        BigDecimal maxTraderExposure = getMaxExposureForTrader(order.traderId());
        
        if (traderExposure.add(orderNotional).compareTo(maxTraderExposure) > 0) {
            throw new RiskThresholdExceededException(
                order.orderId(), order.traderId(), order.strategyId(), order.instrumentId(),
                "TRADER_EXPOSURE", ThresholdType.TRADER_EXPOSURE,
                traderExposure.add(orderNotional), maxTraderExposure,
                "EXPOSURE", Instant.now()
            );
        }
        
        BigDecimal strategyExposure = riskModel.calculateExposureByStrategy(order.strategyId());
        BigDecimal maxStrategyExposure = new BigDecimal("1000000");
        
        if (strategyExposure.add(orderNotional).compareTo(maxStrategyExposure) > 0) {
            throw new RiskThresholdExceededException(
                order.orderId(), order.traderId(), order.strategyId(), order.instrumentId(),
                "STRATEGY_EXPOSURE", ThresholdType.STRATEGY_EXPOSURE,
                strategyExposure.add(orderNotional), maxStrategyExposure,
                "EXPOSURE", Instant.now()
            );
        }
    }
    
    private BigDecimal getMaxExposureForTrader(String traderId) {
        return new BigDecimal("1000000");
    }
    
    private BigDecimal calculateVarWithCircuitBreaker(String instrumentId) {
        return riskModel.getCurrentVar() != null ? riskModel.getCurrentVar() : getFallbackVar();
    }
    
    private CircuitBreaker getOrCreateCircuitBreaker(String instrumentId) {
        return circuitBreakersByStrategy.computeIfAbsent(instrumentId, id -> 
            circuitBreakerRegistry.circuitBreaker(id)
        );
    }
    
    private BigDecimal getFallbackVar() {
        return new BigDecimal("50000");
    }
    
    private BigDecimal calculateOrderRiskContribution(OrderRequest order, BigDecimal currentVar) {
        BigDecimal orderNotional = order.price().multiply(order.quantity());
        BigDecimal baseRisk = orderNotional.multiply(new BigDecimal("0.01"));
        
        if (currentVar != null) {
            return baseRisk.add(currentVar.multiply(new BigDecimal("0.1")));
        }
        return baseRisk;
    }
    
    private BigDecimal getAdjustedThreshold(String instrumentId) {
        Threshold threshold = thresholdsByInstrument.get(instrumentId);
        if (threshold != null) {
            return threshold.getCurrentValue();
        }
        return MAX_EXPOSURE_PER_ORDER;
    }
    
    private BigDecimal estimateCurrentVolatility(String instrumentId) {
        return new BigDecimal("0.20");
    }
    
    private BigDecimal estimateHistoricalVolatility(String instrumentId) {
        return new BigDecimal("0.15");
    }
    
    private void updateRiskModel(OrderRequest order, BigDecimal orderNotional) {
        riskModel.updatePosition(order.instrumentId(), order.quantity(), order.price());
        riskModel.updateTraderExposure(order.traderId(), orderNotional);
        riskModel.updateStrategyExposure(order.strategyId(), orderNotional);
    }
    
    private void recalculateRiskMetrics() {
        synchronized (recalculationLock) {
            try {
                BigDecimal var = riskModel.calculateVar();
                lastRiskRecalculation = Instant.now();
                logger.debug("VaR recalculado: {}", var);
            } catch (Exception e) {
                logger.error("Error al recalcular métricas de riesgo", e);
            }
        }
    }
    
    public void recordMarketDataUpdate(String instrumentId, BigDecimal price, Instant timestamp) {
        riskModel.recordPrice(instrumentId, price, timestamp);
    }
    
    public void recordTrade(String instrumentId, BigDecimal quantity, BigDecimal price,
            Instant timestamp) {
        riskModel.updatePosition(instrumentId, quantity, price);
    }
    
    public void enableCircuitBreakers(boolean enable) {
        this.enableCircuitBreakers = enable;
    }
    
    public RiskModel getRiskModel() {
        return riskModel;
    }
    
    public BigDecimal getCurrentVar() {
        return riskModel.getCurrentVar();
    }
    
    public Map<String, BigDecimal> getDailyPnLByTrader() {
        return dailyPnLByTrader;
    }
    
    public BigDecimal calculateVar() {
        return riskModel.calculateVar();
    }
    
    public BigDecimal calculateTotalExposure() {
        return riskModel.calculateTotalExposure();
    }
    
    public BigDecimal calculateExposureByTrader(String traderId) {
        return riskModel.calculateExposureByTrader(traderId);
    }
    
    public BigDecimal calculateExposureByStrategy(String strategyId) {
        return riskModel.calculateExposureByStrategy(strategyId);
    }
    
    public BigDecimal calculateExposureByInstrument(String instrumentId) {
        return riskModel.calculateExposureByInstrument(instrumentId);
    }
    
    public void validateVarLimit(BigDecimal limit) {
        BigDecimal currentVar = riskModel.calculateVar();
        if (currentVar != null && currentVar.compareTo(limit) > 0) {
            throw new RiskThresholdExceededException(
                null, null, null, null,
                "VAR_LIMIT", ThresholdType.VAR_LIMIT,
                currentVar, limit,
                "VAR", Instant.now()
            );
        }
    }
    
    public void updatePosition(String instrumentId, BigDecimal quantity, BigDecimal price) {
        riskModel.updatePosition(instrumentId, quantity, price);
    }
    
    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}