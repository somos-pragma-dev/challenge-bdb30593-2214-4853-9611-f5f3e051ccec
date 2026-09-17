package com.pragma.riskengine.replay;

import com.pragma.riskengine.disruptor.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;

public class ReplayService {
    private static final Logger logger = LoggerFactory.getLogger(ReplayService.class);
    private static final int MAX_REPLAY_EVENTS = 1_000_000;
    private static final int BATCH_SIZE = 1000;

    private final Path eventStorePath;
    private final ConcurrentLinkedQueue<OrderEvent> eventBuffer;
    private final ConcurrentMap<String, List<OrderEvent>> eventsByIncident;
    private final AtomicLong eventSequence;
    private final ExecutorService replayExecutor;
    private final ScheduledExecutorService cleanupScheduler;
    private volatile boolean replayInProgress;
    private final ReplayListener replayListener;

    public ReplayService(Path eventStorePath, ReplayListener replayListener) {
        this.eventStorePath = eventStorePath != null ? eventStorePath : Paths.get("data/events");
        this.replayListener = replayListener;
        this.eventBuffer = new ConcurrentLinkedQueue<>();
        this.eventsByIncident = new ConcurrentHashMap<>();
        this.eventSequence = new AtomicLong(0);
        this.replayExecutor = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "replay-executor");
            t.setDaemon(true);
            return t;
        });
        this.cleanupScheduler = Executors.newScheduledThreadPool(1, r -> {
            Thread t = new Thread(r, "cleanup-scheduler");
            t.setDaemon(true);
            return t;
        });
        initializeEventStore();
        scheduleCleanup();
    }

    private void initializeEventStore() {
        try {
            if (!Files.exists(eventStorePath)) {
                Files.createDirectories(eventStorePath);
                logger.info("Created event store directory: {}", eventStorePath);
            }
        } catch (IOException e) {
            logger.error("Failed to initialize event store at {}", eventStorePath, e);
        }
    }

    public void recordEvent(OrderEvent event) {
        if (event == null) {
            logger.warn("Attempted to record null event");
            return;
        }
        eventBuffer.offer(event);
        if (eventBuffer.size() >= BATCH_SIZE) {
            flushEventBuffer();
        }
    }

    public void recordEventForIncident(String incidentId, OrderEvent event) {
        if (incidentId == null || incidentId.isBlank()) {
            logger.warn("Attempted to record event with blank incident ID");
            return;
        }
        eventsByIncident.computeIfAbsent(incidentId, k -> new ArrayList<>()).add(event);
        recordEvent(event);
    }

    private void flushEventBuffer() {
        List<OrderEvent> batch = new ArrayList<>();
        OrderEvent event;
        int count = 0;
        while ((event = eventBuffer.poll()) != null && count < BATCH_SIZE) {
            batch.add(event);
            count++;
        }
        if (!batch.isEmpty()) {
            persistBatch(batch);
        }
    }

    private void persistBatch(List<OrderEvent> batch) {
        String filename = String.format("events_%d.dat", System.currentTimeMillis());
        Path filePath = eventStorePath.resolve(filename);
        try (DataOutputStream dos = new DataOutputStream(
                new BufferedOutputStream(Files.newOutputStream(filePath, StandardOpenOption.CREATE, StandardOpenOption.APPEND)))) {
            for (OrderEvent evt : batch) {
                dos.writeLong(eventSequence.incrementAndGet());
                dos.writeUTF(evt.getOrderId() != null ? evt.getOrderId() : "");
                dos.writeUTF(evt.getTraderId() != null ? evt.getTraderId() : "");
                dos.writeUTF(evt.getStrategyId() != null ? evt.getStrategyId() : "");
                dos.writeUTF(evt.getInstrumentId() != null ? evt.getInstrumentId() : "");
                dos.writeUTF(evt.getSide() != null ? evt.getSide().name() : "");
                dos.writeDouble(evt.getQuantity() != null ? evt.getQuantity().doubleValue() : 0.0);
                dos.writeDouble(evt.getLimitPrice() != null ? evt.getLimitPrice().doubleValue() : 0.0);
                dos.writeLong(evt.getReceivedTime() != null ? evt.getReceivedTime().toEpochMilli() : System.currentTimeMillis());
            }
            logger.debug("Persisted {} events to {}", batch.size(), filename);
        } catch (IOException e) {
            logger.error("Failed to persist event batch", e);
        }
    }

    public CompletableFuture<ReplayResult> replayFromTimestamp(Instant startTime) {
        return replayFromTimestamp(startTime, Instant.now());
    }

    public CompletableFuture<ReplayResult> replayFromTimestamp(Instant startTime, Instant endTime) {
        if (replayInProgress) {
            return CompletableFuture.failedFuture(
                    new IllegalStateException("Replay already in progress"));
        }
        if (startTime == null || endTime == null) {
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Start and end times must not be null"));
        }
        if (startTime.isAfter(endTime)) {
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Start time must be before end time"));
        }

        replayInProgress = true;
        logger.info("Starting replay from {} to {}", startTime, endTime);

        return CompletableFuture.supplyAsync(() -> {
            ReplayResult result = new ReplayResult(startTime, endTime);
            try {
                List<Path> eventFiles = findEventFiles();
                List<OrderEvent> eventsToReplay = new ArrayList<>();

                for (Path file : eventFiles) {
                    List<OrderEvent> fileEvents = loadEventsFromFile(file);
                    for (OrderEvent evt : fileEvents) {
                        Instant ts = evt.getReceivedTime();
                        if (ts != null &&
                            !ts.isBefore(startTime) &&
                            !ts.isAfter(endTime)) {
                            eventsToReplay.add(evt);
                        }
                    }
                }

                eventsToReplay.sort(Comparator.comparing(OrderEvent::getReceivedTime,
                        Comparator.nullsLast(Comparator.naturalOrder())));

                result.setTotalEventsFound(eventsToReplay.size());
                int processedCount = 0;

                for (OrderEvent evt : eventsToReplay) {
                    if (replayListener != null) {
                        replayListener.onEventReplayed(evt);
                    }
                    processedCount++;
                    result.addProcessedEvent(evt);
                }

                result.setSuccess(true);
                logger.info("Replay completed: {} events processed", processedCount);
            } catch (Exception e) {
                logger.error("Replay failed", e);
                result.setSuccess(false);
                result.setErrorMessage(e.getMessage());
            } finally {
                replayInProgress = false;
            }
            return result;
        }, replayExecutor);
    }

    public CompletableFuture<ReplayResult> replayIncident(String incidentId) {
        if (incidentId == null || incidentId.isBlank()) {
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Incident ID must not be blank"));
        }

        List<OrderEvent> incidentEvents = eventsByIncident.get(incidentId);
        if (incidentEvents == null || incidentEvents.isEmpty()) {
            return CompletableFuture.completedFuture(
                    new ReplayResult(Instant.MIN, Instant.MAX).withNoEventsFound());
        }

        replayInProgress = true;
        logger.info("Replaying incident {} with {} events", incidentId, incidentEvents.size());

        return CompletableFuture.supplyAsync(() -> {
            ReplayResult result = new ReplayResult(Instant.MIN, Instant.MAX);
            result.setIncidentId(incidentId);
            try {
                List<OrderEvent> sortedEvents = new ArrayList<>(incidentEvents);
                sortedEvents.sort(Comparator.comparing(OrderEvent::getReceivedTime,
                        Comparator.nullsLast(Comparator.naturalOrder())));

                result.setTotalEventsFound(sortedEvents.size());

                for (OrderEvent evt : sortedEvents) {
                    if (replayListener != null) {
                        replayListener.onEventReplayed(evt);
                    }
                    result.addProcessedEvent(evt);
                }

                result.setSuccess(true);
                logger.info("Incident replay completed: {} events", sortedEvents.size());
            } catch (Exception e) {
                logger.error("Incident replay failed for {}", incidentId, e);
                result.setSuccess(false);
                result.setErrorMessage(e.getMessage());
            } finally {
                replayInProgress = false;
            }
            return result;
        }, replayExecutor);
    }

    private List<Path> findEventFiles() throws IOException {
        try (Stream<Path> files = Files.list(eventStorePath)) {
            return files.filter(p -> p.toString().endsWith(".dat"))
                    .sorted()
                    .collect(Collectors.toList());
        }
    }

    private List<OrderEvent> loadEventsFromFile(Path filePath) {
        List<OrderEvent> events = new ArrayList<>();
        try (DataInputStream dis = new DataInputStream(
                new BufferedInputStream(Files.newInputStream(filePath)))) {
            while (dis.available() > 0) {
                try {
                    dis.readLong(); // sequence
                    String orderId = dis.readUTF();
                    String traderId = dis.readUTF();
                    String strategyId = dis.readUTF();
                    String instrumentId = dis.readUTF();
                    String side = dis.readUTF();
                    double quantity = dis.readDouble();
                    double price = dis.readDouble();
                    long timestamp = dis.readLong();

                    if (!orderId.isEmpty()) {
                        OrderEvent evt = new OrderEvent();
                        evt.setOrderId(orderId);
                        evt.setTraderId(traderId);
                        evt.setStrategyId(strategyId);
                        evt.setInstrumentId(instrumentId);
                        evt.setSide(OrderEvent.OrderSide.valueOf(side));
                        evt.setQuantity(BigDecimal.valueOf(quantity));
                        evt.setLimitPrice(BigDecimal.valueOf(price));
                        evt.setReceivedTime(Instant.ofEpochMilli(timestamp));
                        events.add(evt);
                    }
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException e) {
            logger.error("Failed to load events from {}", filePath, e);
        }
        return events;
    }

    public List<OrderEvent> getRecentEvents(int count) {
        List<Path> files;
        try {
            files = findEventFiles();
        } catch (IOException e) {
            logger.error("Failed to find event files", e);
            return Collections.emptyList();
        }

        List<OrderEvent> recentEvents = new ArrayList<>();
        for (int i = files.size() - 1; i >= 0 && recentEvents.size() < count; i--) {
            List<OrderEvent> fileEvents = loadEventsFromFile(files.get(i));
            for (int j = fileEvents.size() - 1; j >= 0 && recentEvents.size() < count; j--) {
                recentEvents.add(fileEvents.get(j));
            }
        }
        return recentEvents;
    }

    public Map<String, Long> getEventCountByTrader() {
        Map<String, Long> counts = new ConcurrentHashMap<>();
        try {
            List<Path> files = findEventFiles();
            for (Path file : files) {
                List<OrderEvent> events = loadEventsFromFile(file);
                for (OrderEvent evt : events) {
                    if (evt.getTraderId() != null) {
                        counts.merge(evt.getTraderId(), 1L, Long::sum);
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Failed to count events by trader", e);
        }
        return counts;
    }

    private void scheduleCleanup() {
        cleanupScheduler.scheduleAtFixedRate(() -> {
            try {
                flushEventBuffer();
                cleanupOldFiles();
            } catch (Exception e) {
                logger.error("Cleanup failed", e);
            }
        }, 1, 1, TimeUnit.HOURS);
    }

    private void cleanupOldFiles() {
        int maxAgeHours = 24;
        Instant cutoff = Instant.now().minus(Duration.ofHours(maxAgeHours));
        try (Stream<Path> files = Files.list(eventStorePath)) {
            files.filter(p -> p.toString().endsWith(".dat"))
                .filter(p -> {
                    try {
                        FileTime ft = Files.getLastModifiedTime(p);
                        return ft.toInstant().isBefore(cutoff);
                    } catch (IOException e) {
                        return false;
                    }
                })
                .forEach(p -> {
                    try {
                        Files.delete(p);
                        logger.info("Deleted old event file: {}", p.getFileName());
                    } catch (IOException e) {
                        logger.warn("Failed to delete {}", p.getFileName(), e);
                    }
                });
        } catch (IOException e) {
            logger.error("Failed to cleanup old files", e);
        }
    }

    public boolean isReplayInProgress() {
        return replayInProgress;
    }

    public long getEventSequence() {
        return eventSequence.get();
    }

    public void shutdown() {
        logger.info("Shutting down ReplayService");
        flushEventBuffer();
        replayExecutor.shutdown();
        cleanupScheduler.shutdown();
        try {
            if (!replayExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                replayExecutor.shutdownNow();
            }
            if (!cleanupScheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                cleanupScheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            replayExecutor.shutdownNow();
            cleanupScheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public interface ReplayListener {
        void onEventReplayed(OrderEvent event);
    }

    public static class ReplayResult {
        private final Instant startTime;
        private final Instant endTime;
        private volatile boolean success;
        private volatile String errorMessage;
        private volatile int totalEventsFound;
        private volatile String incidentId;
        private final List<OrderEvent> processedEvents;

        public ReplayResult(Instant startTime, Instant endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.processedEvents = new CopyOnWriteArrayList<>();
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public void setTotalEventsFound(int totalEventsFound) {
            this.totalEventsFound = totalEventsFound;
        }

        public void setIncidentId(String incidentId) {
            this.incidentId = incidentId;
        }

        public void addProcessedEvent(OrderEvent event) {
            this.processedEvents.add(event);
        }

        public ReplayResult withNoEventsFound() {
            this.totalEventsFound = 0;
            this.success = true;
            return this;
        }

        public boolean isSuccess() { return success; }
        public String getErrorMessage() { return errorMessage; }
        public int getTotalEventsFound() { return totalEventsFound; }
        public int getProcessedEventCount() { return processedEvents.size(); }
        public Instant getStartTime() { return startTime; }
        public Instant getEndTime() { return endTime; }
        public String getIncidentId() { return incidentId; }
        public List<OrderEvent> getProcessedEvents() { return List.copyOf(processedEvents); }
    }
}