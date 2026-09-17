package com.pragma.riskengine.service;

import com.pragma.riskengine.model.RiskModel;
import com.pragma.riskengine.model.Threshold;
import com.pragma.riskengine.model.Threshold.ThresholdType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class KillSwitchServiceTest {

    private KillSwitchService service;
    private RiskModel riskModel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        riskModel = new RiskModel(new BigDecimal("0.99"), 100);
        service = new KillSwitchService(riskModel);
    }

    @Test
    @DisplayName("Debe estar inactivo inicialmente")
    void shouldBeInactiveInitially() {
        assertFalse(service.isKillSwitchActive());
    }

    @Test
    @DisplayName("Debe activarse cuando exposición excede umbral crítico")
    void shouldActivateWhenExposureExceedsCriticalThreshold() {
        service.setCriticalExposureThreshold(new BigDecimal("100000"));

        for (int i = 0; i < 5; i++) {
            riskModel.updateTraderExposure("TRADER-" + i, new BigDecimal("25000"));
        }

        service.checkAndUpdateKillSwitch();

        assertTrue(service.isKillSwitchActive());
    }

    @Test
    @DisplayName("Debe permanecer inactivo cuando exposición está dentro de límites")
    void shouldRemainInactiveWhenExposureWithinLimits() {
        service.setCriticalExposureThreshold(new BigDecimal("1000000"));

        riskModel.updateTraderExposure("TRADER-LOW-1", new BigDecimal("50000"));
        riskModel.updateTraderExposure("TRADER-LOW-2", new BigDecimal("75000"));

        service.checkAndUpdateKillSwitch();

        assertFalse(service.isKillSwitchActive());
    }

    @Test
    @DisplayName("Debe detectar anomalías en patrones de trading")
    void shouldDetectAnomaliesInTradingPatterns() {
        String traderId = "TRADER-ANOMALY";
        AtomicInteger rejectionCount = new AtomicInteger(0);

        for (int i = 0; i < 15; i++) {
            boolean result = service.checkAnomalyPattern(traderId,
                    new BigDecimal("100000"),
                    new BigDecimal((i + 1) * 10000));
            if (!result) {
                rejectionCount.incrementAndGet();
            }
        }

        assertTrue(rejectionCount.get() > 0);
    }

    @Test
    @DisplayName("Debe detectar órdenes rápidas anómalas")
    void shouldDetectRapidAnomalousOrders() {
        String traderId = "TRADER-HFT";

        for (int i = 0; i < 10; i++) {
            service.recordOrderSubmission(traderId, new BigDecimal("10000"));
        }

        boolean hasAnomaly = service.hasRapidOrderAnomaly(traderId, 5, Duration.ofSeconds(1));

        assertTrue(hasAnomaly);
    }

    @Test
    @DisplayName("Debe detectar concentración excesiva por instrumento")
    void shouldDetectExcessiveConcentrationByInstrument() {
        String instrumentId = "CONCENTRATED-STOCK";
        String traderId = "TRADER-CONCENTRATED";

        for (int i = 0; i < 20; i++) {
            riskModel.updatePosition(instrumentId, new BigDecimal("1000"),
                    new BigDecimal("100.00"));
        }

        BigDecimal exposure = riskModel.calculateExposureByInstrument(instrumentId);
        boolean hasConcentration = service.hasExcessiveConcentration(instrumentId,
                exposure, new BigDecimal("100000"));

        assertTrue(hasConcentration);
    }

    @Test
    @DisplayName("Debe ajustar thresholds basado en volatilidad")
    void shouldAdjustThresholdsBasedOnVolatility() {
        Threshold threshold = new Threshold("VOLATILITY-ADJUST",
                ThresholdType.TOTAL_EXPOSURE, new BigDecimal("500000"));

        threshold.adjustForVolatility(new BigDecimal("0.50"), new BigDecimal("0.20"));

        BigDecimal adjustedValue = threshold.getCurrentValue();
        assertTrue(adjustedValue.compareTo(threshold.getBaseValue()) < 0);
    }

    @Test
    @DisplayName("Debe registrar eventos de activación")
    void shouldLogActivationEvents() {
        String activationId = UUID.randomUUID().toString();

        service.activateKillSwitch(activationId, "HIGH_EXPOSURE",
                new BigDecimal("800000"));

        assertTrue(service.isKillSwitchActive());
        assertNotNull(service.getLastActivationReason());
    }

    @Test
    @DisplayName("Debe permitir reseteo manual del kill switch")
    void shouldAllowManualReset() {
        service.activateKillSwitch("TEST-1", "TEST-REASON", new BigDecimal("999999"));
        assertTrue(service.isKillSwitchActive());

        boolean resetResult = service.manualReset("ADMIN");

        assertTrue(resetResult);
        assertFalse(service.isKillSwitchActive());
    }

    @Test
    @DisplayName("Debe detectar anomalías en algoritmos de trading")
    void shouldDetectAlgorithmAnomalies() {
        String strategyId = "STRAT-ALGO-1";
        BigDecimal[] prices = {
            new BigDecimal("100.00"),
            new BigDecimal("105.00"),
            new BigDecimal("110.00"),
            new BigDecimal("115.00"),
            new BigDecimal("120.00"),
            new BigDecimal("125.00"),
            new BigDecimal("130.00")
        };

        for (BigDecimal price : prices) {
            riskModel.recordPrice(strategyId, price, Instant.now());
        }

        boolean hasAnomaly = service.detectAlgorithmAnomaly(strategyId, prices);

        assertFalse(hasAnomaly);
    }

    @Test
    @DisplayName("Debe detectar manipulación de precios")
    void shouldDetectPriceManipulation() {
        String instrumentId = "MANIP-STOCK";
        BigDecimal[] prices = {
            new BigDecimal("100.00"),
            new BigDecimal("50.00"),
            new BigDecimal("100.00"),
            new BigDecimal("50.00"),
            new BigDecimal("100.00")
        };

        boolean detected = service.detectPriceManipulation(instrumentId, prices);

        assertTrue(detected);
    }

    @Test
    @DisplayName("Debe verificar salud del sistema antes de aprobar operaciones")
    void shouldVerifySystemHealthBeforeApproving() {
        service.setHealthyState(true);

        boolean canApprove = service.preCheckApproval();

        assertTrue(canApprove);
    }

    @Test
    @DisplayName("Debe bloquear cuando el sistema no está saludable")
    void shouldBlockWhenSystemUnhealthy() {
        service.setHealthyState(false);

        boolean canApprove = service.preCheckApproval();

        assertFalse(canApprove);
    }
}