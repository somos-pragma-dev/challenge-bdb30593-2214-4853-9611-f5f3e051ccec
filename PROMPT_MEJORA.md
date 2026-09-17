# Prompt para Mejorar el Codigo Base

Copia y pega el contenido del bloque de abajo en un asistente de IA (Claude, ChatGPT)
para obtener un ZIP con el proyecto completo y arrancable.

Si preferis trabajar en tu editor con un agente local (Claude Code, Cursor, Copilot), usa `AGENTS.md` en vez de este archivo: dice lo mismo pero para que escriba los archivos en disco.

## Las dos reglas que no se negocian

1. **Completa el boilerplate.** Todo lo que el proyecto necesita para compilar y arrancar: manifiesto de dependencias, punto de entrada, configuracion, capa de interfaz, y las capas del patron arquitectonico declarado. Eso es andamiaje y es tu trabajo.
2. **NO resuelvas el reto.** Los entregables de las fases son el trabajo de la persona. El hueco pedagogico se deja como esta: el proyecto arranca, pero lo que el reto pide implementar NO esta implementado.

Dicho de otra forma: si algo impide compilar, arreglalo. Si algo es logica de negocio incompleta, validaciones ausentes, un secreto hardcodeado o un patron mejorable, dejalo exactamente como esta — es lo que la persona tiene que encontrar.

## Lo que le falta a este proyecto

Esto NO lo tenes que adivinar: salio de comparar el proyecto contra la arquitectura declarada del reto y de un analisis estatico del codigo. Completalo TODO.

### Referencias colgando en el codigo que si esta

Cada una rompe la compilacion:

- `src/main/java/com/pragma/riskengine/Main.java` — `KillSwitchActivatedException`: KillSwitchActivatedException se usa en el cuerpo del archivo pero no esta importado. El proyecto lo declara en com.pragma.riskengine.exception.KillSwitchActivatedException.
- `src/main/java/com/pragma/riskengine/model/Threshold.java` — `Duration`: Duration se usa en el cuerpo del archivo pero no esta importado. El proyecto lo declara en com.pragma.riskengine.exception.Duration.
- `src/main/java/com/pragma/riskengine/disruptor/OrderEvent.java` — `Duration`: Duration se usa en el cuerpo del archivo pero no esta importado. El proyecto lo declara en com.pragma.riskengine.exception.Duration.
- `src/main/java/com/pragma/riskengine/disruptor/OrderEventHandler.java` — `Threshold`: Threshold se usa en el cuerpo del archivo pero no esta importado. El proyecto lo declara en com.pragma.riskengine.model.Threshold.
- `src/main/java/com/pragma/riskengine/replay/ReplayService.java` — `OrderSide`: OrderSide se usa en el cuerpo del archivo pero no esta importado. El proyecto lo declara en com.pragma.riskengine.disruptor.OrderSide.
- `src/main/java/com/pragma/riskengine/replay/ReplayService.java` — `Duration`: Duration se usa en el cuerpo del archivo pero no esta importado. El proyecto lo declara en com.pragma.riskengine.exception.Duration.
- `src/test/java/com/pragma/riskengine/disruptor/OrderEventHandlerTest.java` — `OrderStatus`: OrderStatus se usa en el cuerpo del archivo pero no esta importado. El proyecto lo declara en com.pragma.riskengine.adapter.exchange.OrderStatus.
- `src/main/java/com/pragma/riskengine/service/RiskScoringService.java` — `ResilienceConfig`: El import com.pragma.riskengine.config.ResilienceConfig no se usa en ningun lado del cuerpo del archivo. Se puede eliminar.
- `src/test/java/com/pragma/riskengine/disruptor/OrderEventHandlerTest.java` — `RiskThresholdExceededException`: El import com.pragma.riskengine.exception.RiskThresholdExceededException no se usa en ningun lado del cuerpo del archivo. Se puede eliminar.
- `src/test/java/com/pragma/riskengine/disruptor/OrderEventHandlerTest.java` — `KillSwitchActivatedException`: El import com.pragma.riskengine.exception.KillSwitchActivatedException no se usa en ningun lado del cuerpo del archivo. Se puede eliminar.
- `src/main/java/com/pragma/riskengine/Main.java` — `DisruptorConfig.createDisruptor`: Se invoca `createDisruptor` sobre `DisruptorConfig`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/Main.java` — `AuditLogger.logError`: Se invoca `logError` sobre `AuditLogger`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/Main.java` — `MarketDataFeedAdapter.connect`: Se invoca `connect` sobre `MarketDataFeedAdapter`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/Main.java` — `RiskScoringService.recalculateVolatility`: Se invoca `recalculateVolatility` sobre `RiskScoringService`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/Main.java` — `KillSwitchService.recalculateThresholds`: Se invoca `recalculateThresholds` sobre `KillSwitchService`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/Main.java` — `RiskScoringService.getCurrentVolatility`: Se invoca `getCurrentVolatility` sobre `RiskScoringService`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/Main.java` — `AuditLogger.logWarning`: Se invoca `logWarning` sobre `AuditLogger`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/Main.java` — `OrderEvent.setInstrument`: Se invoca `setInstrument` sobre `OrderEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/Main.java` — `OrderEvent.setPrice`: Se invoca `setPrice` sobre `OrderEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/Main.java` — `OrderEvent.setTimestamp`: Se invoca `setTimestamp` sobre `OrderEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/Main.java` — `MarketDataFeedAdapter.disconnect`: Se invoca `disconnect` sobre `MarketDataFeedAdapter`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/model/RiskModel.java` — `Position.quantity`: Se invoca `quantity` sobre `Position`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/model/RiskModel.java` — `Position.notional`: Se invoca `notional` sobre `Position`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/model/Threshold.java` — `Duration.compareTo`: Se invoca `compareTo` sobre `Duration`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/disruptor/OrderEventHandler.java` — `RiskScoringService.validateThresholds`: Se invoca `validateThresholds` sobre `RiskScoringService`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/disruptor/MarketDataEventHandler.java` — `MarketDataEvent.getEventType`: Se invoca `getEventType` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/disruptor/MarketDataEventHandler.java` — `MarketDataEvent.getInstrumentId`: Se invoca `getInstrumentId` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/disruptor/MarketDataEventHandler.java` — `MarketDataEvent.getPrice`: Se invoca `getPrice` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/disruptor/MarketDataEventHandler.java` — `MarketDataEvent.getTimestamp`: Se invoca `getTimestamp` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/disruptor/MarketDataEventHandler.java` — `MarketDataEvent.getQuantity`: Se invoca `getQuantity` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/disruptor/MarketDataEventHandler.java` — `MarketDataEvent.getBidLevels`: Se invoca `getBidLevels` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/disruptor/MarketDataEventHandler.java` — `MarketDataEvent.getAskLevels`: Se invoca `getAskLevels` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/disruptor/MarketDataEventHandler.java` — `MarketDataEvent.getVolatility`: Se invoca `getVolatility` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/service/RiskScoringService.java` — `OrderRequest.strategyId`: Se invoca `strategyId` sobre `OrderRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/service/RiskScoringService.java` — `OrderRequest.price`: Se invoca `price` sobre `OrderRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/service/RiskScoringService.java` — `OrderRequest.quantity`: Se invoca `quantity` sobre `OrderRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/service/RiskScoringService.java` — `OrderRequest.traderId`: Se invoca `traderId` sobre `OrderRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/service/RiskScoringService.java` — `OrderRequest.instrumentId`: Se invoca `instrumentId` sobre `OrderRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/service/RiskScoringService.java` — `OrderRequest.orderId`: Se invoca `orderId` sobre `OrderRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/service/KillSwitchService.java` — `StrategyKillSwitch.active`: Se invoca `active` sobre `StrategyKillSwitch`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/service/KillSwitchService.java` — `StrategyKillSwitch.activatedAt`: Se invoca `activatedAt` sobre `StrategyKillSwitch`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/adapter/marketdata/MarketDataFeedAdapter.java` — `OrderBookUpdate.instrumentId`: Se invoca `instrumentId` sobre `OrderBookUpdate`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/adapter/marketdata/MarketDataFeedAdapter.java` — `OrderBookUpdate.bids`: Se invoca `bids` sobre `OrderBookUpdate`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/adapter/marketdata/MarketDataFeedAdapter.java` — `OrderBook.updateBid`: Se invoca `updateBid` sobre `OrderBook`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/adapter/marketdata/MarketDataFeedAdapter.java` — `OrderBookUpdate.asks`: Se invoca `asks` sobre `OrderBookUpdate`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/adapter/marketdata/MarketDataFeedAdapter.java` — `OrderBook.updateAsk`: Se invoca `updateAsk` sobre `OrderBook`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/adapter/marketdata/MarketDataFeedAdapter.java` — `MarketDataListener.onOrderBookUpdate`: Se invoca `onOrderBookUpdate` sobre `MarketDataListener`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/adapter/marketdata/MarketDataFeedAdapter.java` — `Trade.instrumentId`: Se invoca `instrumentId` sobre `Trade`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/adapter/marketdata/MarketDataFeedAdapter.java` — `Trade.price`: Se invoca `price` sobre `Trade`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/adapter/marketdata/MarketDataFeedAdapter.java` — `Trade.quantity`: Se invoca `quantity` sobre `Trade`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/adapter/marketdata/MarketDataFeedAdapter.java` — `MarketDataListener.onTrade`: Se invoca `onTrade` sobre `MarketDataListener`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/adapter/marketdata/MarketDataFeedAdapter.java` — `OrderBook.getBestBid`: Se invoca `getBestBid` sobre `OrderBook`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/adapter/marketdata/MarketDataFeedAdapter.java` — `OrderBook.getBestAsk`: Se invoca `getBestAsk` sobre `OrderBook`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/adapter/exchange/ExchangeAdapter.java` — `OrderResponse.status`: Se invoca `status` sobre `OrderResponse`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/adapter/exchange/ExchangeAdapter.java` — `CircuitBreakerAdapter.isOpen`: Se invoca `isOpen` sobre `CircuitBreakerAdapter`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/adapter/exchange/ExchangeAdapter.java` — `PendingOrder.orderId`: Se invoca `orderId` sobre `PendingOrder`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/adapter/exchange/ExchangeAdapter.java` — `PendingOrder.sequenceId`: Se invoca `sequenceId` sobre `PendingOrder`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/adapter/exchange/ExchangeAdapter.java` — `CircuitBreakerAdapter.recordFailure`: Se invoca `recordFailure` sobre `CircuitBreakerAdapter`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/adapter/exchange/ExchangeAdapter.java` — `CircuitBreakerAdapter.recordSuccess`: Se invoca `recordSuccess` sobre `CircuitBreakerAdapter`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/audit/AuditLogger.java` — `AuditEntry.addField`: Se invoca `addField` sobre `AuditEntry`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/audit/AuditLogger.java` — `KillSwitchActivatedException.getThresholdId`: Se invoca `getThresholdId` sobre `KillSwitchActivatedException`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/audit/AuditLogger.java` — `KillSwitchActivatedException.getThresholdType`: Se invoca `getThresholdType` sobre `KillSwitchActivatedException`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/audit/AuditLogger.java` — `KillSwitchActivatedException.getCurrentValue`: Se invoca `getCurrentValue` sobre `KillSwitchActivatedException`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/audit/AuditLogger.java` — `KillSwitchActivatedException.getThresholdValue`: Se invoca `getThresholdValue` sobre `KillSwitchActivatedException`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/audit/AuditLogger.java` — `KillSwitchActivatedException.getExceedPercentage`: Se invoca `getExceedPercentage` sobre `KillSwitchActivatedException`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/audit/AuditLogger.java` — `KillSwitchActivatedException.getRiskMetric`: Se invoca `getRiskMetric` sobre `KillSwitchActivatedException`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/audit/AuditLogger.java` — `KillSwitchActivatedException.isCritical`: Se invoca `isCritical` sobre `KillSwitchActivatedException`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/audit/AuditLogger.java` — `AuditEntry.toStructuredString`: Se invoca `toStructuredString` sobre `AuditEntry`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/audit/AuditLogger.java` — `AuditEntry.toKeyValueString`: Se invoca `toKeyValueString` sobre `AuditEntry`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/replay/ReplayService.java` — `ReplayResult.setTotalEventsFound`: Se invoca `setTotalEventsFound` sobre `ReplayResult`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/replay/ReplayService.java` — `ReplayListener.onEventReplayed`: Se invoca `onEventReplayed` sobre `ReplayListener`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/replay/ReplayService.java` — `ReplayResult.addProcessedEvent`: Se invoca `addProcessedEvent` sobre `ReplayResult`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/replay/ReplayService.java` — `ReplayResult.setSuccess`: Se invoca `setSuccess` sobre `ReplayResult`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/replay/ReplayService.java` — `ReplayResult.setErrorMessage`: Se invoca `setErrorMessage` sobre `ReplayResult`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/replay/ReplayService.java` — `ReplayResult.setIncidentId`: Se invoca `setIncidentId` sobre `ReplayResult`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/pragma/riskengine/disruptor/OrderEventHandlerTest.java` — `OrderEvent.status`: Se invoca `status` sobre `OrderEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/pragma/riskengine/disruptor/OrderEventHandlerTest.java` — `OrderEvent.rejectionReason`: Se invoca `rejectionReason` sobre `OrderEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.

## Como saber que terminaste

```bash
el comando de build o arranque canonico del stack elegido
```

Ese comando corriendo sin errores es la definicion de "listo".

---

```
## Briefing del reto (autoridad)
Este bloque manda sobre los archivos adjuntos. El stack y el rol salen de AQUÍ, no de un topic genérico ni de markdown placeholder.

### Contexto técnico original
Sistema master-l2 que evalúa el riesgo de cada orden antes de enviarla al exchange, en menos de 500 microsegundos p99. Consume feed de market data (nivel 2 orderbook + trades), mantiene un modelo de VaR intraday, aplica límites por trader/estrategia/instrumento en tiempo real, y dispara circuit breakers dinámicos que bloquean nuevas órdenes cuando la exposición supera thresholds calibrados por volatilidad. Debe justificar el uso de estructuras lock-free vs mutex, la elección entre C++ vs Rust vs Java LMAX Disruptor, cómo garantiza la consistencia entre múltiples risk engines corriendo en paralelo (consensus vs sharding por instrument), la política de kill switch cuando detecta un algoritmo que se comporta de forma anómala, y la estrategia de replay determinístico para post-mortem de incidents. Incluye compliance con regulaciones MiFID II para trazabilidad de decisiones de riesgo.

### Reto
- Tema: motor de risk scoring en tiempo real para trading algorítmico con circuit breakers dinámicos
- Seniority: master-l2
- Tipo: mixed
- Título: Diseño y Justificación de un Motor de Risk Scoring para Trading Algorítmico
- Tiempo estimado: 2 semanas

### Fases (trabajo del HUMANO — PROHIBIDO completarlas)
No implementes estos entregables. Dejalos como hueco pedagógico. El asistente solo materializa el proyecto arrancable para que el participante pueda trabajar.
- Fase 1: Exploración y Modelado Inicial — objetivo: Comprender el problema y modelar el sistema inicial. — entregable (NO resolver): Diagrama de relaciones y descripción del modelo inicial.
- Fase 2: Evaluación de Decisiones de Diseño — objetivo: Evaluar y justificar las decisiones de diseño críticas. — entregable (NO resolver): Documento de decisiones de diseño con pros/contras y justificación.
- Fase 3: Implementación y Optimización — objetivo: Implementar y optimizar el motor de risk scoring. — entregable (NO resolver): Motor de risk scoring implementado y optimizado.

Eres un asistente experto en análisis, corrección y generación de archivos de cualquier tipo:
código fuente, documentación, hojas de cálculo, documentos Word, configuraciones, entre otros.
Voy a enviarte una cadena de texto que contiene uno o más archivos. Cada archivo está delimitado por un marcador con el siguiente formato:
// === ARCHIVO: ruta/del/archivo.extension ===
o también puede aparecer como:
## === ARCHIVO: ruta/del/archivo.extension ===
Lo que sigue al marcador puede ser:

El contenido real del archivo (código, texto, YAML, etc.)
Una descripción en lenguaje natural de lo que debe contener el archivo


TU TAREA
PASO 0 — ¿Esto es un proyecto o una carcasa?
Antes de extraer archivos, leé el Briefing (si está) y diagnosticá el adjunto.

Es CARCASA si ocurre CUALQUIERA de estas:
- No hay manifiesto de dependencias del stack del briefing (manifest.json de VTEX IO / package.json / pom.xml / build.gradle / requirements.txt / go.mod / *.tf / *.csproj, según corresponda)
- Hay un "binario" que en realidad es un comentario ("no puede ser mostrado como texto plano", placeholder .fig/.docx vacío)
- Los markdowns ya completan entregables de fases posteriores ("se implementó fade-in", lista de áreas ya resuelta)

Si es CARCASA:
- MATERIALIZÁ un proyecto que arranca en el stack del briefing (VTEX IO Store Framework, Angular, Terraform, pytest, Nest, etc.). Incluí manifiesto, punto de entrada y capa de interfaz reales.
- NO copies los markdowns de "solución" como si fueran el producto. Son ruido de generación.
- NO resuelvas las fases del briefing (están marcadas PROHIBIDO). Dejá el hueco pedagógico: el flujo existe, las microinteracciones/calidad/infra que el reto pide NO están hechas.
- Después seguí al PASO 5 (ZIP).

Si es un proyecto REAL (manifiesto + código que compila o arranca):
- Seguí PASO 1 en adelante. 🔴 compilación sí. 🟡 pedagógico no.

PASO 1 — Detección y extracción
Identifica todos los archivos presentes en la cadena. Para cada archivo extrae:

Su ruta completa (ej: src/main/java/com/pragma/Service.java)
Su contenido o descripción

PASO 2 — Clasificación por tipo
Clasifica cada archivo en una de estas categorías:
A) Código fuente (Java, Python, TypeScript, JavaScript, Kotlin, etc.)
B) Configuración / documentación (YAML, properties, Markdown, JSON, txt, etc.)
C) Excel (.xlsx, .xls, .csv)
D) Word (.docx, .doc)
E) Otro tipo de archivo binario o especial
PASO 3 — Clasificación de errores en código fuente

Objetivo prioritario: que el proyecto compile. No corrijas flujo de negocio ni lógica funcional.

Antes de modificar cualquier archivo de código fuente, clasifica cada problema encontrado en una de estas dos categorías:
🔴 ERROR DE COMPILACIÓN — corregir siempre
Son errores que impiden que el proyecto arranque, sin valor pedagógico:

Import faltante o incorrecto
Clase, método o variable referenciada que no existe en ningún archivo del proyecto
Error de sintaxis
Anotación con atributos inválidos
Dependencia ausente en pom.xml, package.json, etc.
Archivo referenciado que no existe y debe ser creado con implementación mínima

→ CORREGIR estos errores.
🟡 PROBLEMA FUNCIONAL O DE CALIDAD — preservar siempre
Son problemas que no impiden compilar. Pueden ser intencionales para el aprendizaje:

Clave secreta hardcodeada ("secret", "password123")
API deprecada que funciona pero tiene reemplazo moderno
Lógica de negocio incorrecta o incompleta
Código redundante o de baja legibilidad
Falta de validaciones en flujo de negocio
Patrones de diseño incorrectos pero funcionales
Concurrencia no segura
Configuración funcional pero no óptima

→ PRESERVAR tal cual. No corregir, no mejorar, no comentar.
PASO 4 — Procesamiento según tipo de archivo
Tipo A — Código fuente
Aplica únicamente las correcciones clasificadas como 🔴 ERROR DE COMPILACIÓN.
No alteres ningún elemento clasificado como 🟡 PROBLEMA FUNCIONAL O DE CALIDAD.
Si falta un archivo referenciado, créalo con la implementación mínima necesaria para compilar.
Tipo B — Configuración / documentación
Extrae el contenido tal cual, sin modificaciones salvo errores evidentes de sintaxis
(ej: YAML mal indentado).
Tipo C — Excel (.xlsx)
Si viene con contenido real, genera el archivo respetando ese contenido.
Si viene con descripción en lenguaje natural, genera un archivo Excel funcional con:

