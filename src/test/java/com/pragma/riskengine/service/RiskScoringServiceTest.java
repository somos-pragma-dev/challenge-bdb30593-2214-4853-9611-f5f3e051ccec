package com.pragma.riskengine.service;

import com.pragma.riskengine.model.RiskModel;
import com.pragma.riskengine.model.Threshold;
import com.pragma.riskengine.model.Threshold.ThresholdType;
import com.pragma.riskengine.exception.RiskThresholdExceededException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RiskScoringServiceTest {

    private RiskScoringService service;
    private RiskModel riskModel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        riskModel = new RiskModel(new BigDecimal("0.95"), 100);
        service = new RiskScoringService(riskModel);
    }

    @Test
    @DisplayName("Debe aprobar orden cuando está dentro de límites")
    void shouldApproveOrderWithinLimits() {
        String traderId = "TRADER-001";
        String strategyId = "STRAT-MOMENTUM";
        String instrumentId = "AAPL";
        BigDecimal quantity = new BigDecimal("50");
        BigDecimal price = new BigDecimal("150.00");

        riskModel.updateTraderExposure(traderId, BigDecimal.ZERO);
        riskModel.updateStrategyExposure(strategyId, BigDecimal.ZERO);

        boolean result = service.evaluateOrder(traderId, strategyId, instrumentId, quantity, price);

        assertTrue(result);
    }

    @Test
    @DisplayName("Debe rechazar orden que excede límite por trader")
    void shouldRejectOrderExceedingTraderLimit() {
        String traderId = "TRADER-002";
        BigDecimal existingExposure = new BigDecimal("900000");
        riskModel.updateTraderExposure(traderId, existingExposure);

        BigDecimal orderNotional = new BigDecimal("150000");
        boolean result = service.evaluateOrder(traderId, "STRAT-1", "AAPL",
                new BigDecimal("1000"), new BigDecimal("150.00"));

        assertFalse(result);
    }

    @Test
    @DisplayName("Debe rechazar orden que excede límite por estrategia")
    void shouldRejectOrderExceedingStrategyLimit() {
        String strategyId = "STRAT-HIGH-RISK";
        BigDecimal existingExposure = new BigDecimal("450000");
        riskModel.updateStrategyExposure(strategyId, existingExposure);

        boolean result = service.evaluateOrder("TRADER-003", strategyId, "TSLA",
                new BigDecimal("1000"), new BigDecimal("200.00"));

        assertFalse(result);
    }

    @Test
    @DisplayName("Debe calcular VaR correctamente con posiciones")
    void shouldCalculateVarWithPositions() {
        riskModel.updatePosition("AAPL", new BigDecimal("100"), new BigDecimal("150.00"));
        riskModel.updatePosition("GOOGL", new BigDecimal("50"), new BigDecimal("2800.00"));

        riskModel.recordPrice("AAPL", new BigDecimal("150.00"), Instant.now());
        riskModel.recordPrice("AAPL", new BigDecimal("145.00"), Instant.now().minusSeconds(300));
        riskModel.recordPrice("GOOGL", new BigDecimal("2800.00"), Instant.now());
        riskModel.recordPrice("GOOGL", new BigDecimal("2750.00"), Instant.now().minusSeconds(300));

        BigDecimal var = service.calculateVar();

        assertNotNull(var);
        assertTrue(var.compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Debe calcular exposición total correctamente")
    void shouldCalculateTotalExposure() {
        riskModel.updateTraderExposure("TRADER-001", new BigDecimal("100000"));
        riskModel.updateTraderExposure("TRADER-002", new BigDecimal("200000"));
        riskModel.updateTraderExposure("TRADER-003", new BigDecimal("150000"));

        BigDecimal totalExposure = service.calculateTotalExposure();

        assertEquals(new BigDecimal("450000"), totalExposure.setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    @DisplayName("Debe calcular exposición por trader")
    void shouldCalculateExposureByTrader() {
        String traderId = "TRADER-004";
        riskModel.updateTraderExposure(traderId, new BigDecimal("500000"));

        BigDecimal exposure = service.calculateExposureByTrader(traderId);

        assertEquals(new BigDecimal("500000"), exposure.setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    @DisplayName("Debe calcular exposición por estrategia")
    void shouldCalculateExposureByStrategy() {
        String strategyId = "STRAT-ARBITRAGE";
        riskModel.updateStrategyExposure(strategyId, new BigDecimal("750000"));

        BigDecimal exposure = service.calculateExposureByStrategy(strategyId);

        assertEquals(new BigDecimal("750000"), exposure.setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    @DisplayName("Debe calcular exposición por instrumento")
    void shouldCalculateExposureByInstrument() {
        String instrumentId = "MSFT";
        riskModel.updatePosition(instrumentId, new BigDecimal("200"), new BigDecimal("350.00"));

        BigDecimal exposure = service.calculateExposureByInstrument(instrumentId);

        assertNotNull(exposure);
        assertTrue(exposure.compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Debe validar límites dinámicos basados en volatilidad")
    void shouldValidateDynamicVolatilityBasedLimits() {
        String traderId = "TRADER-005";
        riskModel.updateTraderExposure(traderId, new BigDecimal("300000"));

        Threshold threshold = new Threshold("TEST-THRESHOLD", ThresholdType.TRADER_EXPOSURE,
                new BigDecimal("500000"));
        threshold.adjustForVolatility(new BigDecimal("0.30"), new BigDecimal("0.15"));

        BigDecimal currentThreshold = threshold.getCurrentValue();

        assertNotNull(currentThreshold);
        assertTrue(currentThreshold.compareTo(threshold.getBaseValue()) > 0);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando VaR excede límite")
    void shouldThrowExceptionWhenVarExceedsLimit() {
        riskModel.updatePosition("RISKY-1", new BigDecimal("10000"), new BigDecimal("100.00"));

        for (int i = 0; i < 50; i++) {
            riskModel.recordPrice("RISKY-1", 
                    new BigDecimal(100 + (i * 2)).setScale(2, RoundingMode.HALF_UP),
                    Instant.now().minusSeconds(i * 60));
        }

        assertThrows(RiskThresholdExceededException.class, () -> {
            service.validateVarLimit(new BigDecimal("1000"));
        });
    }

    @Test
    @DisplayName("Debe actualizar modelo de riesgo correctamente")
    void shouldUpdateRiskModelCorrectly() {
        String instrumentId = "NVDA";
        BigDecimal quantity = new BigDecimal("100");
        BigDecimal price = new BigDecimal("500.00");

        service.updatePosition(instrumentId, quantity, price);

        var positions = riskModel.getAllPositions();
        assertTrue(positions.containsKey(instrumentId));
        assertEquals(quantity, positions.get(instrumentId).quantity());
    }
}