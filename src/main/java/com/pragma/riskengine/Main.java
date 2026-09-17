package com.pragma.riskengine;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.pragma.riskengine.config.DisruptorConfig;
import com.pragma.riskengine.config.ResilienceConfig;
import com.pragma.riskengine.disruptor.OrderEvent;
import com.pragma.riskengine.disruptor.OrderEventHandler;
import com.pragma.riskengine.disruptor.MarketDataEventHandler;
import com.pragma.riskengine.service.RiskScoringService;
import com.pragma.riskengine.service.KillSwitchService;
import com.pragma.riskengine.adapter.marketdata.MarketDataFeedAdapter;
import com.pragma.riskengine.adapter.exchange.ExchangeAdapter;
import com.pragma.riskengine.audit.AuditLogger;
import com.pragma.riskengine.replay.ReplayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.Properties;
import java.io.InputStream;

/**
 * Punto de entrada del motor de risk scoring en tiempo real.
 * Inicializa el Disruptor, servicios de riesgo, adapters de market data y exchange.
 * El sistema procesa órdenes con latencia sub-milisegundo usando un anillo de buffers lock-free.
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private final Disruptor<OrderEvent> disruptor;
    private final RiskScoringService riskScoringService;
    private final KillSwitchService killSwitchService;
    private final MarketDataFeedAdapter marketDataAdapter;
    private final ExchangeAdapter exchangeAdapter;
    private final AuditLogger auditLogger;
    private final ReplayService replayService;
    private final ScheduledExecutorService scheduler;
    private final Properties config;

    public Main(Properties config) {
        this.config = config;
        this.scheduler = Executors.newScheduledThreadPool(4, r -> {
            Thread t = new Thread(r, "risk-scheduler");
            t.setDaemon(true);
            return t;
        });

        this.auditLogger = new AuditLogger();
        this.riskScoringService = createRiskScoringService();
        this.killSwitchService = createKillSwitchService();
        this.marketDataAdapter = createMarketDataAdapter();
        this.exchangeAdapter = createExchangeAdapter();
        this.replayService = new ReplayService(riskScoringService, auditLogger);
        this.disruptor = createDisruptor();

        logger.info("Risk Engine inicializado con configuración: bufferSize={}, waitStrategy={}",
                config.getProperty("disruptor.buffer.size", "65536"),
                config.getProperty("disruptor.wait.strategy", "BlockingWaitStrategy"));
    }

    private RiskScoringService createRiskScoringService() {
        double varConfidence = Double.parseDouble(config.getProperty("risk.var.confidence", "0.99"));
        int varWindowMinutes = Integer.parseInt(config.getProperty("risk.var.window.minutes", "15"));
        double maxExposurePerTrade = Double.parseDouble(config.getProperty("risk.max.exposure.trade", "100000"));
        double maxExposurePerStrategy = Double.parseDouble(config.getProperty("risk.max.exposure.strategy", "500000"));
        double maxExposurePerInstrument = Double.parseDouble(config.getProperty("risk.max.exposure.instrument", "200000"));
        int maxOrdersPerSecond = Integer.parseInt(config.getProperty("risk.max.orders.per.second", "1000"));

        ResilienceConfig resilienceConfig = ResilienceConfig.fromProperties(config);

        return new RiskScoringService(
                varConfidence,
                varWindowMinutes,
                maxExposurePerTrade,
                maxExposurePerStrategy,
                maxExposurePerInstrument,
                maxOrdersPerSecond,
                resilienceConfig,
                auditLogger
        );
    }

    private KillSwitchService createKillSwitchService() {
        double defaultThreshold = Double.parseDouble(config.getProperty("killswitch.default.threshold", "0.85"));
        int cooldownSeconds = Integer.parseInt(config.getProperty("killswitch.cooldown.seconds", "60"));
        int maxActivations = Integer.parseInt(config.getProperty("killswitch.max.activations", "5"));

        return new KillSwitchService(defaultThreshold, cooldownSeconds, maxActivations, auditLogger);
    }

    private MarketDataFeedAdapter createMarketDataAdapter() {
        String feedUrl = config.getProperty("marketdata.feed.url", "tcp://localhost:5555");
        int bufferSize = Integer.parseInt(config.getProperty("marketdata.buffer.size", "10000"));
        boolean snapshotEnabled = Boolean.parseBoolean(config.getProperty("marketdata.snapshot.enabled", "true"));

        return new MarketDataFeedAdapter(feedUrl, bufferSize, snapshotEnabled);
    }

    private ExchangeAdapter createExchangeAdapter() {
        String exchangeUrl = config.getProperty("exchange.url", "tcp://localhost:6666");
        int timeoutMs = Integer.parseInt(config.getProperty("exchange.timeout.ms", "5000"));
        int maxRetries = Integer.parseInt(config.getProperty("exchange.max.retries", "3"));

        return new ExchangeAdapter(exchangeUrl, timeoutMs, maxRetries);
    }

    private Disruptor<OrderEvent> createDisruptor() {
        int bufferSize = Integer.parseInt(config.getProperty("disruptor.buffer.size", "65536"));
        String waitStrategyName = config.getProperty("disruptor.wait.strategy", "BlockingWaitStrategy");

        DisruptorConfig disruptorConfig = new DisruptorConfig(bufferSize, waitStrategyName);
        return disruptorConfig.createDisruptor(OrderEvent::new, ProducerType.MULTI);
    }

    /**
     * Inicia el motor de risk scoring y todos sus componentes.
     * El Disruptor queda escuchando para recibir eventos de órdenes.
     */
    public void start() {
        logger.info("Iniciando Risk Engine...");

        disruptor.handleEventsWith(
                new OrderEventHandler(riskScoringService, killSwitchService, exchangeAdapter, auditLogger),
                new MarketDataEventHandler(marketDataAdapter)
        );

        disruptor.setDefaultExceptionHandler(new com.lmax.disruptor.ExceptionHandler<OrderEvent>() {
            @Override
            public void handleEventException(Throwable ex, long sequence, OrderEvent event) {
                logger.error("Excepción procesando evento en secuencia {}", sequence, ex);
                auditLogger.logError("DISRUPTOR_EVENT_ERROR", sequence, ex.getMessage());
            }

            @Override
            public void handleOnStartException(Throwable ex) {
                logger.error("Error al iniciar Disruptor", ex);
                auditLogger.logError("DISRUPTOR_START_ERROR", -1, ex.getMessage());
            }

            @Override
            public void handleOnShutdownException(Throwable ex) {
                logger.error("Error al cerrar Disruptor", ex);
                auditLogger.logError("DISRUPTOR_SHUTDOWN_ERROR", -1, ex.getMessage());
            }
        });

        disruptor.start();

        marketDataAdapter.connect();
        exchangeAdapter.connect();

        startScheduledTasks();

        logger.info("Risk Engine iniciado correctamente. Listo para procesar órdenes.");
    }

    private void startScheduledTasks() {
        int recalcIntervalSeconds = Integer.parseInt(config.getProperty("risk.recalc.interval.seconds", "60"));

        scheduler.scheduleAtFixedRate(
                () -> {
                    try {
                        riskScoringService.recalculateVolatility();
                        killSwitchService.recalculateThresholds(riskScoringService.getCurrentVolatility());
                        logger.debug("Volatilidad y thresholds recalculados");
                    } catch (Exception e) {
                        logger.error("Error en tarea programada de recalculo", e);
                        auditLogger.logError("SCHEDULED_TASK_ERROR", -1, e.getMessage());
                    }
                },
                recalcIntervalSeconds,
                recalcIntervalSeconds,
                TimeUnit.SECONDS
        );

        int healthCheckIntervalSeconds = Integer.parseInt(config.getProperty("health.check.interval.seconds", "30"));
        scheduler.scheduleAtFixedRate(
                this::performHealthCheck,
                healthCheckIntervalSeconds,
                healthCheckIntervalSeconds,
                TimeUnit.SECONDS
        );
    }

    private void performHealthCheck() {
        RingBuffer<OrderEvent> ringBuffer = disruptor.getRingBuffer();
        long cursor = ringBuffer.getCursor();
        int bufferSize = ringBuffer.getBufferSize();
        double utilization = (double) (cursor - ringBuffer.getMinimumGatingSequence()) / bufferSize;

        if (utilization > 0.8) {
            logger.warn("Buffer del Disruptor con utilización alta: {:.2%}", utilization);
            auditLogger.logWarning("HIGH_BUFFER_UTILIZATION", utilization);
        }

        logger.debug("Health check: cursor={}, bufferSize={}, utilization={:.2%}",
                cursor, bufferSize, utilization);
    }

    /**
     * Envía una orden al Disruptor para procesamiento de risk scoring.
     * @param orderId Identificador único de la orden
     * @param traderId Identificador del trader
     * @param strategyId Identificador de la estrategia
     * @param instrument Código del instrumento
     * @param side Lado de la orden (BUY/SELL)
     * @param quantity Cantidad
     * @param price Precio
     */
    public void submitOrder(String orderId, String traderId, String strategyId,
                           String instrument, String side, long quantity, double price) {
        if (killSwitchService.isKillSwitchActive()) {
            logger.warn("Orden {} rechazada: Kill Switch activo", orderId);
            auditLogger.logOrderRejected(orderId, "KILL_SWITCH_ACTIVE");
            throw new com.pragma.riskengine.exception.KillSwitchActivatedException(
                    "Sistema en modo de protección - orders bloqueadas");
        }

        RingBuffer<OrderEvent> ringBuffer = disruptor.getRingBuffer();
        long sequence = ringBuffer.next();

        try {
            OrderEvent event = ringBuffer.get(sequence);
            event.setOrderId(orderId);
            event.setTraderId(traderId);
            event.setStrategyId(strategyId);
            event.setInstrument(instrument);
            event.setSide(side);
            event.setQuantity(quantity);
            event.setPrice(price);
            event.setTimestamp(System.nanoTime());
        } finally {
            ringBuffer.publish(sequence);
        }

        logger.debug("Orden {} publicada en Disruptor para procesamiento", orderId);
    }

    /**
     * Detiene el motor de forma graceful.
     */
    public void shutdown() {
        logger.info("Deteniendo Risk Engine...");

        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(10, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }

        marketDataAdapter.disconnect();
        exchangeAdapter.disconnect();

        disruptor.shutdown();

        logger.info("Risk Engine detenido correctamente");
    }

    public RiskScoringService getRiskScoringService() {
        return riskScoringService;
    }

    public KillSwitchService getKillSwitchService() {
        return killSwitchService;
    }

    public boolean isRunning() {
        return disruptor.isStarted();
    }

    public static void main(String[] args) {
        Properties config = loadConfiguration();

        Main main = new Main(config);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Signal de shutdown recibido");
            main.shutdown();
        }));

        main.start();

        logger.info("Risk Engine corriendo. Presione Ctrl+C para detener.");
    }

    private static Properties loadConfiguration() {
        Properties config = new Properties();

        try (InputStream input = Main.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (input == null) {
                logger.warn("No se encontró application.properties, usando configuración por defecto");
                setDefaultConfiguration(config);
                return config;
            }
            config.load(input);
            logger.info("Configuración cargada correctamente");
        } catch (Exception e) {
            logger.error("Error cargando configuración, usando valores por defecto", e);
            setDefaultConfiguration(config);
        }

        return config;
    }

    private static void setDefaultConfiguration(Properties config) {
        config.setProperty("disruptor.buffer.size", "65536");
        config.setProperty("disruptor.wait.strategy", "BlockingWaitStrategy");
        config.setProperty("risk.var.confidence", "0.99");
        config.setProperty("risk.var.window.minutes", "15");
        config.setProperty("risk.max.exposure.trade", "100000");
        config.setProperty("risk.max.exposure.strategy", "500000");
        config.setProperty("risk.max.exposure.instrument", "200000");
        config.setProperty("risk.max.orders.per.second", "1000");
        config.setProperty("risk.recalc.interval.seconds", "60");
        config.setProperty("killswitch.default.threshold", "0.85");
        config.setProperty("killswitch.cooldown.seconds", "60");
        config.setProperty("killswitch.max.activations", "5");
        config.setProperty("marketdata.feed.url", "tcp://localhost:5555");
        config.setProperty("marketdata.buffer.size", "10000");
        config.setProperty("marketdata.snapshot.enabled", "true");
        config.setProperty("exchange.url", "tcp://localhost:6666");
        config.setProperty("exchange.timeout.ms", "5000");
        config.setProperty("exchange.max.retries", "3");
        config.setProperty("health.check.interval.seconds", "30");
        config.setProperty("circuitbreaker.failure.rate.threshold", "50");
        config.setProperty("circuitbreaker.wait.duration.open", "30");
        config.setProperty("circuitbreaker.sliding.window.size", "100");
    }
}