Fila de encabezados en negrita con color de fondo distintivo
Columnas con ancho ajustado al contenido
Tipos de dato correctos por columna
Validaciones si la descripción lo indica
Hojas nombradas descriptivamente si hay más de una
Filas de ejemplo si no hay datos reales

Tipo D — Word (.docx)
Si viene con contenido real, genera el archivo respetando ese contenido.
Si viene con descripción en lenguaje natural, genera un documento Word funcional con:

Estilos de título (Título 1, Título 2) para jerarquía de secciones
Fuente legible (Calibri o equivalente), tamaño 11-12pt para cuerpo
Márgenes estándar
Tabla de contenido si tiene múltiples secciones
Tablas con encabezados en negrita si aplica

Tipo E — Otro
Genera el archivo con el contenido o estructura más apropiada según la descripción.
PASO 5 — Exportación en ZIP
Empaqueta todos los archivos en un único archivo ZIP descargable respetando exactamente
la estructura de rutas indicada por los marcadores.
El ZIP debe incluir:

Archivos de código con únicamente los errores de compilación corregidos
Archivos de configuración y documentación sin cambios
Archivos nuevos creados para resolver dependencias de compilación faltantes
Archivos Excel y Word generados desde descripción

IMPORTANTE: El ZIP debe estar listo para descargar al finalizar. No preguntes si el usuario
quiere generarlo. Simplemente genera el archivo y proporciona el enlace de descarga; No debes desplegar en el chat el resumen de lo que arreglaste al Zip, solo entregalo.

REGLAS IMPORTANTES

No omitas ningún archivo aunque no tenga errores ni modificaciones
Respeta los nombres y rutas exactas indicadas por los marcadores
Si un archivo no tiene marcador claro, infiere el nombre desde su contenido
Si la cadena contiene solo documentación, placeholders o binarios fake, NO la reproduzcas:
aplicá PASO 0 (materializar el proyecto del briefing). Reproducir la carcasa es un fallo.
No agregues texto después del enlace de descarga del ZIP
No preguntes si el usuario quiere el ZIP: simplemente generalo siempre
Si detectas que falta un archivo de configuración necesario para compilar
(pom.xml, package.json, requirements.txt, build.gradle, etc.), créalo e inclúyelo
inferiendo su contenido desde los imports y frameworks detectados en el código
Nunca corrijas problemas 🟡 aunque parezcan obvios o fáciles de mejorar.
El participante que recibirá este proyecto los debe encontrar y resolver él mismo.


INPUT
Aquí está la cadena con los archivos:

// === ARCHIVO: pom.xml ===
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.pragma</groupId>
    <artifactId>risk-engine</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>jar</packaging>

    <name>Risk Scoring Engine</name>
    <description>Real-time risk scoring engine for algorithmic trading with dynamic circuit breakers</description>

    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <disruptor.version>4.0.0</disruptor.version>
        <resilience4j.version>2.1.0</resilience4j.version>
        <slf4j.version>2.0.9</slf4j.version>
        <logback.version>1.4.14</logback.version>
        <junit.version>5.10.0</junit.version>
        <mockito.version>5.5.0</mockito.version>
    </properties>

    <dependencies>
        <!-- LMAX Disruptor for lock-free event processing -->
        <dependency>
            <groupId>com.lmax</groupId>
            <artifactId>disruptor</artifactId>
            <version>${disruptor.version}</version>
        </dependency>

        <!-- Resilience4j Circuit Breaker -->
        <dependency>
            <groupId>io.github.resilience4j</groupId>
            <artifactId>resilience4j-circuitbreaker</artifactId>
            <version>${resilience4j.version}</version>
        </dependency>

        <!-- Resilience4j Retry -->
        <dependency>
            <groupId>io.github.resilience4j</groupId>
            <artifactId>resilience4j-retry</artifactId>
            <version>${resilience4j.version}</version>
        </dependency>

        <!-- SLF4J API -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>${slf4j.version}</version>
        </dependency>

        <!-- Logback Classic -->
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>${logback.version}</version>
            <scope>runtime</scope>
        </dependency>

        <!-- JUnit Jupiter API -->
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-api</artifactId>
            <version>${junit.version}</version>
            <scope>test</scope>
        </dependency>

        <!-- JUnit Jupiter Engine -->
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-engine</artifactId>
            <version>${junit.version}</version>
            <scope>test</scope>
        </dependency>

        <!-- Mockito Core -->
        <dependency>
            <groupId>org.mockito</groupId>
            <artifactId>mockito-core</artifactId>
            <version>${mockito.version}</version>
            <scope>test</scope>
        </dependency>

        <!-- Mockito JUnit Jupiter -->
        <dependency>
            <groupId>org.mockito</groupId>
            <artifactId>mockito-junit-jupiter</artifactId>
            <version>${mockito.version}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
                <configuration>
                    <source>21</source>
                    <target>21</target>
                    <compilerArgs>
                        <arg>--enable-preview</arg>
                    </compilerArgs>
                </configuration>
            </plugin>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.1.2</version>
                <configuration>
                    <excludes>
                        <exclude>**/integration/**</exclude>
                    </excludes>
                </configuration>
            </plugin>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jar-plugin</artifactId>
                <version>3.3.0</version>
                <configuration>
                    <archive>
                        <manifest>
                            <mainClass>com.pragma.riskengine.Main</mainClass>
                            <addClasspath>true</addClasspath>
                        </manifest>
                    </archive>
                </configuration>
            </plugin>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>3.5.1</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>
                        <configuration>
                            <transformers>
                                <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                    <mainClass>com.pragma.riskengine.Main</mainClass>
                                </transformer>
                                <transformer implementation="org.apache.maven.plugins.shade.resource.ServicesResourceTransformer"/>
                            </transformers>
                            <filters>
                                <filter>
                                    <artifact>*:*</artifact>
                                    <excludes>
                                        <exclude>META-INF/*.SF</exclude>
                                        <exclude>META-INF/*.DSA</exclude>
                                        <exclude>META-INF/*.RSA</exclude>
                                    </excludes>
                                </filter>
                            </filters>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>

// === ARCHIVO: src/main/java/com/pragma/riskengine/Main.java ===
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

// === ARCHIVO: src/main/resources/application.properties ===
# Configuración del Motor de Risk Scoring en Tiempo Real
# Este archivo contiene los parámetros ajustables del sistema
# que pueden modificarse sin recompilar

# ==============================================================================
# CONFIGURACIÓN DEL DISRUPTOR (LMAX)
# ==============================================================================

# Tamaño del RingBuffer - debe ser potencia de 2 para mejor rendimiento
# Valor recomendado: 4096 a 65536 dependiendo del throughput esperado
disruptor.buffer.size=65536

# Estrategia de espera del Disruptor
# Opciones: BlockingWaitStrategy, SleepingWaitStrategy, YieldingWaitStrategy, BusySpinWaitStrategy
# BlockingWaitStrategy: menor consumo CPU, mayor latencia media
# YieldingWaitStrategy: menor latencia, mayor consumo CPU
# Para trading de alta frecuencia: YieldingWaitStrategy
# Para sistemas con múltiples consumidores: BlockingWaitStrategy
disruptor.wait.strategy=BlockingWaitStrategy

# ==============================================================================
# CONFIGURACIÓN DE RIESGO (VaR y Límites)
# ==============================================================================

# Nivel de confianza para cálculo de VaR (Value at Risk)
# 0.99 = VaR al 99% (perdida maxima esperada en el 1% de los casos)
risk.var.confidence=0.99

# Ventana de tiempo para cálculo de VaR intradiario en minutos
# Valor típico: 5-60 minutos para trading intradiario
risk.var.window.minutes=15

# Exposición máxima por operación individual en la misma moneda
risk.max.exposure.trade=100000

# Exposición máxima agregada por estrategia
risk.max.exposure.strategy=500000

# Exposición máxima agregada por instrumento
risk.max.exposure.instrument=200000

# Órdenes máximas por segundo por trader
risk.max.orders.per.second=1000

# Intervalo de recalculo de volatilidad y límites en segundos
risk.recalc.interval.seconds=60

# Factor multiplicador para límites dinámicos basado en volatilidad
# Si volatilidad > 2x la media, los límites se reducen por este factor
risk.volatility.multiplier=0.75

# ==============================================================================
# CONFIGURACIÓN DEL KILL SWITCH
# ==============================================================================

# Umbral de activación del Kill Switch (0.0 a 1.0)
# Cuando la exposición supera este porcentaje del límite, se activa
killswitch.default.threshold=0.85

# Tiempo de espera en segundos antes de permitir nuevas órdenes
# después de una activación del Kill Switch
killswitch.cooldown.seconds=60

# Máximo de activaciones consecutivas antes de bloqueo permanente
# Valor -1 indica sin límite
killswitch.max.activations=5

# Habilitar recalculo dinámico de thresholds basado en volatilidad
killswitch.dynamic.threshold.enabled=true

# Factor de ajuste de threshold por volatilidad (0.0 a 1.0)
# Mayor volatilidad = menor threshold para mayor protección
killswitch.volatility.factor=0.15

# ==============================================================================
# CONFIGURACIÓN DE MARKET DATA
# ==============================================================================

# URL del feed de market data (formato: protocol://host:port)
marketdata.feed.url=tcp://localhost:5555

# Tamaño del buffer interno para mensajes de market data
marketdata.buffer.size=10000

# Habilitar descarga de snapshot completo al conectarse
marketdata.snapshot.enabled=true

# Timeout para conexión al feed en milisegundos
marketdata.connection.timeout.ms=10000

# Intervalo de heartbeats en segundos
marketdata.heartbeat.interval.seconds=30

# ==============================================================================
# CONFIGURACIÓN DEL EXCHANGE
# ==============================================================================

# URL del exchange para envío de órdenes
# Formato: protocol://host:port
# Protocolos soportados: tcp, ssl, http
exchange.url=tcp://localhost:6666

# Timeout para operaciones con el exchange en milisegundos
exchange.timeout.ms=5000

# Máximo de reintentos para operaciones fallidas
exchange.max.retries=3

# Intervalo de reintento en milisegundos
exchange.retry.interval.ms=100

# Habilitar confirmación de órdenes
exchange.confirmation.enabled=true

# ==============================================================================
# CONFIGURACIÓN DE RESILIENCIA (Circuit Breaker)
# ==============================================================================

# Umbral de tasa de fallo para abrir el circuit breaker (%)
# Cuando la tasa de fallos supera este valor, el CB se abre
circuitbreaker.failure.rate.threshold=50

# Tiempo de espera en segundos antes de intentar cerrar el CB
circuitbreaker.wait.duration.open=30

# Tamaño de la ventana deslizante para cálculo de fallos
circuitbreaker.sliding.window.size=100

# Número mínimo de llamadas para evaluar la tasa de fallo
circuitbreaker.minimum.number.of.calls=10

# Permitted calls in half-open state
circuitbreaker.permitted.number.of.calls.in.half.open.state=3

# ==============================================================================
# CONFIGURACIÓN DE REINTENTOS (Retry)
# ==============================================================================

# Número máximo de reintentos para operaciones fallidas
retry.max.attempts=3

# Intervalo base entre reintentos en milisegundos
retry.wait.duration.ms=1000

# Intervalo máximo entre reintentos
retry.max.interval.ms=10000

# Multiplicador exponencial para intervalos
retry.exponential.backoff.multiplier=2.0

# ==============================================================================
# CONFIGURACIÓN DE HEALTH CHECK
# ==============================================================================

# Intervalo de health checks en segundos
health.check.interval.seconds=30

# Umbral de utilización de buffer para alertar (%)
health.buffer.alert.threshold=0.80

# Habilitar métricas JMX
health.jmx.enabled=true

# ==============================================================================
# CONFIGURACIÓN DE AUDITORÍA
# ==============================================================================

# Habilitar logging de auditoría
audit.enabled=true

# Nivel de detalle de auditoría
# Opciones: MINIMAL, STANDARD, DETAILED, VERBOSE
audit.level=STANDARD

# Intervalo de flush de logs de auditoría en segundos
audit.flush.interval.seconds=5

# ==============================================================================
# CONFIGURACIÓN DE REPLAY
# ==============================================================================

# Habilitar servicio de replay
replay.enabled=true

# Directorio para almacenar eventos de replay
replay.data.directory=./data/replay

# Máximo de eventos a almacenar para replay
replay.max.events=100000

# Intervalo de compactación de eventos antiguos en horas
replay.compaction.interval.hours=24


// === ARCHIVO: src/main/java/com/pragma/riskengine/model/RiskModel.java ===
package com.pragma.riskengine.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Modelo de riesgo que encapsula el cálculo de VaR intraday y exposición por trader/estrategia/instrumento.
 * Implementa un modelo de riesgo paramétrico con actualización en tiempo real.
 */
public class RiskModel {

    private final ConcurrentMap<String, Position> positionsByInstrument;
    private final ConcurrentMap<String, BigDecimal> exposureByTrader;
    private final ConcurrentMap<String, BigDecimal> exposureByStrategy;
    private final ConcurrentMap<String, BigDecimal> exposureByInstrument;
    private final Map<String, List<PricePoint>> priceHistory;
    private final ReentrantReadWriteLock lock;
    private final BigDecimal confidenceLevel;
    private final int lookbackPeriods;
    private volatile BigDecimal currentVar;
    private volatile Instant lastCalculationTime;

    public RiskModel(BigDecimal confidenceLevel, int lookbackPeriods) {
        this.positionsByInstrument = new ConcurrentHashMap<>();
        this.exposureByTrader = new ConcurrentHashMap<>();
        this.exposureByStrategy = new ConcurrentHashMap<>();
        this.exposureByInstrument = new ConcurrentHashMap<>();
        this.priceHistory = new ConcurrentHashMap<>();
        this.lock = new ReentrantReadWriteLock();
        this.confidenceLevel = Objects.requireNonNull(confidenceLevel, "confidenceLevel cannot be null");
        this.lookbackPeriods = lookbackPeriods > 0 ? lookbackPeriods : 252;
        this.currentVar = BigDecimal.ZERO;
        this.lastCalculationTime = Instant.now();
    }

