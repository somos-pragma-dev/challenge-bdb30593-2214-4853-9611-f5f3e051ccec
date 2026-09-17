package com.pragma.riskengine.disruptor;

import com.pragma.riskengine.model.RiskModel;
import com.pragma.riskengine.service.RiskScoringService;
import com.pragma.riskengine.service.KillSwitchService;
import com.pragma.riskengine.audit.AuditLogger;
import com.pragma.riskengine.exception.RiskThresholdExceededException;
import com.pragma.riskengine.exception.KillSwitchActivatedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderEventHandlerTest {

    @Mock
    private RiskScoringService riskScoringService;

    @Mock
    private KillSwitchService killSwitchService;

    @Mock
    private AuditLogger auditLogger;

    private OrderEventHandler handler;
    private RiskModel riskModel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        riskModel = new RiskModel(new BigDecimal("0.99"), 250);
        handler = new OrderEventHandler(riskScoringService, killSwitchService, auditLogger, riskModel);
    }

    @Test
    @DisplayName("Debe aprobar orden cuando risk scoring y kill switch pasan")
    void shouldApproveOrderWhenRiskAndKillSwitchPass() throws Exception {
        String orderId = UUID.randomUUID().toString();
        String traderId = "TRADER-001";
        String strategyId = "STRAT-MOMENTUM";
        String instrumentId = "AAPL";
        BigDecimal quantity = new BigDecimal("100");
        BigDecimal price = new BigDecimal("150.00");

        OrderEvent event = new OrderEvent(orderId, traderId, strategyId,
                instrumentId, quantity, price, OrderEvent.OrderSide.BUY);

        when(riskScoringService.evaluateOrder(anyString(), anyString(), anyString(), 
                any(BigDecimal.class), any(BigDecimal.class))).thenReturn(true);
        when(killSwitchService.isKillSwitchActive()).thenReturn(false);

        handler.onEvent(event, 0, false);

        assertEquals(OrderEvent.OrderStatus.APPROVED, event.status());
        verify(auditLogger).logRiskDecision(eq(orderId), eq("APPROVED"), anyString());
    }

    @Test
    @DisplayName("Debe rechazar orden cuando risk scoring falla")
    void shouldRejectOrderWhenRiskScoringFails() throws Exception {
        String orderId = UUID.randomUUID().toString();
        String traderId = "TRADER-002";
        String strategyId = "STRAT-ARBITRAGE";
        String instrumentId = "TSLA";
        BigDecimal quantity = new BigDecimal("500");
        BigDecimal price = new BigDecimal("200.00");

        OrderEvent event = new OrderEvent(orderId, traderId, strategyId,
                instrumentId, quantity, price, OrderEvent.OrderSide.SELL);

        when(riskScoringService.evaluateOrder(anyString(), anyString(), anyString(),
                any(BigDecimal.class), any(BigDecimal.class))).thenReturn(false);
        when(killSwitchService.isKillSwitchActive()).thenReturn(false);

        handler.onEvent(event, 0, false);

        assertEquals(OrderEvent.OrderStatus.REJECTED, event.status());
        assertNotNull(event.rejectionReason());
        verify(auditLogger).logRiskDecision(eq(orderId), eq("REJECTED"), anyString());
    }

    @Test
    @DisplayName("Debe rechazar orden cuando kill switch está activo")
    void shouldRejectOrderWhenKillSwitchActive() throws Exception {
        String orderId = UUID.randomUUID().toString();
        OrderEvent event = new OrderEvent(orderId, "TRADER-003", "STRAT-PAIR",
                "MSFT", new BigDecimal("200"), new BigDecimal("350.00"),
                OrderEvent.OrderSide.BUY);

        when(killSwitchService.isKillSwitchActive()).thenReturn(true);

        handler.onEvent(event, 0, false);

        assertEquals(OrderEvent.OrderStatus.REJECTED, event.status());
        assertTrue(event.rejectionReason().contains("Kill Switch"));
        verify(riskScoringService, never()).evaluateOrder(any(), any(), any(), any(), any());
    }

    @Test
    @DisplayName("Debe actualizar posición después de orden aprobada")
    void shouldUpdatePositionAfterApprovedOrder() throws Exception {
        String instrumentId = "GOOGL";
        BigDecimal initialQuantity = new BigDecimal("0");
        riskModel.updatePosition(instrumentId, initialQuantity, new BigDecimal("100.00"));

        String orderId = UUID.randomUUID().toString();
        OrderEvent event = new OrderEvent(orderId, "TRADER-004", "STRAT-TREND",
                instrumentId, new BigDecimal("50"), new BigDecimal("100.00"),
                OrderEvent.OrderSide.BUY);

        when(riskScoringService.evaluateOrder(anyString(), anyString(), anyString(),
                any(BigDecimal.class), any(BigDecimal.class))).thenReturn(true);
        when(killSwitchService.isKillSwitchActive()).thenReturn(false);

        handler.onEvent(event, 0, false);

        var positions = riskModel.getAllPositions();
        assertTrue(positions.containsKey(instrumentId));
    }

    @Test
    @DisplayName("Debe calcular VaR correctamente después de múltiples órdenes")
    void shouldCalculateVarAfterMultipleOrders() throws Exception {
        String instrumentId = "AMZN";
        riskModel.recordPrice(instrumentId, new BigDecimal("100.00"), Instant.now());
        riskModel.recordPrice(inInstrumentId, new BigDecimal("102.00"), Instant.now().minusSeconds(60));
        riskModel.recordPrice(instrumentId, new BigDecimal("98.00"), Instant.now().minusSeconds(120));

        riskModel.updatePosition(instrumentId, new BigDecimal("100"), new BigDecimal("100.00"));

        when(riskScoringService.evaluateOrder(anyString(), anyString(), anyString(),
                any(BigDecimal.class), any(BigDecimal.class))).thenReturn(true);
        when(killSwitchService.isKillSwitchActive()).thenReturn(false);

        for (int i = 0; i < 3; i++) {
            OrderEvent event = new OrderEvent(UUID.randomUUID().toString(),
                    "TRADER-" + i, "STRAT-" + i, instrumentId,
                    new BigDecimal("10"), new BigDecimal("100.00"),
                    OrderEvent.OrderSide.BUY);
            handler.onEvent(event, 0, false);
        }

        BigDecimal var = riskModel.calculateVar();
        assertNotNull(var);
        assertTrue(var.compareTo(BigDecimal.ZERO) >= 0);
    }

    @Test
    @DisplayName("Debe manejar excepción de risk scoring gracefully")
    void shouldHandleRiskScoringException() throws Exception {
        String orderId = UUID.randomUUID().toString();
        OrderEvent event = new OrderEvent(orderId, "TRADER-005", "STRAT-QUANT",
                "NVDA", new BigDecimal("75"), new BigDecimal("500.00"),
                OrderEvent.OrderSide.BUY);

        when(riskScoringService.evaluateOrder(anyString(), anyString(), anyString(),
                any(BigDecimal.class), any(BigDecimal.class)))
                .thenThrow(new RuntimeException("Risk service unavailable"));
        when(killSwitchService.isKillSwitchActive()).thenReturn(false);

        handler.onEvent(event, 0, false);

        assertEquals(OrderEvent.OrderStatus.REJECTED, event.status());
        verify(auditLogger).logError(eq(orderId), anyString(), any(Exception.class));
    }
}