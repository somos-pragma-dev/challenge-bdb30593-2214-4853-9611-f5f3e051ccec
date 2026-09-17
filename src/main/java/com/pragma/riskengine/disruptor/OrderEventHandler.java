package com.pragma.riskengine.disruptor;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import com.pragma.riskengine.service.RiskScoringService;
import com.pragma.riskengine.service.KillSwitchService;
import com.pragma.riskengine.exception.RiskThresholdExceededException;
import com.pragma.riskengine.exception.KillSwitchActivatedException;
import com.pragma.riskengine.audit.AuditLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Instant;

public class OrderEventHandler implements EventHandler<OrderEvent>, WorkHandler<OrderEvent> {

    private static final Logger logger = LoggerFactory.getLogger(OrderEventHandler.class);
    private static final BigDecimal CRITICAL_THRESHOLD = new BigDecimal("0.95");
    private static final BigDecimal HIGH_THRESHOLD = new BigDecimal("0.75");
    private static final BigDecimal MEDIUM_THRESHOLD = new BigDecimal("0.50");

    private final RiskScoringService riskScoringService;
    private final KillSwitchService killSwitchService;
    private final AuditLogger auditLogger;
    private final String handlerId;

    public OrderEventHandler(RiskScoringService riskScoringService,
                             KillSwitchService killSwitchService,
                             AuditLogger auditLogger) {
        this.riskScoringService = riskScoringService;
        this.killSwitchService = killSwitchService;
        this.auditLogger = auditLogger;
        this.handlerId = "handler-" + System.nanoTime();
    }

    @Override
    public void onEvent(OrderEvent event, long sequence, boolean endOfBatch) throws Exception {
        processOrderEvent(event);
    }

    @Override
    public void onEvent(OrderEvent event) throws Exception {
        processOrderEvent(event);
    }

    private void processOrderEvent(OrderEvent event) {
        long startTime = System.nanoTime();

        try {
            if (event.getStatus() != OrderEvent.ProcessingStatus.PENDING) {
                logger.debug("Order {} already processed, skipping", event.getOrderId());
                return;
            }

            if (killSwitchService.isKillSwitchActive()) {
                event.reject("Kill switch is active - system halted");
                auditLogger.logRiskDecision(event.getOrderId(), event.getTraderId(),
                    OrderEvent.ProcessingStatus.REJECTED, "KILL_SWITCH_ACTIVE");
                return;
            }

            BigDecimal traderExposure = riskScoringService.calculateExposureByTrader(event.getTraderId());
            BigDecimal strategyExposure = riskScoringService.calculateExposureByStrategy(event.getStrategyId());
            BigDecimal instrumentExposure = riskScoringService.calculateExposureByInstrument(event.getInstrumentId());
            BigDecimal orderNotional = event.calculateNotional();

            BigDecimal projectedTraderExposure = traderExposure.add(orderNotional);
            BigDecimal projectedStrategyExposure = strategyExposure.add(orderNotional);
            BigDecimal projectedInstrumentExposure = instrumentExposure.add(orderNotional);

            riskScoringService.validateThresholds(
                event.getTraderId(),
                event.getStrategyId(),
                event.getInstrumentId(),
                projectedTraderExposure,
                projectedStrategyExposure,
                projectedInstrumentExposure
            );

            BigDecimal riskScore = riskScoringService.calculateRiskScore(
                event.getTraderId(),
                event.getStrategyId(),
                event.getInstrumentId(),
                orderNotional,
                event.getSide()
            );

            OrderEvent.RiskScore mappedScore = mapToRiskScore(riskScore);
            event.applyRiskScoring(riskScore, mappedScore);

            if (mappedScore == OrderEvent.RiskScore.CRITICAL) {
                event.reject("Risk score CRITICAL - order rejected");
                auditLogger.logRiskDecision(event.getOrderId(), event.getTraderId(),
                    OrderEvent.ProcessingStatus.REJECTED, "CRITICAL_RISK_SCORE");
                logger.warn("Order {} rejected due to CRITICAL risk score: {}", 
                    event.getOrderId(), riskScore);
            } else if (mappedScore == OrderEvent.RiskScore.HIGH) {
                event.approve();
                auditLogger.logRiskDecision(event.getOrderId(), event.getTraderId(),
                    OrderEvent.ProcessingStatus.APPROVED, "HIGH_RISK_SCORE");
                logger.info("Order {} approved with HIGH risk score: {}", 
                    event.getOrderId(), riskScore);
            } else {
                event.approve();
                auditLogger.logRiskDecision(event.getOrderId(), event.getTraderId(),
                    OrderEvent.ProcessingStatus.APPROVED, "APPROVED");
                logger.debug("Order {} approved with risk score: {}", 
                    event.getOrderId(), riskScore);
            }

            long processingTime = System.nanoTime() - startTime;
            if (processingTime > 500_000) {
                logger.warn("Order {} processing exceeded 500us: {}ns", 
                    event.getOrderId(), processingTime);
            }

        } catch (RiskThresholdExceededException e) {
            event.reject("Threshold exceeded: " + e.getMessage());
            auditLogger.logRiskDecision(event.getOrderId(), event.getTraderId(),
                OrderEvent.ProcessingStatus.REJECTED, "THRESHOLD_EXCEEDED");
            logger.error("Order {} rejected - threshold exceeded: {}", 
                event.getOrderId(), e.getMessage());
        } catch (KillSwitchActivatedException e) {
            event.reject("Kill switch activated during processing");
            auditLogger.logRiskDecision(event.getOrderId(), event.getTraderId(),
                OrderEvent.ProcessingStatus.REJECTED, "KILL_SWITCH");
            logger.error("Order {} rejected - kill switch activated", event.getOrderId());
        } catch (Exception e) {
            event.setStatus(OrderEvent.ProcessingStatus.ERROR);
            auditLogger.logRiskDecision(event.getOrderId(), event.getTraderId(),
                OrderEvent.ProcessingStatus.ERROR, "PROCESSING_ERROR");
            logger.error("Unexpected error processing order {}: {}", 
                event.getOrderId(), e.getMessage(), e);
        }
    }

    private OrderEvent.RiskScore mapToRiskScore(BigDecimal riskScore) {
        if (riskScore.compareTo(CRITICAL_THRESHOLD) >= 0) {
            return OrderEvent.RiskScore.CRITICAL;
        } else if (riskScore.compareTo(HIGH_THRESHOLD) >= 0) {
            return OrderEvent.RiskScore.HIGH;
        } else if (riskScore.compareTo(MEDIUM_THRESHOLD) >= 0) {
            return OrderEvent.RiskScore.MEDIUM;
        } else {
            return OrderEvent.RiskScore.LOW;
        }
    }

    public String getHandlerId() {
        return handlerId;
    }
}