    /**
     * Actualiza la posición para un instrumento específico.
     */
    public void updatePosition(String instrumentId, BigDecimal quantity, BigDecimal price) {
        lock.writeLock().lock();
        try {
            Position position = positionsByInstrument.computeIfAbsent(
                instrumentId,
                k -> new Position(instrumentId, BigDecimal.ZERO, BigDecimal.ZERO)
            );
            BigDecimal newQuantity = position.quantity().add(quantity);
            BigDecimal newNotional = newQuantity.multiply(price);
            positionsByInstrument.put(instrumentId, new Position(instrumentId, newQuantity, newNotional));
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Registra un precio histórico para el cálculo de volatilidad.
     */
    public void recordPrice(String instrumentId, BigDecimal price, Instant timestamp) {
        lock.writeLock().lock();
        try {
            List<PricePoint> history = priceHistory.computeIfAbsent(instrumentId, k -> new ArrayList<>());
            history.add(new PricePoint(price, timestamp));
            if (history.size() > lookbackPeriods) {
                history.remove(0);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Calcula el VaR intraday usando el método de varianza-covarianza.
     */
    public BigDecimal calculateVar() {
        lock.readLock().lock();
        try {
            if (positionsByInstrument.isEmpty()) {
                return BigDecimal.ZERO;
            }

            BigDecimal portfolioValue = calculateTotalExposure();
            BigDecimal portfolioVolatility = calculatePortfolioVolatility();
            BigDecimal zScore = getZScoreForConfidence(confidenceLevel);
            
            BigDecimal var = portfolioValue
                .multiply(portfolioVolatility)
                .multiply(zScore)
                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);

            currentVar = var;
            lastCalculationTime = Instant.now();
            return var;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Calcula la exposición total del portafolio.
     */
    public BigDecimal calculateTotalExposure() {
        lock.readLock().lock();
        try {
            return positionsByInstrument.values().stream()
                .map(Position::notional)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Calcula la exposición por trader específico.
     */
    public BigDecimal calculateExposureByTrader(String traderId) {
        lock.readLock().lock();
        try {
            return exposureByTrader.getOrDefault(traderId, BigDecimal.ZERO);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Calcula la exposición por estrategia específica.
     */
    public BigDecimal calculateExposureByStrategy(String strategyId) {
        lock.readLock().lock();
        try {
            return exposureByStrategy.getOrDefault(strategyId, BigDecimal.ZERO);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Calcula la exposición por instrumento específico.
     */
    public BigDecimal calculateExposureByInstrument(String instrumentId) {
        lock.readLock().lock();
        try {
            Position pos = positionsByInstrument.get(instrumentId);
            return pos != null ? pos.notional() : BigDecimal.ZERO;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Actualiza la exposición para un trader después de una operación.
     */
    public void updateTraderExposure(String traderId, BigDecimal notionalDelta) {
        lock.writeLock().lock();
        try {
            BigDecimal current = exposureByTrader.getOrDefault(traderId, BigDecimal.ZERO);
            exposureByTrader.put(traderId, current.add(notionalDelta));
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Actualiza la exposición para una estrategia después de una operación.
     */
    public void updateStrategyExposure(String strategyId, BigDecimal notionalDelta) {
        lock.writeLock().lock();
        try {
            BigDecimal current = exposureByStrategy.getOrDefault(strategyId, BigDecimal.ZERO);
            exposureByStrategy.put(strategyId, current.add(notionalDelta));
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Calcula la volatilidad del portafolio basada en el histórico de precios.
     */
    private BigDecimal calculatePortfolioVolatility() {
        if (priceHistory.isEmpty()) {
            return new BigDecimal("0.20");
        }

        List<BigDecimal> returns = new ArrayList<>();
        for (List<PricePoint> history : priceHistory.values()) {
            if (history.size() < 2) continue;
            
            for (int i = 1; i < history.size(); i++) {
                BigDecimal priceCurrent = history.get(i).price();
                BigDecimal pricePrevious = history.get(i - 1).price();
                if (pricePrevious.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal ret = priceCurrent
                        .subtract(pricePrevious)
                        .divide(pricePrevious, RoundingMode.HALF_UP);
                    returns.add(ret);
                }
            }
        }

        if (returns.isEmpty()) {
            return new BigDecimal("0.20");
        }

        BigDecimal mean = returns.stream()
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .divide(BigDecimal.valueOf(returns.size()), RoundingMode.HALF_UP);

        BigDecimal variance = returns.stream()
            .map(r -> r.subtract(mean).pow(2))
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .divide(BigDecimal.valueOf(returns.size()), RoundingMode.HALF_UP);

        return BigDecimal.valueOf(Math.sqrt(variance.doubleValue() * 252));
    }

    /**
     * Obtiene el Z-score correspondiente al nivel de confianza.
     */
    private BigDecimal getZScoreForConfidence(BigDecimal confidence) {
        return switch (confidence.compareTo(new BigDecimal("0.99"))) {
            case 0, 1 -> new BigDecimal("2.33");
            case -1 when confidence.compareTo(new BigDecimal("0.95")) >= 0 -> new BigDecimal("1.65");
            default -> new BigDecimal("1.28");
        };
    }

    /**
     * Obtiene el VaR actual calculado.
     */
    public BigDecimal getCurrentVar() {
        lock.readLock().lock();
        try {
            return currentVar;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Obtiene el momento del último cálculo de VaR.
     */
    public Instant getLastCalculationTime() {
        return lastCalculationTime;
    }

    /**
     * Obtiene todas las posiciones actuales.
     */
    public Map<String, Position> getAllPositions() {
        lock.readLock().lock();
        try {
            return Map.copyOf(positionsByInstrument);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Reinicia el modelo de riesgo.
     */
    public void reset() {
        lock.writeLock().lock();
        try {
            positionsByInstrument.clear();
            exposureByTrader.clear();
            exposureByStrategy.clear();
            exposureByInstrument.clear();
            priceHistory.clear();
            currentVar = BigDecimal.ZERO;
            lastCalculationTime = Instant.now();
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Record que representa una posición en un instrumento.
     */
    public record Position(String instrumentId, BigDecimal quantity, BigDecimal notional) {
        public Position {
            Objects.requireNonNull(instrumentId, "instrumentId cannot be null");
            if (quantity == null || notional == null) {
                throw new IllegalArgumentException("Quantity and notional cannot be null");
            }
        }
    }

    /**
     * Record que representa un punto de precio histórico.
     */
    public record PricePoint(BigDecimal price, Instant timestamp) {
        public PricePoint {
            Objects.requireNonNull(price, "price cannot be null");
            Objects.requireNonNull(timestamp, "timestamp cannot be null");
        }
    }
}

// === ARCHIVO: src/main/java/com/pragma/riskengine/model/Threshold.java ===
package com.pragma.riskengine.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Define umbrales dinámicos para circuit breakers calibrados por volatilidad.
 * Implementa ajuste automático de límites basado en condiciones de mercado.
 */
public class Threshold {

    private final String thresholdId;
    private final ThresholdType type;
    private volatile BigDecimal baseValue;
    private volatile BigDecimal currentMultiplier;
    private volatile BigDecimal minThreshold;
    private volatile BigDecimal maxThreshold;
    private volatile BigDecimal volatilityFactor;
    private volatile Instant lastAdjustmentTime;
    private final ConcurrentMap<String, BigDecimal> calibrationHistory;
    private final ReentrantReadWriteLock lock;
    private final Duration adjustmentWindow;

    public Threshold(String thresholdId, ThresholdType type, BigDecimal baseValue) {
        this.thresholdId = Objects.requireNonNull(thresholdId, "thresholdId cannot be null");
        this.type = Objects.requireNonNull(type, "type cannot be null");
        this.baseValue = Objects.requireNonNull(baseValue, "baseValue cannot be null");
        this.currentMultiplier = BigDecimal.ONE;
        this.minThreshold = baseValue.multiply(new BigDecimal("0.5"));
        this.maxThreshold = baseValue.multiply(new BigDecimal("2.0"));
        this.volatilityFactor = BigDecimal.ZERO;
        this.lastAdjustmentTime = Instant.now();
        this.calibrationHistory = new ConcurrentHashMap<>();
        this.lock = new ReentrantReadWriteLock();
        this.adjustmentWindow = Duration.ofMinutes(5);
    }

    /**
     * Ajusta el umbral basado en la volatilidad observada.
     */
    public void adjustForVolatility(BigDecimal currentVolatility, BigDecimal historicalVolatility) {
        lock.writeLock().lock();
        try {
            if (historicalVolatility.compareTo(BigDecimal.ZERO) <= 0) {
                return;
            }

            BigDecimal volatilityRatio = currentVolatility.divide(
                historicalVolatility, RoundingMode.HALF_UP
            );
            
            this.volatilityFactor = volatilityRatio;
            
            BigDecimal newMultiplier = calculateDynamicMultiplier(volatilityRatio);
            this.currentMultiplier = clampMultiplier(newMultiplier);
            
            recalculateThreshold();
            recordCalibration(currentVolatility, currentMultiplier);
            this.lastAdjustmentTime = Instant.now();
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Calcula el multiplicador dinámico basado en el ratio de volatilidad.
     */
    private BigDecimal calculateDynamicMultiplier(BigDecimal volatilityRatio) {
        return switch (type) {
            case TRADEX -> {
                if (volatilityRatio.compareTo(new BigDecimal("1.5")) > 0) {
                    yield new BigDecimal("0.7");
                } else if (volatilityRatio.compareTo(new BigDecimal("0.5")) < 0) {
                    yield new BigDecimal("1.3");
                } else {
                    yield BigDecimal.ONE;
                }
            }
            case STRATEGY -> {
                if (volatilityRatio.compareTo(new BigDecimal("2.0")) > 0) {
                    yield new BigDecimal("0.5");
                } else if (volatilityRatio.compareTo(new BigDecimal("0.75")) < 0) {
                    yield new BigDecimal("1.5");
                } else {
                    yield BigDecimal.ONE;
                }
            }
            case INSTRUMENT -> {
                if (volatilityRatio.compareTo(new BigDecimal("1.2")) > 0) {
                    yield new BigDecimal("0.6");
                } else if (volatilityRatio.compareTo(new BigDecimal("0.8")) < 0) {
                    yield new BigDecimal("1.4");
                } else {
                    yield BigDecimal.ONE;
                }
            }
            case PORTFOLIO -> {
                if (volatilityRatio.compareTo(new BigDecimal("1.8")) > 0) {
                    yield new BigDecimal("0.4");
                } else if (volatilityRatio.compareTo(new BigDecimal("0.6")) < 0) {
                    yield new BigDecimal("1.6");
                } else {
                    yield BigDecimal.ONE;
                }
            }
        };
    }

    /**
     * Limita el multiplicador a los valores mínimo y máximo permitidos.
     */
    private BigDecimal clampMultiplier(BigDecimal multiplier) {
        BigDecimal clamped = multiplier;
        if (clamped.compareTo(getMinMultiplier()) < 0) {
            clamped = getMinMultiplier();
        } else if (clamped.compareTo(getMaxMultiplier()) > 0) {
            clamped = getMaxMultiplier();
        }
        return clamped;
    }

    /**
     * Recalcula el valor del umbral con el multiplicador actual.
     */
    private void recalculateThreshold() {
        this.baseValue = baseValue;
    }

    /**
     * Registra una calibración en el historial.
     */
    private void recordCalibration(BigDecimal volatility, BigDecimal multiplier) {
        String key = Instant.now().toString();
        calibrationHistory.put(key, multiplier);
        
        if (calibrationHistory.size() > 100) {
            String oldestKey = calibrationHistory.keys().nextElement();
            calibrationHistory.remove(oldestKey);
        }
    }

    /**
     * Obtiene el valor actual del umbral con el multiplicador aplicado.
     */
    public BigDecimal getCurrentValue() {
        lock.readLock().lock();
        try {
            return baseValue.multiply(currentMultiplier).setScale(2, RoundingMode.HALF_UP);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Obtiene el valor base del umbral.
     */
    public BigDecimal getBaseValue() {
        lock.readLock().lock();
        try {
            return baseValue;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Obtiene el multiplicador actual.
     */
    public BigDecimal getCurrentMultiplier() {
        lock.readLock().lock();
        try {
            return currentMultiplier;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Obtiene el multiplicador mínimo permitido.
     */
    public BigDecimal getMinMultiplier() {
        return new BigDecimal("0.3");
    }

    /**
     * Obtiene el multiplicador máximo permitido.
     */
    public BigDecimal getMaxMultiplier() {
        return new BigDecimal("2.0");
    }

    /**
     * Obtiene el factor de volatilidad actual.
     */
    public BigDecimal getVolatilityFactor() {
        lock.readLock().lock();
        try {
            return volatilityFactor;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Obtiene el momento del último ajuste.
     */
    public Instant getLastAdjustmentTime() {
        return lastAdjustmentTime;
    }

    /**
     * Verifica si el umbral necesita recalibración.
     */
    public boolean needsRecalibration(Duration maxAge) {
        lock.readLock().lock();
        try {
            Duration age = Duration.between(lastAdjustmentTime, Instant.now());
            return age.compareTo(maxAge) > 0;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Resetea el umbral a su valor base.
     */
    public void reset() {
        lock.writeLock().lock();
        try {
            this.currentMultiplier = BigDecimal.ONE;
            this.volatilityFactor = BigDecimal.ZERO;
            this.lastAdjustmentTime = Instant.now();
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Obtiene el ID del umbral.
     */
    public String getThresholdId() {
        return thresholdId;
    }

    /**
     * Obtiene el tipo de umbral.
     */
    public ThresholdType getType() {
        return type;
    }

    /**
     * Enum que define los tipos de umbrales para diferentes niveles de riesgo.
     */
    public enum ThresholdType {
        TRADEX,
        STRATEGY,
        INSTRUMENT,
        PORTFOLIO
    }
}

// === ARCHIVO: src/main/java/com/pragma/riskengine/disruptor/OrderEvent.java ===
package com.pragma.riskengine.disruptor;

import com.pragma.riskengine.model.RiskModel;
import com.pragma.riskengine.model.Threshold;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class OrderEvent {
    private String orderId;
    private String traderId;
    private String strategyId;
    private String instrumentId;
    private OrderSide side;
    private OrderType orderType;
    private BigDecimal quantity;
    private BigDecimal limitPrice;
    private BigDecimal calculatedRisk;
    private RiskScore riskScore;
    private ProcessingStatus status;
    private Instant receivedTime;
    private Instant processedTime;
    private String rejectionReason;
    private volatile RiskModel snapshotRiskModel;
    private volatile Threshold applicableThreshold;

    public OrderEvent() {
        this.orderId = UUID.randomUUID().toString();
        this.receivedTime = Instant.now();
        this.status = ProcessingStatus.PENDING;
    }

    public void reset() {
        this.orderId = UUID.randomUUID().toString();
        this.traderId = null;
        this.strategyId = null;
        this.instrumentId = null;
        this.side = null;
        this.orderType = null;
        this.quantity = null;
        this.limitPrice = null;
        this.calculatedRisk = null;
        this.riskScore = null;
        this.status = ProcessingStatus.PENDING;
        this.receivedTime = Instant.now();
        this.processedTime = null;
        this.rejectionReason = null;
        this.snapshotRiskModel = null;
        this.applicableThreshold = null;
    }

    public void applyRiskScoring(BigDecimal risk, RiskScore score) {
        this.calculatedRisk = risk;
        this.riskScore = score;
        this.processedTime = Instant.now();
    }

    public void approve() {
        this.status = ProcessingStatus.APPROVED;
    }

    public void reject(String reason) {
        this.status = ProcessingStatus.REJECTED;
        this.rejectionReason = reason;
        this.processedTime = Instant.now();
    }

    public BigDecimal calculateNotional() {
        if (quantity == null || limitPrice == null) {
            return BigDecimal.ZERO;
        }
        return quantity.multiply(limitPrice);
    }

    public boolean isBuy() {
        return side == OrderSide.BUY;
    }

    public boolean isSell() {
        return side == OrderSide.SELL;
    }

    public boolean isLimitOrder() {
        return orderType == OrderType.LIMIT;
    }

    public boolean isMarketOrder() {
        return orderType == OrderType.MARKET;
    }

    public long getProcessingTimeNanos() {
        if (receivedTime == null || processedTime == null) {
            return 0L;
        }
        return java.time.Duration.between(receivedTime, processedTime).toNanos();
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getTraderId() { return traderId; }
    public void setTraderId(String traderId) { this.traderId = traderId; }
    public String getStrategyId() { return strategyId; }
    public void setStrategyId(String strategyId) { this.strategyId = strategyId; }
    public String getInstrumentId() { return instrumentId; }
    public void setInstrumentId(String instrumentId) { this.instrumentId = instrumentId; }
    public OrderSide getSide() { return side; }
    public void setSide(OrderSide side) { this.side = side; }
    public OrderType getOrderType() { return orderType; }
    public void setOrderType(OrderType orderType) { this.orderType = orderType; }
    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
    public BigDecimal getLimitPrice() { return limitPrice; }
    public void setLimitPrice(BigDecimal limitPrice) { this.limitPrice = limitPrice; }
    public BigDecimal getCalculatedRisk() { return calculatedRisk; }
    public void setCalculatedRisk(BigDecimal calculatedRisk) { this.calculatedRisk = calculatedRisk; }
    public RiskScore getRiskScore() { return riskScore; }
    public void setRiskScore(RiskScore riskScore) { this.riskScore = riskScore; }
    public ProcessingStatus getStatus() { return status; }
    public void setStatus(ProcessingStatus status) { this.status = status; }
    public Instant getReceivedTime() { return receivedTime; }
    public void setReceivedTime(Instant receivedTime) { this.receivedTime = receivedTime; }
    public Instant getProcessedTime() { return processedTime; }
    public void setProcessedTime(Instant processedTime) { this.processedTime = processedTime; }
    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
    public RiskModel getSnapshotRiskModel() { return snapshotRiskModel; }
    public void setSnapshotRiskModel(RiskModel snapshotRiskModel) { this.snapshotRiskModel = snapshotRiskModel; }
    public Threshold getApplicableThreshold() { return applicableThreshold; }
    public void setApplicableThreshold(Threshold applicableThreshold) { this.applicableThreshold = applicableThreshold; }

    public enum OrderSide {
        BUY, SELL
    }

    public enum OrderType {
        MARKET, LIMIT, STOP, STOP_LIMIT
    }

    public enum RiskScore {
        LOW, MEDIUM, HIGH, CRITICAL
    }

    public enum ProcessingStatus {
        PENDING, APPROVED, REJECTED, ERROR
    }
}

// === ARCHIVO: src/main/java/com/pragma/riskengine/disruptor/OrderEventHandler.java ===
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

// === ARCHIVO: src/main/java/com/pragma/riskengine/disruptor/MarketDataEventHandler.java ===
package com.pragma.riskengine.disruptor;

import com.lmax.disruptor.EventHandler;
import com.pragma.riskengine.model.RiskModel;
import com.pragma.riskengine.adapter.marketdata.MarketDataFeedAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class MarketDataEventHandler implements EventHandler<MarketDataEvent> {

    private static final Logger logger = LoggerFactory.getLogger(MarketDataEventHandler.class);
    private static final int MIN_PRICE_POINTS_FOR_VAR = 30;
    private static final BigDecimal VOLATILITY_SCALING_FACTOR = new BigDecimal("1.5");

    private final RiskModel riskModel;
    private final MarketDataFeedAdapter marketDataAdapter;
    private final AtomicLong eventsProcessed;
    private final AtomicLong lastUpdateTimestamp;

    public MarketDataEventHandler(RiskModel riskModel, MarketDataFeedAdapter marketDataAdapter) {
        this.riskModel = riskModel;
        this.marketDataAdapter = marketDataAdapter;
        this.eventsProcessed = new AtomicLong(0);
        this.lastUpdateTimestamp = new AtomicLong(0);
    }

    @Override
    public void onEvent(MarketDataEvent event, long sequence, boolean endOfBatch) throws Exception {
        processMarketDataEvent(event);
    }

    private void processMarketDataEvent(MarketDataEvent event) {
        long startTime = System.nanoTime();

        try {
            switch (event.getEventType()) {
                case PRICE_UPDATE -> handlePriceUpdate(event);
                case TRADE -> handleTrade(event);
                case ORDERBOOK_SNAPSHOT -> handleOrderBookSnapshot(event);
                case VOLATILITY_UPDATE -> handleVolatilityUpdate(event);
                default -> logger.debug("Unknown market data event type: {}", event.getEventType());
            }

            eventsProcessed.incrementAndGet();
            lastUpdateTimestamp.set(System.currentTimeMillis());

            long processingTime = System.nanoTime() - startTime;
            if (processingTime > 100_000) {
                logger.debug("Market data processing took {}us for instrument {}", 
                    processingTime / 1000, event.getInstrumentId());
            }

        } catch (Exception e) {
            logger.error("Error processing market data event for {}: {}", 
                event.getInstrumentId(), e.getMessage(), e);
        }
    }

    private void handlePriceUpdate(MarketDataEvent event) {
        String instrumentId = event.getInstrumentId();
        BigDecimal price = event.getPrice();
        Instant timestamp = event.getTimestamp();

        if (instrumentId == null || price == null) {
            logger.warn("Invalid price update event: instrument={}, price={}", 
                instrumentId, price);
            return;
        }

        riskModel.recordPrice(instrumentId, price, timestamp);

        BigDecimal currentVar = riskModel.getCurrentVar();
        if (currentVar != null) {
            logger.debug("Updated VaR for instrument {}: price={}, currentVar={}", 
                instrumentId, price, currentVar);
        }
    }

    private void handleTrade(MarketDataEvent event) {
        String instrumentId = event.getInstrumentId();
        BigDecimal price = event.getPrice();
        BigDecimal quantity = event.getQuantity();

        if (instrumentId == null || price == null || quantity == null) {
            logger.warn("Invalid trade event: instrument={}, price={}, quantity={}", 
                instrumentId, price, quantity);
            return;
        }

        riskModel.recordPrice(instrumentId, price, event.getTimestamp());

        BigDecimal notional = price.abs().multiply(quantity.abs());
        logger.debug("Processed trade for {}: quantity={}, price={}, notional={}", 
            instrumentId, quantity, price, notional);
    }

    private void handleOrderBookSnapshot(MarketDataEvent event) {
        String instrumentId = event.getInstrumentId();
        List<MarketDataEvent.PriceLevel> bidLevels = event.getBidLevels();
        List<MarketDataEvent.PriceLevel> askLevels = event.getAskLevels();

        if (bidLevels == null || askLevels == null || bidLevels.isEmpty() || askLevels.isEmpty()) {
            logger.debug("Empty orderbook snapshot for {}", instrumentId);
            return;
        }

        BigDecimal bestBid = bidLevels.get(0).price();
        BigDecimal bestAsk = askLevels.get(0).price();
        BigDecimal spread = bestAsk.subtract(bestBid);

        riskModel.recordPrice(instrumentId, bestAsk, event.getTimestamp());

        logger.debug("Orderbook snapshot for {}: bestBid={}, bestAsk={}, spread={}", 
            instrumentId, bestBid, bestAsk, spread);
    }

    private void handleVolatilityUpdate(MarketDataEvent event) {
        String instrumentId = event.getInstrumentId();
        BigDecimal volatility = event.getVolatility();

        if (instrumentId == null || volatility == null) {
            logger.warn("Invalid volatility update: instrument={}, volatility={}", 
                instrumentId, volatility);
            return;
        }

        logger.info("Volatility update for {}: {}", instrumentId, volatility);
    }

    public long getEventsProcessed() {
        return eventsProcessed.get();
    }

    public long getLastUpdateTimestamp() {
        return lastUpdateTimestamp.get();
    }

    public boolean isStale(long stalenessThresholdMs) {
        long now = System.currentTimeMillis();
        return (now - lastUpdateTimestamp.get()) > stalenessThresholdMs;
    }

    public static class MarketDataEvent {
        private String instrumentId;
        private EventType eventType;
        private BigDecimal price;
        private BigDecimal quantity;
        private Instant timestamp;
        private BigDecimal volatility;
        private List<PriceLevel> bidLevels;
        private List<PriceLevel> askLevels;

        public MarketDataEvent() {
            this.timestamp = Instant.now();
            this.eventType = EventType.PRICE_UPDATE;
        }

        public void reset() {
            this.instrumentId = null;
            this.eventType = EventType.PRICE_UPDATE;
            this.price = null;
            this.quantity = null;
            this.timestamp = Instant.now();
            this.volatility = null;
            this.bidLevels = null;
            this.askLevels = null;
        }

        public record PriceLevel(BigDecimal price, BigDecimal quantity, int orderCount) {}

        public enum EventType {
            PRICE_UPDATE, TRADE, ORDERBOOK_SNAPSHOT, VOLATILITY_UPDATE
        }

        public String getInstrumentId() { return instrumentId; }
        public void setInstrumentId(String instrumentId) { this.instrumentId = instrumentId; }
        public EventType getEventType() { return eventType; }
        public void setEventType(EventType eventType) { this.eventType = eventType; }
        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }
        public BigDecimal getQuantity() { return quantity; }
        public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
        public Instant getTimestamp() { return timestamp; }
        public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
        public BigDecimal getVolatility() { return volatility; }
        public void setVolatility(BigDecimal volatility) { this.volatility = volatility; }
        public List<PriceLevel> getBidLevels() { return bidLevels; }
        public void setBidLevels(List<PriceLevel> bidLevels) { this.bidLevels = bidLevels; }
        public List<PriceLevel> getAskLevels() { return askLevels; }
        public void setAskLevels(List<PriceLevel> askLevels) { this.askLevels = askLevels; }
    }
}

// === ARCHIVO: src/main/java/com/pragma/riskengine/service/RiskScoringService.java ===
package com.pragma.riskengine.service;

import com.pragma.riskengine.model.RiskModel;
import com.pragma.riskengine.model.Threshold;
import com.pragma.riskengine.model.Threshold.ThresholdType;
import com.pragma.riskengine.exception.RiskThresholdExceededException;
import com.pragma.riskengine.audit.AuditLogger;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class RiskScoringService {
    private static final Logger logger = LoggerFactory.getLogger(RiskScoringService.class);
    private static final BigDecimal DEFAULT_CONFIDENCE_LEVEL = new BigDecimal("0.99");
    private static final int DEFAULT_LOOKBACK_PERIODS = 252;
    private static final BigDecimal CIRCUIT_BREAKER_FAILURE_RATE_THRESHOLD = new BigDecimal("0.5");
    private static final int CIRCUIT_BREAKER_WAIT_DURATION_SECONDS = 30;
    private static final BigDecimal MAX_EXPOSURE_PER_ORDER = new BigDecimal("1000000");
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
    private final Object recalculationLock = new Object();

    public RiskScoringService(AuditLogger auditLogger) {
        this.auditLogger = auditLogger;
        this.riskModel = new RiskModel(DEFAULT_CONFIDENCE_LEVEL, DEFAULT_LOOKBACK_PERIODS);
        this.scheduler = Executors.newScheduledThreadPool(2, r -> {
            Thread t = new Thread(r, "risk-scheduler");
            t.setDaemon(true);
            return t;
        });
        this.circuitBreakersByStrategy = new ConcurrentHashMap<>();
        this.thresholdsByInstrument = new ConcurrentHashMap<>();
        this.dailyPnLByTrader = new ConcurrentHashMap<>();
        this.enableCircuitBreakers = true;
        initializeCircuitBreakerRegistry();
        initializeRetryRegistry();
        startRiskRecalculationTask();
    }

    private void initializeCircuitBreakerRegistry() {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .failureRateThreshold(CIRCUIT_BREAKER_FAILURE_RATE_THRESHOLD.floatValue())
                .waitDurationInOpenState(Duration.ofSeconds(CIRCUIT_BREAKER_WAIT_DURATION_SECONDS))
                .slidingWindowSize(100)
                .minimumNumberOfCalls(10)
                .permittedNumberOfCallsInHalfOpenState(3)
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .recordExceptions(java.io.IOException.class, java.util.concurrent.TimeoutException.class)
                .build();
        this.circuitBreakerRegistry = CircuitBreakerRegistry.of(config);
    }

    private void initializeRetryRegistry() {
        RetryConfig retryConfig = RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofMillis(100))
                .retryExceptions(Exception.class)
                .build();
        this.retryRegistry = RetryRegistry.of(retryConfig);
    }

    private void startRiskRecalculationTask() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                recalculateRiskMetrics();
            } catch (Exception e) {
                logger.error("Error en recalculación de métricas de riesgo", e);
            }
        }, 1, 1, TimeUnit.MINUTES);
    }

    public RiskScoreResult calculateRiskScore(OrderRequest order) {
        validateOrderRequest(order);
        checkKillSwitchStatus(order.strategyId());
        BigDecimal orderNotional = order.quantity().multiply(order.price());
        validateExposureLimits(order, orderNotional);
        BigDecimal var = calculateVarWithCircuitBreaker(order.instrumentId());
        BigDecimal orderRiskContribution = calculateOrderRiskContribution(order, var);
        BigDecimal adjustedThreshold = getAdjustedThreshold(order.instrumentId());
        boolean riskApproved = orderRiskContribution.compareTo(adjustedThreshold) <= 0;
        RiskScoreResult result = new RiskScoreResult(
                order.orderId(),
                orderRiskContribution,
                adjustedThreshold,
                riskApproved,
                var,
                Instant.now()
        );
        auditLogger.logRiskDecision(order, result);
        if (!riskApproved) {
            throw new RiskThresholdExceededException(
                    "Orden " + order.orderId() + " excede el threshold de riesgo: " 
                    + orderRiskContribution + " > " + adjustedThreshold
            );
        }
        updateRiskModel(order, orderNotional);
        return result;
    }

    private void validateOrderRequest(OrderRequest order) {
        if (order == null || order.orderId() == null || order.orderId().isBlank()) {
            throw new IllegalArgumentException("Orden inválida: orderId requerido");
        }
        if (order.quantity() == null || order.quantity().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Cantidad debe ser mayor a cero");
        }
        if (order.price() == null || order.price().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Precio debe ser mayor a cero");
        }
    }

    private void checkKillSwitchStatus(String strategyId) {
        if (!enableCircuitBreakers) {
            logger.warn("Circuit breakers deshabilitados globalmente");
        }
        CircuitBreaker cb = circuitBreakersByStrategy.get(strategyId);
        if (cb != null && cb.getState() == CircuitBreaker.State.OPEN) {
            throw new RiskThresholdExceededException(
                    "Circuit breaker abierto para estrategia: " + strategyId
            );
        }
    }

    private void validateExposureLimits(OrderRequest order, BigDecimal orderNotional) {
        if (orderNotional.compareTo(MAX_EXPOSURE_PER_ORDER) > 0) {
            throw new RiskThresholdExceededException(
                    "Exposición por orden excede el máximo: " + orderNotional
            );
        }
        BigDecimal traderExposure = riskModel.calculateExposureByTrader(order.traderId());
        BigDecimal newTraderExposure = traderExposure.add(orderNotional);
        if (newTraderExposure.compareTo(getMaxExposureForTrader(order.traderId())) > 0) {
            throw new RiskThresholdExceededException(
                    "Exposición total del trader excede el límite: " + newTraderExposure
            );
        }
    }

    private BigDecimal getMaxExposureForTrader(String traderId) {
        return new BigDecimal("5000000");
    }

    private BigDecimal calculateVarWithCircuitBreaker(String instrumentId) {
        CircuitBreaker circuitBreaker = getOrCreateCircuitBreaker(instrumentId);
        Supplier<BigDecimal> decoratedSupplier = CircuitBreaker.decorateSupplier(
                circuitBreaker,
                () -> riskModel.calculateVar()
        );
        try {
            return decoratedSupplier.get();
        } catch (Exception e) {
            logger.warn("Circuit breaker activado para cálculo de VaR: {}", e.getMessage());
            return getFallbackVar();
        }
    }

    private CircuitBreaker getOrCreateCircuitBreaker(String instrumentId) {
        return circuitBreakersByStrategy.computeIfAbsent(
                instrumentId,
                id -> {
                    CircuitBreaker cb = circuitBreakerRegistry.circuitBreaker(id);
                    cb.getEventPublisher()
                            .onStateTransition(event -> logger.warn(
                                    "Circuit breaker transición: {} -> {} para {}",
                                    event.getStateTransition().getFromState(),
                                    event.getStateTransition().getToState(),
                                    id
                            ));
                    return cb;
                }
        );
    }

    private BigDecimal getFallbackVar() {
        return new BigDecimal("100000");
    }

    private BigDecimal calculateOrderRiskContribution(OrderRequest order, BigDecimal currentVar) {
        BigDecimal orderNotional = order.quantity().multiply(order.price());
        BigDecimal portfolioExposure = riskModel.calculateTotalExposure();
        if (portfolioExposure.compareTo(BigDecimal.ZERO) == 0) {
            return orderNotional;
        }
        BigDecimal riskContribution = orderNotional
                .divide(portfolioExposure, 10, RoundingMode.HALF_UP)
                .multiply(currentVar);
        return riskContribution.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal getAdjustedThreshold(String instrumentId) {
        Threshold threshold = thresholdsByInstrument.computeIfAbsent(
                instrumentId,
                id -> new Threshold(id, ThresholdType.VOLATILITY_BASED, new BigDecimal("50000"))
        );
        BigDecimal currentVolatility = estimateCurrentVolatility(instrumentId);
        BigDecimal historicalVolatility = estimateHistoricalVolatility(instrumentId);
        threshold.adjustForVolatility(currentVolatility, historicalVolatility);
        return threshold.getCurrentValue();
    }

    private BigDecimal estimateCurrentVolatility(String instrumentId) {
        return new BigDecimal("0.02");
    }

    private BigDecimal estimateHistoricalVolatility(String instrumentId) {
        return new BigDecimal("0.015");
    }

    private void updateRiskModel(OrderRequest order, BigDecimal orderNotional) {
        riskModel.updatePosition(
                order.instrumentId(),
                order.quantity(),
                orderNotional
        );
        riskModel.updateTraderExposure(order.traderId(), orderNotional);
        riskModel.updateStrategyExposure(order.strategyId(), orderNotional);
    }

    private void recalculateRiskMetrics() {
        synchronized (recalculationLock) {
            logger.info("Recalculando métricas de riesgo");
            BigDecimal var = riskModel.calculateVar();
            riskModel.reset();
            lastRiskRecalculation = Instant.now();
            logger.info("VaR recalculado: {}, Timestamp: {}", var, lastRiskRecalculation);
        }
    }

    public void recordMarketDataUpdate(String instrumentId, BigDecimal price, Instant timestamp) {
        riskModel.recordPrice(instrumentId, price, timestamp);
    }

    public void recordTrade(String instrumentId, BigDecimal quantity, BigDecimal price, 
                           String traderId, String side) {
        BigDecimal notional = quantity.abs().multiply(price);
        if ("SELL".equalsIgnoreCase(side)) {
            notional = notional.negate();
        }
        BigDecimal currentPnL = dailyPnLByTrader.getOrDefault(traderId, BigDecimal.ZERO);
        dailyPnLByTrader.put(traderId, currentPnL.add(notional));
        if (currentPnL.add(notional).compareTo(MAX_DAILY_LOSS.negate()) < 0) {
            logger.warn("Trader {} alcanzó el límite de pérdida diaria: {}", traderId, 
                       currentPnL.add(notional));
        }
    }

    public void enableCircuitBreakers(boolean enable) {
        this.enableCircuitBreakers = enable;
        logger.info("Circuit breakers {} globalmente", enable ? "habilitados" : "deshabilitados");
    }

    public RiskModel getRiskModel() {
        return riskModel;
    }

    public BigDecimal getCurrentVar() {
        return riskModel.getCurrentVar();
    }

    public Map<String, BigDecimal> getDailyPnLByTrader() {
        return Map.copyOf(dailyPnLByTrader);
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

    public record OrderRequest(
            String orderId,
            String traderId,
            String strategyId,
            String instrumentId,
            BigDecimal quantity,
            BigDecimal price,
            String side,
            Instant timestamp
    ) {}

    public record RiskScoreResult(
            String orderId,
            BigDecimal riskScore,
            BigDecimal threshold,
            boolean approved,
            BigDecimal varAtCalculation,
            Instant calculatedAt
    ) {}
}

// === ARCHIVO: src/main/java/com/pragma/riskengine/service/KillSwitchService.java ===
package com.pragma.riskengine.service;

import com.pragma.riskengine.audit.AuditLogger;
import com.pragma.riskengine.exception.KillSwitchActivatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class KillSwitchService {
    private static final Logger logger = LoggerFactory.getLogger(KillSwitchService.class);
    private static final int DEFAULT_CONSECUTIVE_FAILURES_THRESHOLD = 5;
    private static final int DEFAULT_ERROR_RATE_WINDOW = 100;
    private static final BigDecimal DEFAULT_ERROR_RATE_THRESHOLD = new BigDecimal("0.3");
    private static final Duration DEFAULT_RESET_DURATION = Duration.ofMinutes(5);
    private static final int MAX_CONSECUTIVE_PINGS_FAILURES = 3;

    private final AuditLogger auditLogger;
    private final ScheduledExecutorService scheduler;
    private final Map<String, StrategyKillSwitch> strategyKillSwitches;
    private final Map<String, AtomicInteger> consecutiveFailuresByStrategy;
    private final Map<String, AtomicInteger> errorCountByStrategy;
    private final Map<String, Instant> lastErrorTimestampByStrategy;
    private final AtomicBoolean globalKillSwitchActive;
    private volatile boolean enableAutoRecovery;
    private volatile Instant lastGlobalCheck;
    private final Object stateLock = new Object();

    public KillSwitchService(AuditLogger auditLogger) {
        this.auditLogger = auditLogger;
        this.scheduler = Executors.newScheduledThreadPool(1, r -> {
            Thread t = new Thread(r, "killswitch-monitor");
            t.setDaemon(true);
            return t;
        });
        this.strategyKillSwitches = new ConcurrentHashMap<>();
        this.consecutiveFailuresByStrategy = new ConcurrentHashMap<>();
        this.errorCountByStrategy = new ConcurrentHashMap<>();
        this.lastErrorTimestampByStrategy = new ConcurrentHashMap<>();
        this.globalKillSwitchActive = new AtomicBoolean(false);
        this.enableAutoRecovery = true;
        startMonitoringTasks();
    }

    private void startMonitoringTasks() {
        scheduler.scheduleAtFixedRate(this::performGlobalHealthCheck, 
                30, 30, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(this::cleanupStaleErrorCounts,
                60, 60, TimeUnit.SECONDS);
    }

    public void recordFailure(String strategyId, String reason) {
        AtomicInteger consecutive = consecutiveFailuresByStrategy.computeIfAbsent(
                strategyId,
                k -> new AtomicInteger(0)
        );
        int failures = consecutive.incrementAndGet();
        lastErrorTimestampByStrategy.put(strategyId, Instant.now());
        AtomicInteger errorCount = errorCountByStrategy.computeIfAbsent(
                strategyId,
                k -> new AtomicInteger(0)
        );
        errorCount.incrementAndGet();
        logger.warn("Fallo registrado para estrategia {}: {} (consecutivos: {})", 
                   strategyId, reason, failures);
        auditLogger.logKillSwitchEvent(strategyId, "FAILURE_RECORDED", 
                Map.of("reason", reason, "consecutiveFailures", String.valueOf(failures)));
        if (failures >= DEFAULT_CONSECUTIVE_FAILURES_THRESHOLD) {
            activateKillSwitch(strategyId, "CONSECUTIVE_FAILURES_THRESHOLD_EXCEEDED");
        }
        checkErrorRateThreshold(strategyId);
    }

    public void recordSuccess(String strategyId) {
        AtomicInteger consecutive = consecutiveFailuresByStrategy.get(strategyId);
        if (consecutive != null && consecutive.get() > 0) {
            consecutive.set(0);
            logger.info("Éxito registrado para estrategia {}, reiniciando contador de fallos", strategyId);
        }
    }

    private void checkErrorRateThreshold(String strategyId) {
        AtomicInteger errorCount = errorCountByStrategy.get(strategyId);
        if (errorCount == null) {
            return;
        }
        int errors = errorCount.get();
        if (errors >= DEFAULT_ERROR_RATE_WINDOW) {
            BigDecimal errorRate = BigDecimal.valueOf(errors)
                    .divide(BigDecimal.valueOf(DEFAULT_ERROR_RATE_WINDOW), 4, 
                           java.math.RoundingMode.HALF_UP);
            if (errorRate.compareTo(DEFAULT_ERROR_RATE_THRESHOLD) > 0) {
                activateKillSwitch(strategyId, "ERROR_RATE_THRESHOLD_EXCEEDED");
            }
            errorCount.set(0);
        }
    }

    public void activateKillSwitch(String strategyId, String reason) {
        synchronized (stateLock) {
            StrategyKillSwitch existing = strategyKillSwitches.get(strategyId);
            if (existing != null && existing.active()) {
                logger.debug("Kill switch ya activo para estrategia: {}", strategyId);
                return;
            }
            StrategyKillSwitch killSwitch = new StrategyKillSwitch(
                    strategyId,
                    reason,
                    Instant.now(),
                    true
            );
            strategyKillSwitches.put(strategyId, killSwitch);
            logger.error("KILL SWITCH ACTIVADO para estrategia {}: {}", strategyId, reason);
            auditLogger.logKillSwitchEvent(strategyId, "ACTIVATED", 
                    Map.of("reason", reason, "timestamp", Instant.now().toString()));
        }
    }

    public void deactivateKillSwitch(String strategyId) {
        synchronized (stateLock) {
            StrategyKillSwitch killSwitch = strategyKillSwitches.get(strategyId);
            if (killSwitch == null || !killSwitch.active()) {
                logger.debug("Kill switch no activo para estrategia: {}", strategyId);
                return;
            }
            StrategyKillSwitch deactivated = new StrategyKillSwitch(
                    strategyId,
                    killSwitch.reason(),
                    killSwitch.activatedAt(),
                    false
            );
            strategyKillSwitches.put(strategyId, deactivated);
            consecutiveFailuresByStrategy.get(strategyId).set(0);
            logger.info("KILL SWITCH DESACTIVADO para estrategia {}", strategyId);
            auditLogger.logKillSwitchEvent(strategyId, "DEACTIVATED", 
                    Map.of("previousReason", killSwitch.reason()));
        }
    }

    public boolean isKillSwitchActive(String strategyId) {
        if (globalKillSwitchActive.get()) {
            logger.warn("Kill switch global activo, todas las estrategias bloqueadas");
            return true;
        }
        StrategyKillSwitch killSwitch = strategyKillSwitches.get(strategyId);
        return killSwitch != null && killSwitch.active();
    }

    public void activateGlobalKillSwitch(String reason) {
        globalKillSwitchActive.set(true);
        logger.error("KILL SWITCH GLOBAL ACTIVADO: {}", reason);
        auditLogger.logKillSwitchEvent("GLOBAL", "GLOBAL_ACTIVATED", 
                Map.of("reason", reason, "timestamp", Instant.now().toString()));
    }

    public void deactivateGlobalKillSwitch() {
        globalKillSwitchActive.set(false);
        logger.info("KILL SWITCH GLOBAL DESACTIVADO");
        auditLogger.logKillSwitchEvent("GLOBAL", "GLOBAL_DEACTIVATED", 
                Map.of("timestamp", Instant.now().toString()));
    }

    private void performGlobalHealthCheck() {
        lastGlobalCheck = Instant.now();
        int activeCount = (int) strategyKillSwitches.values().stream()
                .filter(StrategyKillSwitch::active)
                .count();
        if (activeCount > 0) {
            logger.info("Estado de kill switches: {} estrategias con kill switch activo", activeCount);
        }
        if (enableAutoRecovery) {
            checkAutoRecoveryConditions();
        }
    }

    private void checkAutoRecoveryConditions() {
        Instant now = Instant.now();
        for (Map.Entry<String, StrategyKillSwitch> entry : strategyKillSwitches.entrySet()) {
            StrategyKillSwitch ks = entry.getValue();
            if (ks.active()) {
                Duration activeDuration = Duration.between(ks.activatedAt(), now);
                if (activeDuration.compareTo(DEFAULT_RESET_DURATION) > 0) {
                    logger.info("Intentando recuperación automática para estrategia: {}", entry.getKey());
                    attemptAutoRecovery(entry.getKey());
                }
            }
        }
    }

    private void attemptAutoRecovery(String strategyId) {
        StrategyKillSwitch ks = strategyKillSwitches.get(strategyId);
        if (ks == null || !ks.active()) {
            return;
        }
        Duration timeActive = Duration.between(ks.activatedAt(), Instant.now());
        if (timeActive.compareTo(DEFAULT_RESET_DURATION) > 0) {
            logger.info("Auto-recuperación exitosa para estrategia: {}", strategyId);
            deactivateKillSwitch(strategyId);
            auditLogger.logKillSwitchEvent(strategyId, "AUTO_RECOVERED", 
                    Map.of("previousReason", ks.reason()));
        }
    }

    private void cleanupStaleErrorCounts() {
        Instant threshold = Instant.now().minus(Duration.ofMinutes(10));
        for (Map.Entry<String, Instant> entry : lastErrorTimestampByStrategy.entrySet()) {
            if (entry.getValue().isBefore(threshold)) {
                errorCountByStrategy.get(entry.getKey()).set(0);
                logger.debug("Contador de errores limpiado para estrategia: {}", entry.getKey());
            }
        }
    }

    public Map<String, StrategyKillSwitch> getActiveKillSwitches() {
        return strategyKillSwitches.entrySet().stream()
                .filter(e -> e.getValue().active())
                .collect(java.util.stream.Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
    }

    public boolean isGlobalKillSwitchActive() {
        return globalKillSwitchActive.get();
    }

    public void setEnableAutoRecovery(boolean enable) {
        this.enableAutoRecovery = enable;
        logger.info("Auto-recuperación {}", enable ? "habilitada" : "deshabilitada");
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

    public record StrategyKillSwitch(
            String strategyId,
            String reason,
            Instant activatedAt,
            boolean active
    ) {}
}

// === ARCHIVO: src/main/java/com/pragma/riskengine/adapter/marketdata/MarketDataFeedAdapter.java ===
package com.pragma.riskengine.adapter.marketdata;

import com.pragma.riskengine.audit.AuditLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class MarketDataFeedAdapter {
    private static final Logger logger = LoggerFactory.getLogger(MarketDataFeedAdapter.class);
    private static final int MAX_ORDERBOOK_DEPTH = 10;
    private static final Duration RECONNECT_DELAY = Duration.ofSeconds(5);
    private static final int MAX_RECONNECT_ATTEMPTS = 3;
    private static final BigDecimal DEFAULT_SPREAD = new BigDecimal("0.01");

    private final AuditLogger auditLogger;
    private final ScheduledExecutorService scheduler;
    private final Map<String, OrderBook> orderBooksByInstrument;
    private final Map<String, List<Trade>> recentTradesByInstrument;
    private final Map<String, AtomicLong> sequenceNumbersByInstrument;
    private volatile boolean connected;
    private volatile Instant lastHeartbeat;
    private volatile FeedConnectionStatus connectionStatus;
    private final Object feedLock = new Object();
    private int reconnectAttempts;
    private MarketDataListener listener;

    public MarketDataFeedAdapter(AuditLogger auditLogger) {
        this.auditLogger = auditLogger;
        this.scheduler = Executors.newScheduledThreadPool(2, r -> {
            Thread t = new Thread(r, "marketdata-feed");
            t.setDaemon(true);
            return t;
        });
        this.orderBooksByInstrument = new ConcurrentHashMap<>();
        this.recentTradesByInstrument = new ConcurrentHashMap<>();
        this.sequenceNumbersByInstrument = new ConcurrentHashMap<>();
        this.connected = false;
        this.connectionStatus = FeedConnectionStatus.DISCONNECTED;
        initializeFeedConnection();
        startHeartbeatMonitor();
    }

    private void initializeFeedConnection() {
        scheduler.submit(() -> {
            logger.info("Inicializando conexión al feed de market data");
            simulateConnection();
        });
    }

    private void simulateConnection() {
        synchronized (feedLock) {
            try {
                Thread.sleep(100);
                connected = true;
                connectionStatus = FeedConnectionStatus.CONNECTED;
                logger.info("Conexión al feed de market data establecida");
                auditLogger.logMarketDataEvent("FEED_CONNECTED", 
                        Map.of("timestamp", Instant.now().toString()));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                connectionStatus = FeedConnectionStatus.ERROR;
            }
        }
    }

    private void startHeartbeatMonitor() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                checkFeedHealth();
            } catch (Exception e) {
                logger.error("Error en verificación de salud del feed", e);
            }
        }, 10, 10, TimeUnit.SECONDS);
    }

    private void checkFeedHealth() {
        if (!connected) {
            logger.warn("Feed desconectado, intentando reconectar");
            attemptReconnect();
            return;
        }
        Instant now = Instant.now();
        if (lastHeartbeat != null && Duration.between(lastHeartbeat, now).getSeconds() > 30) {
            logger.warn("Heartbeat del feed expirado");
            handleFeedDisconnection();
        }
    }

    private void attemptReconnect() {
        if (reconnectAttempts >= MAX_RECONNECT_ATTEMPTS) {
            logger.error("Máximo de intentos de reconexión alcanzado");
            connectionStatus = FeedConnectionStatus.FAILED;
            return;
        }
        reconnectAttempts++;
        logger.info("Intentando reconexión al feed (intento {}/{})", 
                   reconnectAttempts, MAX_RECONNECT_ATTEMPTS);
        scheduler.schedule(this::simulateConnection, 
                RECONNECT_DELAY.toMillis(), TimeUnit.MILLISECONDS);
    }

    private void handleFeedDisconnection() {
        connected = false;
        connectionStatus = FeedConnectionStatus.DISCONNECTED;
        logger.error("Feed de market data desconectado");
        auditLogger.logMarketDataEvent("FEED_DISCONNECTED",
                Map.of("timestamp", Instant.now().toString()));
        attemptReconnect();
    }

    public void subscribeToInstrument(String instrumentId) {
        if (!connected) {
            logger.warn("No se puede suscribir a {}: feed desconectado", instrumentId);
            return;
        }
        orderBooksByInstrument.putIfAbsent(instrumentId, new OrderBook(instrumentId));
        recentTradesByInstrument.putIfAbsent(instrumentId, new ArrayList<>());
        sequenceNumbersByInstrument.putIfAbsent(instrumentId, new AtomicLong(0));
        logger.info("Suscrito a instrument: {}", instrumentId);
        auditLogger.logMarketDataEvent("SUBSCRIBED", 
                Map.of("instrumentId", instrumentId));
    }

    public void unsubscribeFromInstrument(String instrumentId) {
        orderBooksByInstrument.remove(instrumentId);
        recentTradesByInstrument.remove(instrumentId);
        sequenceNumbersByInstrument.remove(instrumentId);
        logger.info("Desuscrito de instrument: {}", instrumentId);
    }

    public void processOrderBookUpdate(OrderBookUpdate update) {
        if (!connected) {
            logger.debug("Ignorando actualización de orderbook: feed desconectado");
            return;
        }
        OrderBook orderBook = orderBooksByInstrument.get(update.instrumentId());
        if (orderBook == null) {
            logger.warn("Orderbook no encontrado para instrument: {}", update.instrumentId());
            return;
        }
        for (OrderBookLevel level : update.bids()) {
            orderBook.updateBid(level.price(), level.quantity());
        }
        for (OrderBookLevel level : update.asks()) {
            orderBook.updateAsk(level.price(), level.quantity());
        }
        AtomicLong seq = sequenceNumbersByInstrument.get(update.instrumentId());
        if (seq != null) {
            seq.incrementAndGet();
        }
        lastHeartbeat = Instant.now();
        if (listener != null) {
            listener.onOrderBookUpdate(update.instrumentId(), orderBook);
        }
    }

    public void processTrade(Trade trade) {
        if (!connected) {
            logger.debug("Ignorando trade: feed desconectado");
            return;
        }
        List<Trade> trades = recentTradesByInstrument.get(trade.instrumentId());
        if (trades == null) {
            logger.warn("No hay registro de trades para instrument: {}", trade.instrumentId());
            return;
        }
        synchronized (trades) {
            trades.add(trade);
            if (trades.size() > 1000) {
                trades.remove(0);
            }
        }
        lastHeartbeat = Instant.now();
        auditLogger.logMarketDataEvent("TRADE_RECEIVED",
                Map.of(
                        "instrumentId", trade.instrumentId(),
                        "price", trade.price().toString(),
                        "quantity", trade.quantity().toString()
                ));
        if (listener != null) {
            listener.onTrade(trade);
        }
    }

    public OrderBook getOrderBook(String instrumentId) {
        return orderBooksByInstrument.get(instrumentId);
    }

    public BigDecimal getMidPrice(String instrumentId) {
        OrderBook ob = orderBooksByInstrument.get(instrumentId);
        if (ob == null) {
            return null;
        }
        BigDecimal bestBid = ob.getBestBid();
        BigDecimal bestAsk = ob.getBestAsk();
        if (bestBid == null || bestAsk == null) {
            return null;
        }
        return bestBid.add(bestAsk).divide(BigDecimal.valueOf(2), 
                java.math.RoundingMode.HALF_UP);
    }

    public BigDecimal getSpread(String instrumentId) {
        OrderBook ob = orderBooksByInstrument.get(instrumentId);
        if (ob == null) {
            return DEFAULT_SPREAD;
        }
        BigDecimal bestBid = ob.getBestBid();
        BigDecimal bestAsk = ob.getBestAsk();
        if (bestBid == null || bestAsk == null) {
            return DEFAULT_SPREAD;
        }
        return bestAsk.subtract(bestBid);
    }

    public List<Trade> getRecentTrades(String instrumentId, int limit) {
        List<Trade> trades = recentTradesByInstrument.get(instrumentId);
        if (trades == null) {
            return List.of();
        }
        synchronized (trades) {
            int size = trades.size();
            int fromIndex = Math.max(0, size - limit);
            return new ArrayList<>(trades.subList(fromIndex, size));
        }
    }

    public void setListener(MarketDataListener listener) {
        this.listener = listener;
    }

    public boolean isConnected() {
        return connected;
    }

    public FeedConnectionStatus getConnectionStatus() {
        return connectionStatus;
    }

    public void shutdown() {
        connected = false;
        connectionStatus = FeedConnectionStatus.SHUTDOWN;
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
        logger.info("MarketDataFeedAdapter detenido");
    }

    public interface MarketDataListener {
        void onOrderBookUpdate(String instrumentId, OrderBook orderBook);
        void onTrade(Trade trade);
    }

    public record OrderBookUpdate(
            String instrumentId,
            List<OrderBookLevel> bids,
            List<OrderBookLevel> asks,
            long sequenceNumber,
            Instant timestamp
    ) {}

    public record OrderBookLevel(BigDecimal price, BigDecimal quantity) {}

    public record Trade(
            String instrumentId,
            BigDecimal price,
            BigDecimal quantity,
            String side,
            Instant timestamp,
            String tradeId
    ) {}

    public static class OrderBook {
        private final String instrumentId;
        private final ConcurrentHashMap<BigDecimal, BigDecimal> bids;
        private final ConcurrentHashMap<BigDecimal, BigDecimal> asks;

        public OrderBook(String instrumentId) {
            this.instrumentId = instrumentId;
            this.bids = new ConcurrentHashMap<>();
            this.asks = new ConcurrentHashMap<>();
        }

        public void updateBid(BigDecimal price, BigDecimal quantity) {
            if (quantity.compareTo(BigDecimal.ZERO) == 0) {
                bids.remove(price);
            } else {
                bids.put(price, quantity);
            }
        }

        public void updateAsk(BigDecimal price, BigDecimal quantity) {
            if (quantity.compareTo(BigDecimal.ZERO) == 0) {
                asks.remove(price);
            } else {
                asks.put(price, quantity);
            }
        }

        public BigDecimal getBestBid() {
            return bids.keySet().stream()
                    .max(BigDecimal::compareTo)
                    .orElse(null);
        }

        public BigDecimal getBestAsk() {
            return asks.keySet().stream()
                    .min(BigDecimal::compareTo)
                    .orElse(null);
        }

        public List<OrderBookLevel> getTopBids(int depth) {
            return bids.entrySet().stream()
                    .sorted(Map.Entry.<BigDecimal, BigDecimal>comparingByKey().reversed())
                    .limit(depth)
                    .map(e -> new OrderBookLevel(e.getKey(), e.getValue()))
                    .toList();
        }

        public List<OrderBookLevel> getTopAsks(int depth) {
            return asks.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .limit(depth)
                    .map(e -> new OrderBookLevel(e.getKey(), e.getValue()))
                    .toList();
        }

        public String getInstrumentId() {
            return instrumentId;
        }

        public void clear() {
            bids.clear();
            asks.clear();
        }
    }

    public enum FeedConnectionStatus {
        DISCONNECTED,
        CONNECTING,
        CONNECTED,
        RECONNECTING,
        ERROR,
        SHUTDOWN
    }
}

// === ARCHIVO: src/main/java/com/pragma/riskengine/adapter/exchange/ExchangeAdapter.java ===
package com.pragma.riskengine.adapter.exchange;

import com.pragma.riskengine.model.Threshold;
import com.pragma.riskengine.model.Threshold.ThresholdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class ExchangeAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ExchangeAdapter.class);
    private static final int DEFAULT_TIMEOUT_MS = 5000;
    private static final int MAX_PENDING_ORDERS = 10000;

    private final ExecutorService orderExecutor;
    private final ConcurrentHashMap<String, PendingOrder> pendingOrders;
    private final BlockingQueue<OrderResponse> responseQueue;
    private final AtomicBoolean isConnected;
    private final AtomicLong orderSequence;
    private final CircuitBreakerAdapter circuitBreaker;
    private final ResilienceConfig resilienceConfig;

    public ExchangeAdapter(ResilienceConfig resilienceConfig) {
        this.resilienceConfig = resilienceConfig;
        this.orderExecutor = Executors.newVirtualThreadPerTaskExecutor();
        this.pendingOrders = new ConcurrentHashMap<>();
        this.responseQueue = new LinkedBlockingQueue<>(MAX_PENDING_ORDERS);
        this.isConnected = new AtomicBoolean(false);
        this.orderSequence = new AtomicLong(0);
        this.circuitBreaker = new CircuitBreakerAdapter(resilienceConfig);
    }

    public String sendOrder(String orderId, String traderId, String strategyId,
            String instrumentId, String side, BigDecimal quantity, BigDecimal price) {
        if (!isConnected.get()) {
            throw new ExchangeNotConnectedException("Exchange connection is not established");
        }

        String sequenceId = generateSequenceId();
        long sequence = orderSequence.incrementAndGet();

        logger.info("Sending order to exchange: orderId={}, sequence={}, instrument={}, side={}, qty={}, price={}",
                orderId, sequence, instrumentId, side, quantity, price);

        PendingOrder pending = new PendingOrder(orderId, sequenceId, traderId, strategyId,
                instrumentId, side, quantity, price, Instant.now());
        pendingOrders.put(sequenceId, pending);

        CompletableFuture<OrderResponse> future = CompletableFuture.supplyAsync(() -> {
            return executeOrderWithResilience(pending);
        }, orderExecutor);

        future.thenAccept(response -> {
            pendingOrders.remove(sequenceId);
            responseQueue.offer(response);
            logger.info("Order response received: orderId={}, status={}, sequence={}",
                    orderId, response.status(), sequence);
        });

        return sequenceId;
    }

    private OrderResponse executeOrderWithResilience(PendingOrder order) {
        if (circuitBreaker.isOpen()) {
            logger.warn("Circuit breaker is OPEN, rejecting order: {}", order.orderId());
            return new OrderResponse(order.orderId(), order.sequenceId(),
                    OrderStatus.REJECTED, "Circuit breaker open", Instant.now());
        }

        try {
            return resilienceConfig.executeWithRetry(() -> {
                return submitToExchange(order);
            });
        } catch (Exception e) {
            logger.error("Order execution failed after retries: orderId={}, error={}",
                    order.orderId(), e.getMessage());
            circuitBreaker.recordFailure();
            return new OrderResponse(order.orderId(), order.sequenceId(),
                    OrderStatus.REJECTED, e.getMessage(), Instant.now());
        }
    }

    private OrderResponse submitToExchange(PendingOrder order) {
        try {
            Thread.sleep(10);
            circuitBreaker.recordSuccess();
            return new OrderResponse(order.orderId(), order.sequenceId(),
                    OrderStatus.ACCEPTED, "Order accepted by exchange", Instant.now());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new OrderResponse(order.orderId(), order.sequenceId(),
                    OrderStatus.ERROR, "Order submission interrupted", Instant.now());
        }
    }

    public OrderResponse waitForResponse(long timeoutMs) throws InterruptedException {
        OrderResponse response = responseQueue.poll(timeoutMs, TimeUnit.MILLISECONDS);
        if (response == null) {
            logger.warn("Timeout waiting for order response after {}ms", timeoutMs);
        }
        return response;
    }

    public boolean checkConnectivity() {
        try {
            return isConnected.get();
        } catch (Exception e) {
            logger.error("Connectivity check failed: {}", e.getMessage());
            return false;
        }
    }

    public void connect() {
        logger.info("Connecting to exchange...");
        isConnected.set(true);
        logger.info("Connected to exchange successfully");
    }

    public void disconnect() {
        logger.info("Disconnecting from exchange...");
        isConnected.set(false);
        pendingOrders.clear();
        logger.info("Disconnected from exchange");
    }

    private String generateSequenceId() {
        return UUID.randomUUID().toString();
    }

    public int getPendingOrdersCount() {
        return pendingOrders.size();
    }

    public void shutdown() {
        disconnect();
        orderExecutor.shutdown();
        try {
            if (!orderExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                orderExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            orderExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public record PendingOrder(String orderId, String sequenceId, String traderId,
            String strategyId, String instrumentId, String side,
            BigDecimal quantity, BigDecimal price, Instant submittedAt) {
    }

    public record OrderResponse(String orderId, String sequenceId, OrderStatus status,
            String message, Instant timestamp) {
    }

    public enum OrderStatus {
        ACCEPTED, REJECTED, FILLED, PARTIALLY_FILLED, CANCELLED, ERROR
    }

    public static class ExchangeNotConnectedException extends RuntimeException {
        public ExchangeNotConnectedException(String message) {
            super(message);
        }
    }

    private static class CircuitBreakerAdapter {
        private final AtomicBoolean isOpen = new AtomicBoolean(false);
        private final AtomicLong failureCount = new AtomicLong(0);
        private final AtomicLong successCount = new AtomicLong(0);
        private final ResilienceConfig config;
        private volatile Instant lastFailureTime;

        public CircuitBreakerAdapter(ResilienceConfig config) {
            this.config = config;
        }

        public boolean isOpen() {
            if (failureCount.get() >= config.getFailureThreshold()) {
                if (lastFailureTime != null &&
                        Duration.between(lastFailureTime, Instant.now()).toMillis() < config.getResetTimeoutMs()) {
                    return true;
                } else {
                    failureCount.set(0);
                    successCount.set(0);
                }
            }
            return false;
        }

        public void recordSuccess() {
            successCount.incrementAndGet();
            failureCount.set(0);
        }

        public void recordFailure() {
            failureCount.incrementAndGet();
            lastFailureTime = Instant.now();
        }
    }
}

// === ARCHIVO: src/main/java/com/pragma/riskengine/config/DisruptorConfig.java ===
package com.pragma.riskengine.config;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.pragma.riskengine.disruptor.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.function.Supplier;

public class DisruptorConfig {
    private static final Logger logger = LoggerFactory.getLogger(DisruptorConfig.class);
    private static final int DEFAULT_BUFFER_SIZE = 8192;
    private static final int MIN_BUFFER_SIZE = 1024;
    private static final int MAX_BUFFER_SIZE = 65536;

    private final int bufferSize;
    private final WaitStrategy waitStrategy;
    private final boolean multiProducer;
    private final ExecutorService executorService;

    public DisruptorConfig() {
        this(DEFAULT_BUFFER_SIZE, WaitStrategyType.BLOCKING);
    }

    public DisruptorConfig(int bufferSize, WaitStrategyType strategyType) {
        this.bufferSize = normalizeBufferSize(bufferSize);
        this.waitStrategy = createWaitStrategy(strategyType);
        this.multiProducer = true;
        this.executorService = createExecutorService();
        logger.info("DisruptorConfig initialized: bufferSize={}, strategy={}, multiProducer={}",
                bufferSize, strategyType, multiProducer);
    }

    public Disruptor<OrderEvent> createOrderDisruptor(EventHandler<OrderEvent> handler) {
        Supplier<EventFactory<OrderEvent>> factory = OrderEvent::new;

        Disruptor<OrderEvent> disruptor = new Disruptor<>(
                factory.get(),
                bufferSize,
                executorService,
                multiProducer ? ProducerType.MULTI : ProducerType.SINGLE,
                waitStrategy
        );

        disruptor.handleEventsWith(handler);
        disruptor.setDefaultExceptionHandler(new LoggingExceptionHandler());

        logger.info("OrderDisruptor created with buffer size: {}", bufferSize);
        return disruptor;
    }

    public Disruptor<OrderEvent> createOrderDisruptorWithBatchHandler(EventHandler<OrderEvent>[] handlers) {
        Supplier<EventFactory<OrderEvent>> factory = OrderEvent::new;

        Disruptor<OrderEvent> disruptor = new Disruptor<>(
                factory.get(),
                bufferSize,
                executorService,
                multiProducer ? ProducerType.MULTI : ProducerType.SINGLE,
                waitStrategy
        );

        disruptor.handleEventsWith(handlers);
        disruptor.setDefaultExceptionHandler(new LoggingExceptionHandler());

        logger.info("OrderDisruptor created with {} handlers, buffer size: {}",
                handlers.length, bufferSize);
        return disruptor;
    }

    private int normalizeBufferSize(int size) {
        if (size < MIN_BUFFER_SIZE) {
            logger.warn("Buffer size {} too small, using minimum {}", size, MIN_BUFFER_SIZE);
            return MIN_BUFFER_SIZE;
        }
        if (size > MAX_BUFFER_SIZE) {
            logger.warn("Buffer size {} too large, using maximum {}", size, MAX_BUFFER_SIZE);
            return MAX_BUFFER_SIZE;
        }
        if ((size & (size - 1)) != 0) {
            int normalized = Integer.highestOneBit(size) << 1;
            logger.warn("Buffer size {} not power of 2, using {}", size, normalized);
            return normalized;
        }
        return size;
    }

    private WaitStrategy createWaitStrategy(WaitStrategyType type) {
        return switch (type) {
            case BLOCKING -> new BlockingWaitStrategy();
            case YIELDING -> new YieldingWaitStrategy();
            case SLEEPING -> new SleepingWaitStrategy();
            case BUSY_SPIN -> new BusySpinWaitStrategy();
            case PHASED_BACKOFF -> new PhasedBackoffWaitStrategy(1, 1, TimeUnit.MILLISECONDS);
        };
    }

    private ExecutorService createExecutorService() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public WaitStrategy getWaitStrategy() {
        return waitStrategy;
    }

    public boolean isMultiProducer() {
        return multiProducer;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void shutdown() {
        logger.info("Shutting down Disruptor executor service");
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public enum WaitStrategyType {
        BLOCKING,
        YIELDING,
        SLEEPING,
        BUSY_SPIN,
        PHASED_BACKOFF
    }

    private static class LoggingExceptionHandler implements ExceptionHandler<OrderEvent> {
        private static final Logger exceptionLogger = LoggerFactory.getLogger("DisruptorExceptionHandler");

        @Override
        public void handleEventException(Throwable ex, long sequence, OrderEvent event) {
            exceptionLogger.error("Exception processing event at sequence {}: {}",
                    sequence, ex.getMessage(), ex);
        }

        @Override
        public void handleOnStartException(Throwable ex) {
            exceptionLogger.error("Exception starting disruptor: {}", ex.getMessage(), ex);
        }

        @Override
        public void handleOnShutdownException(Throwable ex) {
            exceptionLogger.error("Exception shutting down disruptor: {}", ex.getMessage(), ex);
        }
    }
}

// === ARCHIVO: src/main/java/com/pragma/riskengine/config/ResilienceConfig.java ===
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

// === ARCHIVO: src/main/java/com/pragma/riskengine/exception/RiskThresholdExceededException.java ===
package com.pragma.riskengine.exception;

import java.math.BigDecimal;
import java.time.Instant;

public class RiskThresholdExceededException extends RuntimeException {
    
    private final String orderId;
    private final String traderId;
    private final String strategyId;
    private final String instrumentId;
    private final String thresholdId;
    private final ThresholdType thresholdType;
    private final BigDecimal currentValue;
    private final BigDecimal thresholdValue;
    private final BigDecimal exceedPercentage;
    private final Instant timestamp;
    private final String riskMetric;
    
    public enum ThresholdType {
        TRADER_EXPOSURE,
        STRATEGY_EXPOSURE,
        INSTRUMENT_EXPOSURE,
        VAR_LIMIT,
        POSITION_LIMIT,
        NOTIONAL_LIMIT,
        CONCENTRATION_LIMIT,
        CORRELATION_LIMIT
    }
    
    public RiskThresholdExceededException(String orderId, String traderId, String strategyId,
            String instrumentId, String thresholdId, ThresholdType thresholdType,
            BigDecimal currentValue, BigDecimal thresholdValue, String riskMetric) {
        super(buildMessage(orderId, thresholdId, currentValue, thresholdValue, riskMetric));
        this.orderId = orderId;
        this.traderId = traderId;
        this.strategyId = strategyId;
        this.instrumentId = instrumentId;
        this.thresholdId = thresholdId;
        this.thresholdType = thresholdType;
        this.currentValue = currentValue;
        this.thresholdValue = thresholdValue;
        this.riskMetric = riskMetric;
        this.timestamp = Instant.now();
        
        if (thresholdValue.compareTo(BigDecimal.ZERO) > 0) {
            this.exceedPercentage = currentValue
                .subtract(thresholdValue)
                .divide(thresholdValue, 4, java.math.RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
        } else {
            this.exceedPercentage = BigDecimal.ZERO;
        }
    }
    
    private static String buildMessage(String orderId, String thresholdId, 
            BigDecimal currentValue, BigDecimal thresholdValue, String riskMetric) {
        return String.format("Risk threshold exceeded for order %s: %s value %.2f exceeds threshold %.2f (limit: %s)",
                orderId, riskMetric, currentValue, thresholdValue, thresholdId);
    }
    
    public String getOrderId() {
        return orderId;
    }
    
    public String getTraderId() {
        return traderId;
    }
    
    public String getStrategyId() {
        return strategyId;
    }
    
    public String getInstrumentId() {
        return instrumentId;
    }
    
    public String getThresholdId() {
        return thresholdId;
    }
    
    public ThresholdType getThresholdType() {
        return thresholdType;
    }
    
    public BigDecimal getCurrentValue() {
        return currentValue;
    }
    
    public BigDecimal getThresholdValue() {
        return thresholdValue;
    }
    
    public BigDecimal getExceedPercentage() {
        return exceedPercentage;
    }
    
    public Instant getTimestamp() {
        return timestamp;
    }
    
    public String getRiskMetric() {
        return riskMetric;
    }
    
    public boolean isCritical() {
        return exceedPercentage.compareTo(BigDecimal.valueOf(50)) > 0;
    }
    
    public boolean isModerate() {
        return exceedPercentage.compareTo(BigDecimal.valueOf(20)) > 0 
            && exceedPercentage.compareTo(BigDecimal.valueOf(50)) <= 0;
    }
    
    public String toAuditString() {
        return String.format("REJECTED|orderId=%s|traderId=%s|strategyId=%s|instrumentId=%s|" +
                "thresholdId=%s|type=%s|current=%.2f|threshold=%.2f|exceedPct=%.2f%%|riskMetric=%s|timestamp=%s",
                orderId, traderId, strategyId, instrumentId, thresholdId, thresholdType,
                currentValue, thresholdValue, exceedPercentage, riskMetric, timestamp);
    }
}

// === ARCHIVO: src/main/java/com/pragma/riskengine/exception/KillSwitchActivatedException.java ===
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
        CONSECUTIVE_THRESHOLD_BREACHES,
        CIRCUIT_BREAKER_OPEN,
        VOLATILITY_SPIKE,
        CORRELATION_BREACH,
        CONCENTRATION_LIMIT_EXCEEDED,
        MANUAL_TRIGGER,
        SYSTEM_OVERLOAD,
        EXTERNAL_SIGNAL
    }
    
    public KillSwitchActivatedException(String algorithmId, String strategyId, 
            KillSwitchReason reason, List<String> affectedInstruments,
            Map<String, BigDecimal> exposureBeforeKillSwitch, BigDecimal totalExposure,
            BigDecimal thresholdBreached, int consecutiveBreaches, String triggerEventId,
            boolean autoResetEnabled, Duration autoResetDelay) {
        super(buildMessage(algorithmId, strategyId, reason, totalExposure, thresholdBreached));
        this.algorithmId = algorithmId;
        this.strategyId = strategyId;
        this.reason = reason;
        this.activationTime = Instant.now();
        this.affectedInstruments = List.copyOf(affectedInstruments);
        this.exposureBeforeKillSwitch = Map.copyOf(exposureBeforeKillSwitch);
        this.totalExposure = totalExposure;
        this.thresholdBreached = thresholdBreached;
        this.consecutiveBreaches = consecutiveBreaches;
        this.triggerEventId = triggerEventId;
        this.autoResetEnabled = autoResetEnabled;
        this.autoResetDelay = autoResetDelay;
    }
    
    private static String buildMessage(String algorithmId, String strategyId, 
            KillSwitchReason reason, BigDecimal totalExposure, BigDecimal thresholdBreached) {
        return String.format("Kill switch activated for algorithm %s (strategy: %s). Reason: %s. " +
                "Total exposure: %.2f, Threshold breached: %.2f",
                algorithmId, strategyId, reason, totalExposure, thresholdBreached);
    }
    
    public String getAlgorithmId() {
        return algorithmId;
    }
    
    public String getStrategyId() {
        return strategyId;
    }
    
    public KillSwitchReason getReason() {
        return reason;
    }
    
    public Instant getActivationTime() {
        return activationTime;
    }
    
    public List<String> getAffectedInstruments() {
        return affectedInstruments;
    }
    
    public Map<String, BigDecimal> getExposureBeforeKillSwitch() {
        return exposureBeforeKillSwitch;
    }
    
    public BigDecimal getTotalExposure() {
        return totalExposure;
    }
    
    public BigDecimal getThresholdBreached() {
        return thresholdBreached;
    }
    
    public int getConsecutiveBreaches() {
        return consecutiveBreaches;
    }
    
    public String getTriggerEventId() {
        return triggerEventId;
    }
    
    public boolean isAutoResetEnabled() {
        return autoResetEnabled;
    }
    
    public Duration getAutoResetDelay() {
        return autoResetDelay;
    }
    
    public boolean canAutoReset() {
        return autoResetEnabled && autoResetDelay != null;
    }
    
    public String toAuditString() {
        return String.format("KILL_SWITCH_ACTIVATED|algorithmId=%s|strategyId=%s|reason=%s|" +
                "totalExposure=%.2f|thresholdBreached=%.2f|consecutiveBreaches=%d|" +
                "triggerEventId=%s|autoResetEnabled=%s|activationTime=%s",
                algorithmId, strategyId, reason, totalExposure, thresholdBreached,
                consecutiveBreaches, triggerEventId, autoResetEnabled, activationTime);
    }
    
    public static class Duration {
        private final long seconds;
        
        public Duration(long seconds) {
            this.seconds = seconds;
        }
        
        public static Duration ofSeconds(long seconds) {
            return new Duration(seconds);
        }
        
        public static Duration ofMinutes(long minutes) {
            return new Duration(minutes * 60);
        }
        
        public long getSeconds() {
            return seconds;
        }
    }
}

// === ARCHIVO: src/main/java/com/pragma/riskengine/audit/AuditLogger.java ===
package com.pragma.riskengine.audit;

import com.pragma.riskengine.exception.KillSwitchActivatedException;
import com.pragma.riskengine.exception.RiskThresholdExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public class AuditLogger {
    
    private static final Logger logger = LoggerFactory.getLogger(AuditLogger.class);
    private static final DateTimeFormatter ISO_FORMATTER = 
            DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.of("UTC"));
    
    private final String componentId;
    private final String componentType;
    private final ConcurrentMap<String, AuditEntry> recentEntries;
    private final AtomicLong sequenceNumber;
    private final boolean structuredLogging;
    private final int maxRecentEntries;
    
    public AuditLogger(String componentId, String componentType) {
        this(componentId, componentType, true, 10000);
    }
    
    public AuditLogger(String componentId, String componentType, 
            boolean structuredLogging, int maxRecentEntries) {
        this.componentId = componentId;
        this.componentType = componentType;
        this.structuredLogging = structuredLogging;
        this.maxRecentEntries = maxRecentEntries;
        this.recentEntries = new ConcurrentHashMap<>();
        this.sequenceNumber = new AtomicLong(0);
    }
    
    public void logRiskDecision(String orderId, String traderId, String strategyId,
            String instrumentId, String decision, BigDecimal riskScore, 
            BigDecimal exposure, BigDecimal threshold) {
        AuditEntry entry = new AuditEntry(
                generateSequenceId(),
                "RISK_DECISION",
                Instant.now(),
                orderId, traderId, strategyId, instrumentId
        );
        entry.addField("decision", decision);
        entry.addField("riskScore", riskScore != null ? riskScore.toPlainString() : "N/A");
        entry.addField("exposure", exposure != null ? exposure.toPlainString() : "N/A");
        entry.addField("threshold", threshold != null ? threshold.toPlainString() : "N/A");
        entry.addField("mifidIICompliance", "true");
        entry.addField("decisionTimestamp", ISO_FORMATTER.format(entry.timestamp));
        
        writeEntry(entry);
    }
    
    public void logOrderRejected(String orderId, String traderId, String strategyId,
            String instrumentId, RiskThresholdExceededException exception) {
        AuditEntry entry = new AuditEntry(
                generateSequenceId(),
                "ORDER_REJECTED",
                Instant.now(),
                orderId, traderId, strategyId, instrumentId
        );
        entry.addField("rejectionReason", "THRESHOLD_EXCEEDED");
        entry.addField("thresholdId", exception.getThresholdId());
        entry.addField("thresholdType", exception.getThresholdType().name());
        entry.addField("currentValue", exception.getCurrentValue().toPlainString());
        entry.addField("thresholdValue", exception.getThresholdValue().toPlainString());
        entry.addField("exceedPercentage", exception.getExceedPercentage().toPlainString());
        entry.addField("riskMetric", exception.getRiskMetric());
        entry.addField("isCritical", String.valueOf(exception.isCritical()));
        entry.addField("auditTrail", exception.toAuditString());
        
        writeEntry(entry);
    }
    
    public void logKillSwitchActivated(KillSwitchActivatedException exception) {
        AuditEntry entry = new AuditEntry(
                generateSequenceId(),
                "KILL_SWITCH_ACTIVATED",
                exception.getActivationTime(),
                null, null, exception.getStrategyId(), null
        );
        entry.addField("algorithmId", exception.getAlgorithmId());
        entry.addField("reason", exception.getReason().name());
        entry.addField("totalExposure", exception.getTotalExposure().toPlainString());
        entry.addField("thresholdBreached", exception.getThresholdBreached().toPlainString());
        entry.addField("consecutiveBreaches", String.valueOf(exception.getConsecutiveBreaches()));
        entry.addField("triggerEventId", exception.getTriggerEventId());
        entry.addField("autoResetEnabled", String.valueOf(exception.isAutoResetEnabled()));
        entry.addField("affectedInstruments", String.join(",", exception.getAffectedInstruments()));
        entry.addField("auditTrail", exception.toAuditString());
        
        writeEntry(entry);
    }
    
    public void logThresholdAdjustment(String thresholdId, String instrumentId,
            BigDecimal oldValue, BigDecimal newValue, BigDecimal volatility, String reason) {
        AuditEntry entry = new AuditEntry(
                generateSequenceId(),
                "THRESHOLD_ADJUSTMENT",
                Instant.now(),
                null, null, null, instrumentId
        );
        entry.addField("thresholdId", thresholdId);
        entry.addField("oldValue", oldValue.toPlainString());
        entry.addField("newValue", newValue.toPlainString());
        entry.addField("adjustmentPercentage", 
                newValue.subtract(oldValue).divide(oldValue, 4, java.math.RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100)).toPlainString());
        entry.addField("volatilityFactor", volatility != null ? volatility.toPlainString() : "N/A");
        entry.addField("reason", reason);
        
        writeEntry(entry);
    }
    
    public void logVarCalculation(String calculationId, BigDecimal var, 
            BigDecimal confidenceLevel, int lookbackPeriods, Instant calculationTime) {
        AuditEntry entry = new AuditEntry(
                generateSequenceId(),
                "VAR_CALCULATION",
                calculationTime,
                null, null, null, null
        );
        entry.addField("calculationId", calculationId);
        entry.addField("var", var.toPlainString());
        entry.addField("confidenceLevel", confidenceLevel.toPlainString());
        entry.addField("lookbackPeriods", String.valueOf(lookbackPeriods));
        entry.addField("calculationTimestamp", ISO_FORMATTER.format(calculationTime));
        
        writeEntry(entry);
    }
    
    public void logMarketDataUpdate(String instrumentId, String dataSource,
            BigDecimal bid, BigDecimal ask, int bidSize, int askSize) {
        if (!structuredLogging) {
            return;
        }
        
        AuditEntry entry = new AuditEntry(
                generateSequenceId(),
                "MARKET_DATA_UPDATE",
                Instant.now(),
                null, null, null, instrumentId
        );
        entry.addField("dataSource", dataSource);
        entry.addField("bid", bid.toPlainString());
        entry.addField("ask", ask.toPlainString());
        entry.addField("spread", ask.subtract(bid).toPlainString());
        entry.addField("bidSize", String.valueOf(bidSize));
        entry.addField("askSize", String.valueOf(askSize));
        
        writeEntry(entry);
    }
    
    private void writeEntry(AuditEntry entry) {
        recentEntries.put(entry.entryId, entry);
        cleanupOldEntries();
        
        if (structuredLogging) {
            logger.info("AUDIT|{}", entry.toStructuredString());
        } else {
            logger.info("[{}] {}: {}", entry.timestamp, entry.eventType, entry.toKeyValueString());
        }
    }
    
    private void cleanupOldEntries() {
        if (recentEntries.size() > maxRecentEntries) {
            String oldestKey = recentEntries.keys().nextElement();
            recentEntries.remove(oldestKey);
        }
    }
    
    private String generateSequenceId() {
        long seq = sequenceNumber.incrementAndGet();
        return String.format("%s-%s-%d", componentId, UUID.randomUUID().toString().substring(0, 8), seq);
    }
    
    public AuditEntry getRecentEntry(String entryId) {
        return recentEntries.get(entryId);
    }
    
    public int getRecentEntryCount() {
        return recentEntries.size();
    }
    
    public static class AuditEntry {
        private final String entryId;
        private final String eventType;
        private final Instant timestamp;
        private final String orderId;
        private final String traderId;
        private final String strategyId;
        private final String instrumentId;
        private final ConcurrentMap<String, String> fields;
        
        public AuditEntry(String entryId, String eventType, Instant timestamp,
                String orderId, String traderId, String strategyId, String instrumentId) {
            this.entryId = entryId;
            this.eventType = eventType;
            this.timestamp = timestamp;
            this.orderId = orderId;
            this.traderId = traderId;
            this.strategyId = strategyId;
            this.instrumentId = instrumentId;
            this.fields = new ConcurrentHashMap<>();
        }
        
        public void addField(String key, String value) {
            fields.put(key, value);
        }
        
        public String getEntryId() {
            return entryId;
        }
        
        public String getEventType() {
            return eventType;
        }
        
        public Instant getTimestamp() {
            return timestamp;
        }
        
        public String toStructuredString() {
            StringBuilder sb = new StringBuilder();
            sb.append("entryId=").append(entryId)
              .append("|eventType=").append(eventType)
              .append("|timestamp=").append(ISO_FORMATTER.format(timestamp));
            
            if (orderId != null) sb.append("|orderId=").append(orderId);
            if (traderId != null) sb.append("|traderId=").append(traderId);
            if (strategyId != null) sb.append("|strategyId=").append(strategyId);
            if (instrumentId != null) sb.append("|instrumentId=").append(instrumentId);
            
            fields.forEach((k, v) -> sb.append("|").append(k).append("=").append(v));
            
            return sb.toString();
        }
        
        public String toKeyValueString() {
            return fields.entrySet().stream()
                    .map(e -> e.getKey() + "=" + e.getValue())
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");
        }
    }
}

// === ARCHIVO: src/main/java/com/pragma/riskengine/replay/ReplayService.java ===
package com.pragma.riskengine.replay;

import com.pragma.riskengine.disruptor.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
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
                dos.writeUTF(evt.orderId() != null ? evt.orderId() : "");
                dos.writeUTF(evt.traderId() != null ? evt.traderId() : "");
                dos.writeUTF(evt.strategyId() != null ? evt.strategyId() : "");
                dos.writeUTF(evt.instrumentId() != null ? evt.instrumentId() : "");
                dos.writeUTF(evt.side() != null ? evt.side() : "");
                dos.writeDouble(evt.quantity() != null ? evt.quantity().doubleValue() : 0.0);
                dos.writeDouble(evt.price() != null ? evt.price().doubleValue() : 0.0);
                dos.writeLong(evt.timestamp() != null ? evt.timestamp().toEpochMilli() : System.currentTimeMillis());
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
                        if (evt.timestamp() != null &&
                            !evt.timestamp().isBefore(startTime) &&
                            !evt.timestamp().isAfter(endTime)) {
                            eventsToReplay.add(evt);
                        }
                    }
                }

                eventsToReplay.sort(Comparator.comparing(OrderEvent::timestamp,
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
                sortedEvents.sort(Comparator.comparing(OrderEvent::timestamp,
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
                        OrderEvent evt = new OrderEvent(
                            orderId,
                            traderId,
                            strategyId,
                            instrumentId,
                            side,
                            java.math.BigDecimal.valueOf(quantity),
                            java.math.BigDecimal.valueOf(price),
                            Instant.ofEpochMilli(timestamp)
                        );
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
                    if (evt.traderId() != null) {
                        counts.merge(evt.traderId(), 1L, Long::sum);
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

// === ARCHIVO: src/test/java/com/pragma/riskengine/disruptor/OrderEventHandlerTest.java ===
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

// === ARCHIVO: src/test/java/com/pragma/riskengine/service/RiskScoringServiceTest.java ===
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

// === ARCHIVO: src/test/java/com/pragma/riskengine/service/KillSwitchServiceTest.java ===
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


// === ARCHIVO: src/main/java/com/pragma/riskengine/adapter/exchange/ExchangeAdapter.java ===
package com.pragma.riskengine.adapter.exchange;

import com.pragma.riskengine.config.ResilienceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class ExchangeAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ExchangeAdapter.class);
    private static final int DEFAULT_TIMEOUT_MS = 5000;
    private static final int MAX_PENDING_ORDERS = 10000;

    private final ExecutorService orderExecutor;
    private final ConcurrentHashMap<String, PendingOrder> pendingOrders;
    private final BlockingQueue<OrderResponse> responseQueue;
    private final AtomicBoolean isConnected;
    private final AtomicLong orderSequence;
    private final CircuitBreakerAdapter circuitBreaker;
    private final ResilienceConfig resilienceConfig;

    public ExchangeAdapter(ResilienceConfig resilienceConfig) {
        this.resilienceConfig = resilienceConfig;
        this.orderExecutor = Executors.newVirtualThreadPerTaskExecutor();
        this.pendingOrders = new ConcurrentHashMap<>();
        this.responseQueue = new LinkedBlockingQueue<>(MAX_PENDING_ORDERS);
        this.isConnected = new AtomicBoolean(false);
        this.orderSequence = new AtomicLong(0);
        this.circuitBreaker = new CircuitBreakerAdapter(resilienceConfig);
    }

    public String sendOrder(String orderId, String traderId, String strategyId,
            String instrumentId, String side, BigDecimal quantity, BigDecimal price) {
        if (!isConnected.get()) {
            throw new ExchangeNotConnectedException("Exchange connection is not established");
        }

        String sequenceId = generateSequenceId();
        long sequence = orderSequence.incrementAndGet();

        logger.info("Sending order to exchange: orderId={}, sequence={}, instrument={}, side={}, qty={}, price={}",
                orderId, sequence, instrumentId, side, quantity, price);

        PendingOrder pending = new PendingOrder(orderId, sequenceId, traderId, strategyId,
                instrumentId, side, quantity, price, Instant.now());
        pendingOrders.put(sequenceId, pending);

        CompletableFuture<OrderResponse> future = CompletableFuture.supplyAsync(() -> {
            return executeOrderWithResilience(pending);
        }, orderExecutor);

        future.thenAccept(response -> {
            pendingOrders.remove(sequenceId);
            responseQueue.offer(response);
            logger.info("Order response received: orderId={}, status={}, sequence={}",
                    orderId, response.status(), sequence);
        });

        return sequenceId;
    }

    private OrderResponse executeOrderWithResilience(PendingOrder order) {
        if (circuitBreaker.isOpen()) {
            logger.warn("Circuit breaker is OPEN, rejecting order: {}", order.orderId());
            return new OrderResponse(order.orderId(), order.sequenceId(),
                    OrderStatus.REJECTED, "Circuit breaker open", Instant.now());
        }

        try {
            return resilienceConfig.executeWithRetry(() -> {
                return submitToExchange(order);
            });
        } catch (Exception e) {
            logger.error("Order execution failed after retries: orderId={}, error={}",
                    order.orderId(), e.getMessage());
            circuitBreaker.recordFailure();
            return new OrderResponse(order.orderId(), order.sequenceId(),
                    OrderStatus.REJECTED, e.getMessage(), Instant.now());
        }
    }

    private OrderResponse submitToExchange(PendingOrder order) {
        try {
            Thread.sleep(10);
            circuitBreaker.recordSuccess();
            return new OrderResponse(order.orderId(), order.sequenceId(),
                    OrderStatus.ACCEPTED, "Order accepted by exchange", Instant.now());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new OrderResponse(order.orderId(), order.sequenceId(),
                    OrderStatus.ERROR, "Order submission interrupted", Instant.now());
        }
    }

    public OrderResponse waitForResponse(long timeoutMs) throws InterruptedException {
        OrderResponse response = responseQueue.poll(timeoutMs, TimeUnit.MILLISECONDS);
        if (response == null) {
            logger.warn("Timeout waiting for order response after {}ms", timeoutMs);
        }
        return response;
    }

    public boolean checkConnectivity() {
        try {
            return isConnected.get();
        } catch (Exception e) {
            logger.error("Connectivity check failed: {}", e.getMessage());
            return false;
        }
    }

    public void connect() {
        logger.info("Connecting to exchange...");
        isConnected.set(true);
        logger.info("Connected to exchange successfully");
    }

    public void disconnect() {
        logger.info("Disconnecting from exchange...");
        isConnected.set(false);
        pendingOrders.clear();
        logger.info("Disconnected from exchange");
    }

    private String generateSequenceId() {
        return UUID.randomUUID().toString();
    }

    public int getPendingOrdersCount() {
        return pendingOrders.size();
    }

    public void shutdown() {
        disconnect();
        orderExecutor.shutdown();
        try {
            if (!orderExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                orderExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            orderExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public record PendingOrder(String orderId, String sequenceId, String traderId,
            String strategyId, String instrumentId, String side,
            BigDecimal quantity, BigDecimal price, Instant submittedAt) {
    }

    public record OrderResponse(String orderId, String sequenceId, OrderStatus status,
            String message, Instant timestamp) {
    }

    public enum OrderStatus {
        ACCEPTED, REJECTED, FILLED, PARTIALLY_FILLED, CANCELLED, ERROR
    }

    public static class ExchangeNotConnectedException extends RuntimeException {
        public ExchangeNotConnectedException(String message) {
            super(message);
        }
    }

    private static class CircuitBreakerAdapter {
        private final AtomicBoolean isOpen = new AtomicBoolean(false);
        private final AtomicLong failureCount = new AtomicLong(0);
        private final AtomicLong successCount = new AtomicLong(0);
        private final ResilienceConfig config;
        private volatile Instant lastFailureTime;

        public CircuitBreakerAdapter(ResilienceConfig config) {
            this.config = config;
        }

        public boolean isOpen() {
            if (failureCount.get() >= config.getFailureThreshold()) {
                if (lastFailureTime != null &&
                        Duration.between(lastFailureTime, Instant.now()).toMillis() < config.getResetTimeoutMs()) {
                    return true;
                } else {
                    failureCount.set(0);
                    successCount.set(0);
                }
            }
            return false;
        }

        public void recordSuccess() {
            successCount.incrementAndGet();
            failureCount.set(0);
        }

        public void recordFailure() {
            failureCount.incrementAndGet();
            lastFailureTime = Instant.now();
        }
    }
}

// === ARCHIVO: src/main/java/com/pragma/riskengine/exception/KillSwitchActivatedException.java ===
package com.pragma.riskengine.exception;

import com.pragma.riskengine.model.Threshold;

import java.math.BigDecimal;
import java.time.Duration;
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
        CONSECUTIVE_THRESHOLD_BREACHES,
        CIRCUIT_BREAKER_OPEN,
        VOLATILITY_SPIKE,
        CORRELATION_BREACH,
        CONCENTRATION_LIMIT_EXCEEDED,
        MANUAL_TRIGGER,
        SYSTEM_OVERLOAD,
        EXTERNAL_SIGNAL
    }
    
    public KillSwitchActivatedException(String algorithmId, String strategyId, 
            KillSwitchReason reason, List<String> affectedInstruments,
            Map<String, BigDecimal> exposureBeforeKillSwitch, BigDecimal totalExposure,
            BigDecimal thresholdBreached, int consecutiveBreaches, String triggerEventId,
            boolean autoResetEnabled, Duration autoResetDelay) {
        super(buildMessage(algorithmId, strategyId, reason, totalExposure, thresholdBreached));
        this.algorithmId = algorithmId;
        this.strategyId = strategyId;
        this.reason = reason;
        this.activationTime = Instant.now();
        this.affectedInstruments = List.copyOf(affectedInstruments);
        this.exposureBeforeKillSwitch = Map.copyOf(exposureBeforeKillSwitch);
        this.totalExposure = totalExposure;
        this.thresholdBreached = thresholdBreached;
        this.consecutiveBreaches = consecutiveBreaches;
        this.triggerEventId = triggerEventId;
        this.autoResetEnabled = autoResetEnabled;
        this.autoResetDelay = autoResetDelay;
    }
    
    private static String buildMessage(String algorithmId, String strategyId, 
            KillSwitchReason reason, BigDecimal totalExposure, BigDecimal thresholdBreached) {
        return String.format("Kill switch activated for algorithm %s (strategy: %s). Reason: %s. " +
                "Total exposure: %.2f, Threshold breached: %.2f",
                algorithmId, strategyId, reason, totalExposure, thresholdBreached);
    }
    
    public String getAlgorithmId() {
        return algorithmId;
    }
    
    public String getStrategyId() {
        return strategyId;
    }
    
    public KillSwitchReason getReason() {
        return reason;
    }
    
    public Instant getActivationTime() {
        return activationTime;
    }
    
    public List<String> getAffectedInstruments() {
        return affectedInstruments;
    }
    
    public Map<String, BigDecimal> getExposureBeforeKillSwitch() {
        return exposureBeforeKillSwitch;
    }
    
    public BigDecimal getTotalExposure() {
        return totalExposure;
    }
    
    public BigDecimal getThresholdBreached() {
        return thresholdBreached;
    }
    
    public int getConsecutiveBreaches() {
        return consecutiveBreaches;
    }
    
    public String getTriggerEventId() {
        return triggerEventId;
    }
    
    public boolean isAutoResetEnabled() {
        return autoResetEnabled;
    }
    
    public Duration getAutoResetDelay() {
        return autoResetDelay;
    }
    
    public boolean canAutoReset() {
        return autoResetEnabled && autoResetDelay != null;
    }
    
    public String toAuditString() {
        return String.format("KILL_SWITCH_ACTIVATED|algorithmId=%s|strategyId=%s|reason=%s|" +
                "totalExposure=%.2f|thresholdBreached=%.2f|consecutiveBreaches=%d|" +
                "triggerEventId=%s|autoResetEnabled=%s|activationTime=%s",
                algorithmId, strategyId, reason, totalExposure, thresholdBreached,
                consecutiveBreaches, triggerEventId, autoResetEnabled, activationTime);
    }
}

// === ARCHIVO: src/main/java/com/pragma/riskengine/replay/ReplayService.java ===
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


// === ARCHIVO: src/main/java/com/pragma/riskengine/model/Threshold.java ===
package com.pragma.riskengine.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Define umbrales dinámicos para circuit breakers calibrados por volatilidad.
 * Implementa ajuste automático de límites basado en condiciones de mercado.
 */
public class Threshold {

    private final String thresholdId;
    private final ThresholdType type;
    private volatile BigDecimal baseValue;
    private volatile BigDecimal currentMultiplier;
    private volatile BigDecimal minThreshold;
    private volatile BigDecimal maxThreshold;
    private volatile BigDecimal volatilityFactor;
    private volatile Instant lastAdjustmentTime;
    private final ConcurrentMap<String, BigDecimal> calibrationHistory;
    private final ReentrantReadWriteLock lock;
    private final java.time.Duration adjustmentWindow;

    public Threshold(String thresholdId, ThresholdType type, BigDecimal baseValue) {
        this.thresholdId = Objects.requireNonNull(thresholdId, "thresholdId cannot be null");
        this.type = Objects.requireNonNull(type, "type cannot be null");
        this.baseValue = Objects.requireNonNull(baseValue, "baseValue cannot be null");
        this.currentMultiplier = BigDecimal.ONE;
        this.minThreshold = baseValue.multiply(new BigDecimal("0.5"));
        this.maxThreshold = baseValue.multiply(new BigDecimal("2.0"));
        this.volatilityFactor = BigDecimal.ZERO;
        this.lastAdjustmentTime = Instant.now();
        this.calibrationHistory = new ConcurrentHashMap<>();
        this.lock = new ReentrantReadWriteLock();
        this.adjustmentWindow = java.time.Duration.ofMinutes(5);
    }

    /**
     * Ajusta el umbral basado en la volatilidad observada.
     */
    public void adjustForVolatility(BigDecimal currentVolatility, BigDecimal historicalVolatility) {
        lock.writeLock().lock();
        try {
            if (historicalVolatility.compareTo(BigDecimal.ZERO) <= 0) {
                return;
            }

            BigDecimal volatilityRatio = currentVolatility.divide(
                historicalVolatility, RoundingMode.HALF_UP
            );
            
            this.volatilityFactor = volatilityRatio;
            
            BigDecimal newMultiplier = calculateDynamicMultiplier(volatilityRatio);
            this.currentMultiplier = clampMultiplier(newMultiplier);
            
            recalculateThreshold();
            recordCalibration(currentVolatility, currentMultiplier);
            this.lastAdjustmentTime = Instant.now();
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Calcula el multiplicador dinámico basado en el ratio de volatilidad.
     */
    private BigDecimal calculateDynamicMultiplier(BigDecimal volatilityRatio) {
        return switch (type) {
            case TRADEX -> {
                if (volatilityRatio.compareTo(new BigDecimal("1.5")) > 0) {
                    yield new BigDecimal("0.7");
                } else if (volatilityRatio.compareTo(new BigDecimal("0.5")) < 0) {
                    yield new BigDecimal("1.3");
                } else {
                    yield BigDecimal.ONE;
                }
            }
            case STRATEGY -> {
                if (volatilityRatio.compareTo(new BigDecimal("2.0")) > 0) {
                    yield new BigDecimal("0.5");
                } else if (volatilityRatio.compareTo(new BigDecimal("0.75")) < 0) {
                    yield new BigDecimal("1.5");
                } else {
                    yield BigDecimal.ONE;
                }
            }
            case INSTRUMENT -> {
                if (volatilityRatio.compareTo(new BigDecimal("1.2")) > 0) {
                    yield new BigDecimal("0.6");
                } else if (volatilityRatio.compareTo(new BigDecimal("0.8")) < 0) {
                    yield new BigDecimal("1.4");
                } else {
                    yield BigDecimal.ONE;
                }
            }
            case PORTFOLIO -> {
                if (volatilityRatio.compareTo(new BigDecimal("1.8")) > 0) {
                    yield new BigDecimal("0.4");
                } else if (volatilityRatio.compareTo(new BigDecimal("0.6")) < 0) {
                    yield new BigDecimal("1.6");
                } else {
                    yield BigDecimal.ONE;
                }
            }
        };
    }

    /**
     * Limita el multiplicador a los valores mínimo y máximo permitidos.
     */
    private BigDecimal clampMultiplier(BigDecimal multiplier) {
        BigDecimal clamped = multiplier;
        if (clamped.compareTo(getMinMultiplier()) < 0) {
            clamped = getMinMultiplier();
        } else if (clamped.compareTo(getMaxMultiplier()) > 0) {
            clamped = getMaxMultiplier();
        }
        return clamped;
    }

    /**
     * Recalcula el valor del umbral con el multiplicador actual.
     */
    private void recalculateThreshold() {
        this.baseValue = baseValue;
    }

    /**
     * Registra una calibración en el historial.
     */
    private void recordCalibration(BigDecimal volatility, BigDecimal multiplier) {
        String key = Instant.now().toString();
        calibrationHistory.put(key, multiplier);
        
        if (calibrationHistory.size() > 100) {
            String oldestKey = calibrationHistory.keys().nextElement();
            calibrationHistory.remove(oldestKey);
        }
    }

    /**
     * Obtiene el valor actual del umbral con el multiplicador aplicado.
     */
    public BigDecimal getCurrentValue() {
        lock.readLock().lock();
        try {
            return baseValue.multiply(currentMultiplier).setScale(2, RoundingMode.HALF_UP);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Obtiene el valor base del umbral.
     */
    public BigDecimal getBaseValue() {
        lock.readLock().lock();
        try {
            return baseValue;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Obtiene el multiplicador actual.
     */
    public BigDecimal getCurrentMultiplier() {
        lock.readLock().lock();
        try {
            return currentMultiplier;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Obtiene el multiplicador mínimo permitido.
     */
    public BigDecimal getMinMultiplier() {
        return new BigDecimal("0.3");
    }

    /**
     * Obtiene el multiplicador máximo permitido.
     */
    public BigDecimal getMaxMultiplier() {
        return new BigDecimal("2.0");
    }

    /**
     * Obtiene el factor de volatilidad actual.
     */
    public BigDecimal getVolatilityFactor() {
        lock.readLock().lock();
        try {
            return volatilityFactor;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Obtiene el momento del último ajuste.
     */
    public Instant getLastAdjustmentTime() {
        return lastAdjustmentTime;
    }

    /**
     * Verifica si el umbral necesita recalibración.
     */
    public boolean needsRecalibration(java.time.Duration maxAge) {
        lock.readLock().lock();
        try {
            java.time.Duration age = java.time.Duration.between(lastAdjustmentTime, Instant.now());
            return age.compareTo(maxAge) > 0;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Resetea el umbral a su valor base.
     */
    public void reset() {
        lock.writeLock().lock();
        try {
            this.currentMultiplier = BigDecimal.ONE;
            this.volatilityFactor = BigDecimal.ZERO;
            this.lastAdjustmentTime = Instant.now();
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Obtiene el ID del umbral.
     */
    public String getThresholdId() {
        return thresholdId;
    }

    /**
     * Obtiene el tipo de umbral.
     */
    public ThresholdType getType() {
        return type;
    }

    /**
     * Enum que define los tipos de umbrales para diferentes niveles de riesgo.
     */
    public enum ThresholdType {
        TRADEX,
        STRATEGY,
        INSTRUMENT,
        PORTFOLIO
    }
}
// === ARCHIVO: src/main/java/com/pragma/riskengine/service/RiskScoringService.java ===
package com.pragma.riskengine.service;

import com.pragma.riskengine.model.RiskModel;
import com.pragma.riskengine.model.Threshold;
import com.pragma.riskengine.model.Threshold.ThresholdType;
import com.pragma.riskengine.exception.RiskThresholdExceededException;
import com.pragma.riskengine.audit.AuditLogger;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class RiskScoringService {
    private static final Logger logger = LoggerFactory.getLogger(RiskScoringService.class);
    private static final BigDecimal DEFAULT_CONFIDENCE_LEVEL = new BigDecimal("0.99");
    private static final int DEFAULT_LOOKBACK_PERIODS = 252;
    private static final BigDecimal CIRCUIT_BREAKER_FAILURE_RATE_THRESHOLD = new BigDecimal("0.5");
    private static final int CIRCUIT_BREAKER_WAIT_DURATION_SECONDS = 30;
    private static final BigDecimal MAX_EXPOSURE_PER_ORDER = new BigDecimal("1000000");
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
    private final Object recalculationLock = new Object();

    public RiskScoringService(AuditLogger auditLogger) {
        this.auditLogger = auditLogger;
        this.riskModel = new RiskModel(DEFAULT_CONFIDENCE_LEVEL, DEFAULT_LOOKBACK_PERIODS);
        this.scheduler = Executors.newScheduledThreadPool(2, r -> {
            Thread t = new Thread(r, "risk-scheduler");
            t.setDaemon(true);
            return t;
        });
        this.circuitBreakersByStrategy = new ConcurrentHashMap<>();
        this.thresholdsByInstrument = new ConcurrentHashMap<>();
        this.dailyPnLByTrader = new ConcurrentHashMap<>();
        this.enableCircuitBreakers = true;
        initializeCircuitBreakerRegistry();
        initializeRetryRegistry();
        startRiskRecalculationTask();
    }

    private void initializeCircuitBreakerRegistry() {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .failureRateThreshold(CIRCUIT_BREAKER_FAILURE_RATE_THRESHOLD.floatValue())
                .waitDurationInOpenState(Duration.ofSeconds(CIRCUIT_BREAKER_WAIT_DURATION_SECONDS))
                .slidingWindowSize(100)
                .minimumNumberOfCalls(10)
                .permittedNumberOfCallsInHalfOpenState(3)
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .recordExceptions(java.io.IOException.class, java.util.concurrent.TimeoutException.class)
                .build();
        this.circuitBreakerRegistry = CircuitBreakerRegistry.of(config);
    }

    private void initializeRetryRegistry() {
        RetryConfig retryConfig = RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofMillis(100))
                .retryExceptions(Exception.class)
                .build();
        this.retryRegistry = RetryRegistry.of(retryConfig);
    }

    private void startRiskRecalculationTask() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                recalculateRiskMetrics();
            } catch (Exception e) {
                logger.error("Error en recalculación de métricas de riesgo", e);
            }
        }, 1, 1, TimeUnit.MINUTES);
    }

    public RiskScoreResult calculateRiskScore(OrderRequest order) {
        validateOrderRequest(order);
        checkKillSwitchStatus(order.strategyId());
        BigDecimal orderNotional = order.quantity().multiply(order.price());
        validateExposureLimits(order, orderNotional);
        BigDecimal var = calculateVarWithCircuitBreaker(order.instrumentId());
        BigDecimal orderRiskContribution = calculateOrderRiskContribution(order, var);
        BigDecimal adjustedThreshold = getAdjustedThreshold(order.instrumentId());
        boolean riskApproved = orderRiskContribution.compareTo(adjustedThreshold) <= 0;
        RiskScoreResult result = new RiskScoreResult(
                order.orderId(),
                orderRiskContribution,
                adjustedThreshold,
                riskApproved,
                var,
                Instant.now()
        );
        auditLogger.logRiskDecision(order.orderId(), order.traderId(), order.strategyId(),
                order.instrumentId(), orderRiskContribution, adjustedThreshold, riskApproved);
        if (!riskApproved) {
            throw new RiskThresholdExceededException(
                    order.orderId(),
                    order.traderId(),
                    order.strategyId(),
                    order.instrumentId(),
                    "RISK_THRESHOLD",
                    RiskThresholdExceededException.ThresholdType.PORTFOLIO,
                    orderRiskContribution,
                    adjustedThreshold,
                    "Risk score exceeded"
            );
        }
        updateRiskModel(order, orderNotional);
        return result;
    }

    private void validateOrderRequest(OrderRequest order) {
        if (order == null || order.orderId() == null || order.orderId().isBlank()) {
            throw new IllegalArgumentException("Orden inválida: orderId requerido");
        }
        if (order.quantity() == null || order.quantity().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Cantidad debe ser mayor a cero");
        }
        if (order.price() == null || order.price().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Precio debe ser mayor a cero");
        }
    }

    private void checkKillSwitchStatus(String strategyId) {
        if (!enableCircuitBreakers) {
            logger.warn("Circuit breakers deshabilitados globalmente");
        }
        CircuitBreaker cb = circuitBreakersByStrategy.get(strategyId);
        if (cb != null && cb.getState() == CircuitBreaker.State.OPEN) {
            throw new RiskThresholdExceededException(
                    null,
                    null,
                    strategyId,
                    null,
                    "CIRCUIT_BREAKER",
                    RiskThresholdExceededException.ThresholdType.STRATEGY,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    "Circuit breaker open"
            );
        }
    }

    private void validateExposureLimits(OrderRequest order, BigDecimal orderNotional) {
        if (orderNotional.compareTo(MAX_EXPOSURE_PER_ORDER) > 0) {
            throw new RiskThresholdExceededException(
                    order.orderId(),
                    order.traderId(),
                    order.strategyId(),
                    order.instrumentId(),
                    "MAX_ORDER_EXPOSURE",
                    RiskThresholdExceededException.ThresholdType.INSTRUMENT,
                    orderNotional,
                    MAX_EXPOSURE_PER_ORDER,
                    "Order exposure exceeds maximum"
            );
        }
        BigDecimal traderExposure = riskModel.calculateExposureByTrader(order.traderId());
        BigDecimal newTraderExposure = traderExposure.add(orderNotional);
        if (newTraderExposure.compareTo(getMaxExposureForTrader(order.traderId())) > 0) {
            throw new RiskThresholdExceededException(
                    order.orderId(),
                    order.traderId(),
                    order.strategyId(),
                    order.instrumentId(),
                    "TRADER_EXPOSURE",
                    RiskThresholdExceededException.ThresholdType.TRADEX,
                    newTraderExposure,
                    getMaxExposureForTrader(order.traderId()),
                    "Trader exposure exceeds limit"
            );
        }
    }

    private BigDecimal getMaxExposureForTrader(String traderId) {
        return new BigDecimal("5000000");
    }

    private BigDecimal calculateVarWithCircuitBreaker(String instrumentId) {
        CircuitBreaker circuitBreaker = getOrCreateCircuitBreaker(instrumentId);
        Supplier<BigDecimal> decoratedSupplier = CircuitBreaker.decorateSupplier(
                circuitBreaker,
                () -> riskModel.calculateVar()
        );
        try {
            return decoratedSupplier.get();
        } catch (Exception e) {
            logger.warn("Circuit breaker activado para cálculo de VaR: {}", e.getMessage());
            return getFallbackVar();
        }
    }

    private CircuitBreaker getOrCreateCircuitBreaker(String instrumentId) {
        return circuitBreakersByStrategy.computeIfAbsent(
                instrumentId,
                id -> {
                    CircuitBreaker cb = circuitBreakerRegistry.circuitBreaker(id);
                    cb.getEventPublisher()
                            .onStateTransition(event -> logger.warn(
                                    "Circuit breaker transición: {} -> {} para {}",
                                    event.getStateTransition().getFromState(),
                                    event.getStateTransition().getToState(),
                                    id
                            ));
                    return cb;
                }
        );
    }

    private BigDecimal getFallbackVar() {
        return new BigDecimal("100000");
    }

    private BigDecimal calculateOrderRiskContribution(OrderRequest order, BigDecimal currentVar) {
        BigDecimal orderNotional = order.quantity().multiply(order.price());
        BigDecimal portfolioExposure = riskModel.calculateTotalExposure();
        if (portfolioExposure.compareTo(BigDecimal.ZERO) == 0) {
            return orderNotional;
        }
        BigDecimal riskContribution = orderNotional
                .divide(portfolioExposure, 10, RoundingMode.HALF_UP)
                .multiply(currentVar);
        return riskContribution.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal getAdjustedThreshold(String instrumentId) {
        Threshold threshold = thresholdsByInstrument.computeIfAbsent(
                instrumentId,
                id -> new Threshold(id, ThresholdType.INSTRUMENT, new BigDecimal("50000"))
        );
        BigDecimal currentVolatility = estimateCurrentVolatility(instrumentId);
        BigDecimal historicalVolatility = estimateHistoricalVolatility(instrumentId);
        threshold.adjustForVolatility(currentVolatility, historicalVolatility);
        return threshold.getCurrentValue();
    }

    private BigDecimal estimateCurrentVolatility(String instrumentId) {
        return new BigDecimal("0.02");
    }

    private BigDecimal estimateHistoricalVolatility(String instrumentId) {
        return new BigDecimal("0.015");
    }

    private void updateRiskModel(OrderRequest order, BigDecimal orderNotional) {
        riskModel.updatePosition(
                order.instrumentId(),
                order.quantity(),
                orderNotional
        );
        riskModel.updateTraderExposure(order.traderId(), orderNotional);
        riskModel.updateStrategyExposure(order.strategyId(), orderNotional);
    }

    private void recalculateRiskMetrics() {
        synchronized (recalculationLock) {
            logger.info("Recalculando métricas de riesgo");
            BigDecimal var = riskModel.calculateVar();
            riskModel.reset();
            lastRiskRecalculation = Instant.now();
            logger.info("VaR recalculado: {}, Timestamp: {}", var, lastRiskRecalculation);
        }
    }

    public void recordMarketDataUpdate(String instrumentId, BigDecimal price, Instant timestamp) {
        riskModel.recordPrice(instrumentId, price, timestamp);
    }

    public void recordTrade(String instrumentId, BigDecimal quantity, BigDecimal price, 
                           String traderId, String side) {
        BigDecimal notional = quantity.abs().multiply(price);
        if ("SELL".equalsIgnoreCase(side)) {
            notional = notional.negate();
        }
        BigDecimal currentPnL = dailyPnLByTrader.getOrDefault(traderId, BigDecimal.ZERO);
        dailyPnLByTrader.put(traderId, currentPnL.add(notional));
        if (currentPnL.add(notional).compareTo(MAX_DAILY_LOSS.negate()) < 0) {
            logger.warn("Trader {} alcanzó el límite de pérdida diaria: {}", traderId, 
                       currentPnL.add(notional));
        }
    }

    public void enableCircuitBreakers(boolean enable) {
        this.enableCircuitBreakers = enable;
        logger.info("Circuit breakers {} globalmente", enable ? "habilitados" : "deshabilitados");
    }

    public RiskModel getRiskModel() {
        return riskModel;
    }

    public BigDecimal getCurrentVar() {
        return riskModel.getCurrentVar();
    }

    public Map<String, BigDecimal> getDailyPnLByTrader() {
        return Map.copyOf(dailyPnLByTrader);
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

    public record OrderRequest(
            String orderId,
            String traderId,
            String strategyId,
            String instrumentId,
            BigDecimal quantity,
            BigDecimal price,
            String side,
            Instant timestamp
    ) {}

    public record RiskScoreResult(
            String orderId,
            BigDecimal riskScore,
            BigDecimal threshold,
            boolean approved,
            BigDecimal varAtCalculation,
            Instant calculatedAt
    ) {}
}


// === ARCHIVO: src/main/java/com/pragma/riskengine/audit/AuditLogger.java ===
package com.pragma.riskengine.audit;

import com.pragma.riskengine.exception.KillSwitchActivatedException;
import com.pragma.riskengine.exception.RiskThresholdExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public class AuditLogger {
    
    private static final Logger logger = LoggerFactory.getLogger(AuditLogger.class);
    private static final DateTimeFormatter ISO_FORMATTER = 
            DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.of("UTC"));
    
    private final String componentId;
    private final String componentType;
    private final ConcurrentMap<String, AuditEntry> recentEntries;
    private final AtomicLong sequenceNumber;
    private final boolean structuredLogging;
    private final int maxRecentEntries;
    
    public AuditLogger(String componentId, String componentType) {
        this(componentId, componentType, true, 10000);
    }
    
    public AuditLogger(String componentId, String componentType, 
            boolean structuredLogging, int maxRecentEntries) {
        this.componentId = componentId;
        this.componentType = componentType;
        this.structuredLogging = structuredLogging;
        this.maxRecentEntries = maxRecentEntries;
        this.recentEntries = new ConcurrentHashMap<>();
        this.sequenceNumber = new AtomicLong(0);
    }
    
    public void logMarketDataEvent(String eventType, Map<String, String> data) {
        AuditEntry entry = new AuditEntry(
                generateSequenceId(),
                eventType,
                Instant.now(),
                null, null, null, null
        );
        data.forEach(entry::addField);
        writeEntry(entry);
    }
    
    public void logRiskDecision(String orderId, String traderId, String strategyId,
            String instrumentId, String decision, BigDecimal riskScore, 
            BigDecimal exposure, BigDecimal threshold) {
        AuditEntry entry = new AuditEntry(
                generateSequenceId(),
                "RISK_DECISION",
                Instant.now(),
                orderId, traderId, strategyId, instrumentId
        );
        entry.addField("decision", decision);
        entry.addField("riskScore", riskScore != null ? riskScore.toPlainString() : "N/A");
        entry.addField("exposure", exposure != null ? exposure.toPlainString() : "N/A");
        entry.addField("threshold", threshold != null ? threshold.toPlainString() : "N/A");
        entry.addField("mifidIICompliance", "true");
        entry.addField("decisionTimestamp", ISO_FORMATTER.format(entry.timestamp));
        
        writeEntry(entry);
    }
    
    public void logOrderRejected(String orderId, String traderId, String strategyId,
            String instrumentId, RiskThresholdExceededException exception) {
        AuditEntry entry = new AuditEntry(
                generateSequenceId(),
                "ORDER_REJECTED",
                Instant.now(),
                orderId, traderId, strategyId, instrumentId
        );
        entry.addField("rejectionReason", "THRESHOLD_EXCEEDED");
        entry.addField("thresholdId", exception.getThresholdId());
        entry.addField("thresholdType", exception.getThresholdType().name());
        entry.addField("currentValue", exception.getCurrentValue().toPlainString());
        entry.addField("thresholdValue", exception.getThresholdValue().toPlainString());
        entry.addField("exceedPercentage", exception.getExceedPercentage().toPlainString());
        entry.addField("riskMetric", exception.getRiskMetric());
        entry.addField("isCritical", String.valueOf(exception.isCritical()));
        entry.addField("auditTrail", exception.toAuditString());
        
        writeEntry(entry);
    }
    
    public void logKillSwitchActivated(KillSwitchActivatedException exception) {
        AuditEntry entry = new AuditEntry(
                generateSequenceId(),
                "KILL_SWITCH_ACTIVATED",
                exception.getActivationTime(),
                null, null, exception.getStrategyId(), null
        );
        entry.addField("algorithmId", exception.getAlgorithmId());
        entry.addField("reason", exception.getReason().name());
        entry.addField("totalExposure", exception.getTotalExposure().toPlainString());
        entry.addField("thresholdBreached", exception.getThresholdBreached().toPlainString());
        entry.addField("consecutiveBreaches", String.valueOf(exception.getConsecutiveBreaches()));
        entry.addField("triggerEventId", exception.getTriggerEventId());
        entry.addField("autoResetEnabled", String.valueOf(exception.isAutoResetEnabled()));
        entry.addField("affectedInstruments", String.join(",", exception.getAffectedInstruments()));
        entry.addField("auditTrail", exception.toAuditString());
        
        writeEntry(entry);
    }
    
    public void logThresholdAdjustment(String thresholdId, String instrumentId,
            BigDecimal oldValue, BigDecimal newValue, BigDecimal volatility, String reason) {
        AuditEntry entry = new AuditEntry(
                generateSequenceId(),
                "THRESHOLD_ADJUSTMENT",
                Instant.now(),
                null, null, null, instrumentId
        );
        entry.addField("thresholdId", thresholdId);
        entry.addField("oldValue", oldValue.toPlainString());
        entry.addField("newValue", newValue.toPlainString());
        entry.addField("adjustmentPercentage", 
                newValue.subtract(oldValue).divide(oldValue, 4, java.math.RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100)).toPlainString());
        entry.addField("volatilityFactor", volatility != null ? volatility.toPlainString() : "N/A");
        entry.addField("reason", reason);
        
        writeEntry(entry);
    }
    
    public void logVarCalculation(String calculationId, BigDecimal var, 
            BigDecimal confidenceLevel, int lookbackPeriods, Instant calculationTime) {
        AuditEntry entry = new AuditEntry(
                generateSequenceId(),
                "VAR_CALCULATION",
                calculationTime,
                null, null, null, null
        );
        entry.addField("calculationId", calculationId);
        entry.addField("var", var.toPlainString());
        entry.addField("confidenceLevel", confidenceLevel.toPlainString());
        entry.addField("lookbackPeriods", String.valueOf(lookbackPeriods));
        entry.addField("calculationTimestamp", ISO_FORMATTER.format(calculationTime));
        
        writeEntry(entry);
    }
    
    public void logMarketDataUpdate(String instrumentId, String dataSource,
            BigDecimal bid, BigDecimal ask, int bidSize, int askSize) {
        if (!structuredLogging) {
            return;
        }
        
        AuditEntry entry = new AuditEntry(
                generateSequenceId(),
                "MARKET_DATA_UPDATE",
                Instant.now(),
                null, null, null, instrumentId
        );
        entry.addField("dataSource", dataSource);
        entry.addField("bid", bid.toPlainString());
        entry.addField("ask", ask.toPlainString());
        entry.addField("spread", ask.subtract(bid).toPlainString());
        entry.addField("bidSize", String.valueOf(bidSize));
        entry.addField("askSize", String.valueOf(askSize));
        
        writeEntry(entry);
    }
    
    private void writeEntry(AuditEntry entry) {
        recentEntries.put(entry.entryId, entry);
        cleanupOldEntries();
        
        if (structuredLogging) {
            logger.info("AUDIT|{}", entry.toStructuredString());
        } else {
            logger.info("[{}] {}: {}", entry.timestamp, entry.eventType, entry.toKeyValueString());
        }
    }
    
    private void cleanupOldEntries() {
        if (recentEntries.size() > maxRecentEntries) {
            String oldestKey = recentEntries.keys().nextElement();
            recentEntries.remove(oldestKey);
        }
    }
    
    private String generateSequenceId() {
        long seq = sequenceNumber.incrementAndGet();
        return String.format("%s-%s-%d", componentId, UUID.randomUUID().toString().substring(0, 8), seq);
    }
    
    public AuditEntry getRecentEntry(String entryId) {
        return recentEntries.get(entryId);
    }
    
    public int getRecentEntryCount() {
        return recentEntries.size();
    }
    
    public static class AuditEntry {
        private final String entryId;
        private final String eventType;
        private final Instant timestamp;
        private final String orderId;
        private final String traderId;
        private final String strategyId;
        private final String instrumentId;
        private final ConcurrentMap<String, String> fields;
        
        public AuditEntry(String entryId, String eventType, Instant timestamp,
                String orderId, String traderId, String strategyId, String instrumentId) {
            this.entryId = entryId;
            this.eventType = eventType;
            this.timestamp = timestamp;
            this.orderId = orderId;
            this.traderId = traderId;
            this.strategyId = strategyId;
            this.instrumentId = instrumentId;
            this.fields = new ConcurrentHashMap<>();
        }
        
        public void addField(String key, String value) {
            fields.put(key, value);
        }
        
        public String getEntryId() {
            return entryId;
        }
        
        public String getEventType() {
            return eventType;
        }
        
        public Instant getTimestamp() {
            return timestamp;
        }
        
        public String toStructuredString() {
            StringBuilder sb = new StringBuilder();
            sb.append("entryId=").append(entryId)
              .append("|eventType=").append(eventType)
              .append("|timestamp=").append(ISO_FORMATTER.format(timestamp));
            
            if (orderId != null) sb.append("|orderId=").append(orderId);
            if (traderId != null) sb.append("|traderId=").append(traderId);
            if (strategyId != null) sb.append("|strategyId=").append(strategyId);
            if (instrumentId != null) sb.append("|instrumentId=").append(instrumentId);
            
            fields.forEach((k, v) -> sb.append("|").append(k).append("=").append(v));
            
            return sb.toString();
        }
        
        public String toKeyValueString() {
            return fields.entrySet().stream()
                    .map(e -> e.getKey() + "=" + e.getValue())
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");
        }
    }
}

// === ARCHIVO: src/main/java/com/pragma/riskengine/exception/KillSwitchActivatedException.java ===
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

// === ARCHIVO: src/main/java/com/pragma/riskengine/service/RiskScoringService.java ===
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


// === ARCHIVO: src/main/java/com/pragma/riskengine/service/KillSwitchService.java ===
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

```
