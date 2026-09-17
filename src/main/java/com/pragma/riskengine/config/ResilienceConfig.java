package com.pragma.riskengine.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class ResilienceConfig {
    private static final Logger logger = LoggerFactory.getLogger(ResilienceConfig.class);

    private static final int DEFAULT_FAILURE_THRESHOLD = 5;
    private static final int DEFAULT_SUCCESS_THRESHOLD = 3;
    private static final long DEFAULT_RESET_TIMEOUT_MS = 30000;
    private static final int DEFAULT_MAX_RETRY_ATTEMPTS = 3;
    private static final long DEFAULT_RETRY_WAIT_MS = 1000;

    private final CircuitBreakerRegistry circuitBreakerRegistry;
    private final RetryRegistry retryRegistry;
    private final int failureThreshold;
    private final int successThreshold;
    private final long resetTimeoutMs;
    private final int maxRetryAttempts;
    private final long retryWaitMs;

    public ResilienceConfig() {
        this(DEFAULT_FAILURE_THRESHOLD, DEFAULT_SUCCESS_THRESHOLD,
                DEFAULT_RESET_TIMEOUT_MS, DEFAULT_MAX_RETRY_ATTEMPTS, DEFAULT_RETRY_WAIT_MS);
    }

    public ResilienceConfig(int failureThreshold, int successThreshold,
            long resetTimeoutMs, int maxRetryAttempts, long retryWaitMs) {
        this.failureThreshold = failureThreshold;
        this.successThreshold = successThreshold;
        this.resetTimeoutMs = resetTimeoutMs;
        this.maxRetryAttempts = maxRetryAttempts;
        this.retryWaitMs = retryWaitMs;

        CircuitBreakerConfig cbConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(failureThreshold)
                .waitDurationInOpenState(Duration.ofMillis(resetTimeoutMs))
                .permittedNumberOfCallsInHalfOpenState(successThreshold)
                .slidingWindowSize(100)
                .minimumNumberOfCalls(10)
                .build();

        RetryConfig retryConfig = RetryConfig.custom()
                .maxAttempts(maxRetryAttempts)
                .waitDuration(Duration.ofMillis(retryWaitMs))
                .retryExceptions(Exception.class)
                .build();

        this.circuitBreakerRegistry = CircuitBreakerRegistry.of(cbConfig);
        this.retryRegistry = RetryRegistry.of(retryConfig);

        logger.info("ResilienceConfig initialized: failureThreshold={}, successThreshold={}, " +
                        "resetTimeoutMs={}, maxRetryAttempts={}, retryWaitMs={}",
                failureThreshold, successThreshold, resetTimeoutMs, maxRetryAttempts, retryWaitMs);
    }

    public <T> T executeWithCircuitBreaker(String name, Supplier<T> operation) {
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(name);

        return CircuitBreaker.decorateSupplier(circuitBreaker, operation).get();
    }

    public <T> T executeWithRetry(Supplier<T> operation) {
        Retry retry = retryRegistry.retry("default-retry");

        return Retry.decorateSupplier(retry, operation).get();
    }

    public <T> T executeWithRetryAndCircuitBreaker(String cbName, Supplier<T> operation) {
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(cbName);
        Retry retry = retryRegistry.retry(cbName + "-retry");

        Supplier<T> decorated = CircuitBreaker.decorateSupplier(circuitBreaker,
                Retry.decorateSupplier(retry, operation));

        return decorated.get();
    }

    public CircuitBreaker getCircuitBreaker(String name) {
        return circuitBreakerRegistry.circuitBreaker(name);
    }

    public Retry getRetry(String name) {
        return retryRegistry.retry(name);
    }

    public void updateThresholdsBasedOnVolatility(BigDecimal currentVolatility,
            BigDecimal historicalVolatility) {
        if (currentVolatility == null || historicalVolatility == null) {
            logger.warn("Cannot update thresholds: volatility values are null");
            return;
        }

        BigDecimal volatilityRatio = currentVolatility.divide(historicalVolatility,
                java.math.RoundingMode.HALF_UP);

        int adjustedFailureThreshold = calculateAdjustedThreshold(failureThreshold, volatilityRatio);
        long adjustedResetTimeout = calculateAdjustedTimeout(resetTimeoutMs, volatilityRatio);

        logger.info("Updating resilience thresholds based on volatility: ratio={}, " +
                        "newFailureThreshold={}, newResetTimeout={}",
                volatilityRatio, adjustedFailureThreshold, adjustedResetTimeout);
    }

    private int calculateAdjustedThreshold(int baseThreshold, BigDecimal volatilityRatio) {
        double multiplier = volatilityRatio.doubleValue();
        double adjusted = baseThreshold * (1.0 + (multiplier - 1.0) * 0.5);
        return Math.max(1, (int) Math.round(adjusted));
    }

    private long calculateAdjustedTimeout(long baseTimeout, BigDecimal volatilityRatio) {
        double multiplier = volatilityRatio.doubleValue();
        double adjusted = baseTimeout * (1.0 + (multiplier - 1.0) * 0.3);
        return Math.max(1000, (long) Math.round(adjusted));
    }

    public int getFailureThreshold() {
        return failureThreshold;
    }

    public int getSuccessThreshold() {
        return successThreshold;
    }

    public long getResetTimeoutMs() {
        return resetTimeoutMs;
    }

    public int getMaxRetryAttempts() {
        return maxRetryAttempts;
    }

    public long getRetryWaitMs() {
        return retryWaitMs;
    }

    public CircuitBreakerRegistry getCircuitBreakerRegistry() {
        return circuitBreakerRegistry;
    }

    public RetryRegistry getRetryRegistry() {
        return retryRegistry;
    }

    public void shutdown() {
        logger.info("Shutting down ResilienceConfig");
        circuitBreakerRegistry.getAllCircuitBreakers().forEach(CircuitBreaker::close);
        retryRegistry.getAllRetries().forEach(Retry::close);
    }
}