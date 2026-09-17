# AGENTS.md

Instrucciones para el agente de IA que abra este repositorio (Claude Code, Cursor, Codex, Copilot, Gemini). Se cargan solas: no hay que pegar nada en ningun chat.

## Que es este repositorio

Es el codigo base de un reto de aprendizaje de Pragma: **Diseño y Justificación de un Motor de Risk Scoring para Trading Algorítmico**.

| | |
|---|---|
| Tema | motor de risk scoring en tiempo real para trading algorítmico con circuit breakers dinámicos |
| Nivel | master-l2 |
| Chapter | Generico |
| Especialidad | Inferido del contexto |
| Stack | Java 21 / LMAX Disruptor 4.0 |
| Patron arquitectonico | event-driven con anillo de buffers lock-free y sharding por instrumento |
| Tiempo estimado | 2 semanas |

## Tu tarea

Dejar este proyecto en estado **verificable**: que el comando de verificacion corra sin errores. Escribi los archivos en disco, en este repositorio. No generes ZIPs ni archivos adjuntos.

En orden:

1. Corre `el comando de build o arranque canonico del stack elegido` y mira que falla.
2. Completa lo que falte de la lista de abajo: manifiesto de dependencias, punto de entrada, capa de interfaz y las capas del patron declarado.
3. Arregla SOLO los errores que impiden compilar o arrancar.
4. Volve a correr `el comando de build o arranque canonico del stack elegido` hasta que pase.
5. Pará ahí.

## Regla dura: las fases son trabajo del humano

**PROHIBIDO implementar los entregables de las fases.** El valor del reto esta en que la persona los resuelva. Tu trabajo es que tenga un proyecto que arranca; el hueco pedagogico se queda como esta.

No resuelvas nada de esto:

- **Fase 1 — Exploración y Modelado Inicial**: Diagrama de relaciones y descripción del modelo inicial.
- **Fase 2 — Evaluación de Decisiones de Diseño**: Documento de decisiones de diseño con pros/contras y justificación.
- **Fase 3 — Implementación y Optimización**: Motor de risk scoring implementado y optimizado.

Distincion operativa:

- **Arreglar** (si): import faltante, tipo que no existe, dependencia sin declarar, error de sintaxis, archivo referenciado que no existe.
- **No tocar** (no): logica de negocio incompleta, validaciones ausentes, secretos hardcodeados, APIs deprecadas que funcionan, concurrencia insegura, patrones mejorables. Eso es lo que la persona tiene que encontrar.

## Lo que falta y tenes que completar

### 1. Referencias colgando (77)

Salieron de un analisis estatico del codigo que SI esta en el repo. Cada una rompe la compilacion:

- [ ] `src/main/java/com/pragma/riskengine/Main.java` — `KillSwitchActivatedException`
      KillSwitchActivatedException se usa en el cuerpo del archivo pero no esta importado. El proyecto lo declara en com.pragma.riskengine.exception.KillSwitchActivatedException.
- [ ] `src/main/java/com/pragma/riskengine/model/Threshold.java` — `Duration`
      Duration se usa en el cuerpo del archivo pero no esta importado. El proyecto lo declara en com.pragma.riskengine.exception.Duration.
- [ ] `src/main/java/com/pragma/riskengine/disruptor/OrderEvent.java` — `Duration`
      Duration se usa en el cuerpo del archivo pero no esta importado. El proyecto lo declara en com.pragma.riskengine.exception.Duration.
- [ ] `src/main/java/com/pragma/riskengine/disruptor/OrderEventHandler.java` — `Threshold`
      Threshold se usa en el cuerpo del archivo pero no esta importado. El proyecto lo declara en com.pragma.riskengine.model.Threshold.
- [ ] `src/main/java/com/pragma/riskengine/replay/ReplayService.java` — `OrderSide`
      OrderSide se usa en el cuerpo del archivo pero no esta importado. El proyecto lo declara en com.pragma.riskengine.disruptor.OrderSide.
- [ ] `src/main/java/com/pragma/riskengine/replay/ReplayService.java` — `Duration`
      Duration se usa en el cuerpo del archivo pero no esta importado. El proyecto lo declara en com.pragma.riskengine.exception.Duration.
- [ ] `src/test/java/com/pragma/riskengine/disruptor/OrderEventHandlerTest.java` — `OrderStatus`
      OrderStatus se usa en el cuerpo del archivo pero no esta importado. El proyecto lo declara en com.pragma.riskengine.adapter.exchange.OrderStatus.
- [ ] `src/main/java/com/pragma/riskengine/service/RiskScoringService.java` — `ResilienceConfig`
      El import com.pragma.riskengine.config.ResilienceConfig no se usa en ningun lado del cuerpo del archivo. Se puede eliminar.
- [ ] `src/test/java/com/pragma/riskengine/disruptor/OrderEventHandlerTest.java` — `RiskThresholdExceededException`
      El import com.pragma.riskengine.exception.RiskThresholdExceededException no se usa en ningun lado del cuerpo del archivo. Se puede eliminar.
- [ ] `src/test/java/com/pragma/riskengine/disruptor/OrderEventHandlerTest.java` — `KillSwitchActivatedException`
      El import com.pragma.riskengine.exception.KillSwitchActivatedException no se usa en ningun lado del cuerpo del archivo. Se puede eliminar.
- [ ] `src/main/java/com/pragma/riskengine/Main.java` — `DisruptorConfig.createDisruptor`
      Se invoca `createDisruptor` sobre `DisruptorConfig`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/Main.java` — `AuditLogger.logError`
      Se invoca `logError` sobre `AuditLogger`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/Main.java` — `MarketDataFeedAdapter.connect`
      Se invoca `connect` sobre `MarketDataFeedAdapter`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/Main.java` — `RiskScoringService.recalculateVolatility`
      Se invoca `recalculateVolatility` sobre `RiskScoringService`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/Main.java` — `KillSwitchService.recalculateThresholds`
      Se invoca `recalculateThresholds` sobre `KillSwitchService`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/Main.java` — `RiskScoringService.getCurrentVolatility`
      Se invoca `getCurrentVolatility` sobre `RiskScoringService`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/Main.java` — `AuditLogger.logWarning`
      Se invoca `logWarning` sobre `AuditLogger`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/Main.java` — `OrderEvent.setInstrument`
      Se invoca `setInstrument` sobre `OrderEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/Main.java` — `OrderEvent.setPrice`
      Se invoca `setPrice` sobre `OrderEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/Main.java` — `OrderEvent.setTimestamp`
      Se invoca `setTimestamp` sobre `OrderEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/Main.java` — `MarketDataFeedAdapter.disconnect`
      Se invoca `disconnect` sobre `MarketDataFeedAdapter`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/model/RiskModel.java` — `Position.quantity`
      Se invoca `quantity` sobre `Position`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/model/RiskModel.java` — `Position.notional`
      Se invoca `notional` sobre `Position`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/model/Threshold.java` — `Duration.compareTo`
      Se invoca `compareTo` sobre `Duration`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/disruptor/OrderEventHandler.java` — `RiskScoringService.validateThresholds`
      Se invoca `validateThresholds` sobre `RiskScoringService`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/disruptor/MarketDataEventHandler.java` — `MarketDataEvent.getEventType`
      Se invoca `getEventType` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/disruptor/MarketDataEventHandler.java` — `MarketDataEvent.getInstrumentId`
      Se invoca `getInstrumentId` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/disruptor/MarketDataEventHandler.java` — `MarketDataEvent.getPrice`
      Se invoca `getPrice` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/disruptor/MarketDataEventHandler.java` — `MarketDataEvent.getTimestamp`
      Se invoca `getTimestamp` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/disruptor/MarketDataEventHandler.java` — `MarketDataEvent.getQuantity`
      Se invoca `getQuantity` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/disruptor/MarketDataEventHandler.java` — `MarketDataEvent.getBidLevels`
      Se invoca `getBidLevels` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/disruptor/MarketDataEventHandler.java` — `MarketDataEvent.getAskLevels`
      Se invoca `getAskLevels` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/disruptor/MarketDataEventHandler.java` — `MarketDataEvent.getVolatility`
      Se invoca `getVolatility` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/service/RiskScoringService.java` — `OrderRequest.strategyId`
      Se invoca `strategyId` sobre `OrderRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/service/RiskScoringService.java` — `OrderRequest.price`
      Se invoca `price` sobre `OrderRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/service/RiskScoringService.java` — `OrderRequest.quantity`
      Se invoca `quantity` sobre `OrderRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/service/RiskScoringService.java` — `OrderRequest.traderId`
      Se invoca `traderId` sobre `OrderRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/service/RiskScoringService.java` — `OrderRequest.instrumentId`
      Se invoca `instrumentId` sobre `OrderRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/service/RiskScoringService.java` — `OrderRequest.orderId`
      Se invoca `orderId` sobre `OrderRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/service/KillSwitchService.java` — `StrategyKillSwitch.active`
      Se invoca `active` sobre `StrategyKillSwitch`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/service/KillSwitchService.java` — `StrategyKillSwitch.activatedAt`
      Se invoca `activatedAt` sobre `StrategyKillSwitch`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/adapter/marketdata/MarketDataFeedAdapter.java` — `OrderBookUpdate.instrumentId`
      Se invoca `instrumentId` sobre `OrderBookUpdate`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/adapter/marketdata/MarketDataFeedAdapter.java` — `OrderBookUpdate.bids`
      Se invoca `bids` sobre `OrderBookUpdate`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/adapter/marketdata/MarketDataFeedAdapter.java` — `OrderBook.updateBid`
      Se invoca `updateBid` sobre `OrderBook`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/adapter/marketdata/MarketDataFeedAdapter.java` — `OrderBookUpdate.asks`
      Se invoca `asks` sobre `OrderBookUpdate`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/adapter/marketdata/MarketDataFeedAdapter.java` — `OrderBook.updateAsk`
      Se invoca `updateAsk` sobre `OrderBook`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/adapter/marketdata/MarketDataFeedAdapter.java` — `MarketDataListener.onOrderBookUpdate`
      Se invoca `onOrderBookUpdate` sobre `MarketDataListener`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/adapter/marketdata/MarketDataFeedAdapter.java` — `Trade.instrumentId`
      Se invoca `instrumentId` sobre `Trade`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/adapter/marketdata/MarketDataFeedAdapter.java` — `Trade.price`
      Se invoca `price` sobre `Trade`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/adapter/marketdata/MarketDataFeedAdapter.java` — `Trade.quantity`
      Se invoca `quantity` sobre `Trade`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/adapter/marketdata/MarketDataFeedAdapter.java` — `MarketDataListener.onTrade`
      Se invoca `onTrade` sobre `MarketDataListener`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/adapter/marketdata/MarketDataFeedAdapter.java` — `OrderBook.getBestBid`
      Se invoca `getBestBid` sobre `OrderBook`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/adapter/marketdata/MarketDataFeedAdapter.java` — `OrderBook.getBestAsk`
      Se invoca `getBestAsk` sobre `OrderBook`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/adapter/exchange/ExchangeAdapter.java` — `OrderResponse.status`
      Se invoca `status` sobre `OrderResponse`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/adapter/exchange/ExchangeAdapter.java` — `CircuitBreakerAdapter.isOpen`
      Se invoca `isOpen` sobre `CircuitBreakerAdapter`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/adapter/exchange/ExchangeAdapter.java` — `PendingOrder.orderId`
      Se invoca `orderId` sobre `PendingOrder`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/adapter/exchange/ExchangeAdapter.java` — `PendingOrder.sequenceId`
      Se invoca `sequenceId` sobre `PendingOrder`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/adapter/exchange/ExchangeAdapter.java` — `CircuitBreakerAdapter.recordFailure`
      Se invoca `recordFailure` sobre `CircuitBreakerAdapter`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/adapter/exchange/ExchangeAdapter.java` — `CircuitBreakerAdapter.recordSuccess`
      Se invoca `recordSuccess` sobre `CircuitBreakerAdapter`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/audit/AuditLogger.java` — `AuditEntry.addField`
      Se invoca `addField` sobre `AuditEntry`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/audit/AuditLogger.java` — `KillSwitchActivatedException.getThresholdId`
      Se invoca `getThresholdId` sobre `KillSwitchActivatedException`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/audit/AuditLogger.java` — `KillSwitchActivatedException.getThresholdType`
      Se invoca `getThresholdType` sobre `KillSwitchActivatedException`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/audit/AuditLogger.java` — `KillSwitchActivatedException.getCurrentValue`
      Se invoca `getCurrentValue` sobre `KillSwitchActivatedException`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/audit/AuditLogger.java` — `KillSwitchActivatedException.getThresholdValue`
      Se invoca `getThresholdValue` sobre `KillSwitchActivatedException`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/audit/AuditLogger.java` — `KillSwitchActivatedException.getExceedPercentage`
      Se invoca `getExceedPercentage` sobre `KillSwitchActivatedException`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/audit/AuditLogger.java` — `KillSwitchActivatedException.getRiskMetric`
      Se invoca `getRiskMetric` sobre `KillSwitchActivatedException`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/audit/AuditLogger.java` — `KillSwitchActivatedException.isCritical`
      Se invoca `isCritical` sobre `KillSwitchActivatedException`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/audit/AuditLogger.java` — `AuditEntry.toStructuredString`
      Se invoca `toStructuredString` sobre `AuditEntry`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/audit/AuditLogger.java` — `AuditEntry.toKeyValueString`
      Se invoca `toKeyValueString` sobre `AuditEntry`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/replay/ReplayService.java` — `ReplayResult.setTotalEventsFound`
      Se invoca `setTotalEventsFound` sobre `ReplayResult`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/replay/ReplayService.java` — `ReplayListener.onEventReplayed`
      Se invoca `onEventReplayed` sobre `ReplayListener`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/replay/ReplayService.java` — `ReplayResult.addProcessedEvent`
      Se invoca `addProcessedEvent` sobre `ReplayResult`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/replay/ReplayService.java` — `ReplayResult.setSuccess`
      Se invoca `setSuccess` sobre `ReplayResult`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/replay/ReplayService.java` — `ReplayResult.setErrorMessage`
      Se invoca `setErrorMessage` sobre `ReplayResult`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/replay/ReplayService.java` — `ReplayResult.setIncidentId`
      Se invoca `setIncidentId` sobre `ReplayResult`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/pragma/riskengine/disruptor/OrderEventHandlerTest.java` — `OrderEvent.status`
      Se invoca `status` sobre `OrderEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/pragma/riskengine/disruptor/OrderEventHandlerTest.java` — `OrderEvent.rejectionReason`
      Se invoca `rejectionReason` sobre `OrderEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.

### Presentes (21)

- `pom.xml`
- `src/main/java/com/pragma/riskengine/Main.java`
- `src/main/resources/application.properties`
- `src/main/java/com/pragma/riskengine/model/RiskModel.java`
- `src/main/java/com/pragma/riskengine/model/Threshold.java`
- `src/main/java/com/pragma/riskengine/disruptor/OrderEvent.java`
- `src/main/java/com/pragma/riskengine/disruptor/OrderEventHandler.java`
- `src/main/java/com/pragma/riskengine/disruptor/MarketDataEventHandler.java`
- `src/main/java/com/pragma/riskengine/service/RiskScoringService.java`
- `src/main/java/com/pragma/riskengine/service/KillSwitchService.java`
- `src/main/java/com/pragma/riskengine/adapter/marketdata/MarketDataFeedAdapter.java`
- `src/main/java/com/pragma/riskengine/adapter/exchange/ExchangeAdapter.java`
- `src/main/java/com/pragma/riskengine/config/DisruptorConfig.java`
- `src/main/java/com/pragma/riskengine/config/ResilienceConfig.java`
- `src/main/java/com/pragma/riskengine/exception/RiskThresholdExceededException.java`
- `src/main/java/com/pragma/riskengine/exception/KillSwitchActivatedException.java`
- `src/main/java/com/pragma/riskengine/audit/AuditLogger.java`
- `src/main/java/com/pragma/riskengine/replay/ReplayService.java`
- `src/test/java/com/pragma/riskengine/disruptor/OrderEventHandlerTest.java`
- `src/test/java/com/pragma/riskengine/service/RiskScoringServiceTest.java`
- `src/test/java/com/pragma/riskengine/service/KillSwitchServiceTest.java`

### Capas del patron declarado

Cada una tiene que existir como directorio real con al menos un archivo. Codigo plano en la raiz no satisface el patron.

- `src/main/java/com/pragma/riskengine`
- `src/main/java/com/pragma/riskengine/disruptor`
- `src/main/java/com/pragma/riskengine/model`
- `src/main/java/com/pragma/riskengine/service`
- `src/main/java/com/pragma/riskengine/adapter/marketdata`
- `src/main/java/com/pragma/riskengine/adapter/exchange`
- `src/main/java/com/pragma/riskengine/config`
- `src/main/java/com/pragma/riskengine/exception`
- `src/main/resources`
- `src/test/java/com/pragma/riskengine`
- `src/test/resources`

## Verificacion

```bash
el comando de build o arranque canonico del stack elegido
```

Ese comando pasando es la definicion de "terminado" para vos.

## Convenciones que tenes que respetar

- Un solo ecosistema: no declares librerias de otro lenguaje ni mezcles gestores de paquetes.
- Toda libreria que uses tiene que estar declarada en el manifiesto de dependencias.
- Todo import declarado tiene que usarse; todo tipo usado tiene que existir o venir de una dependencia declarada.
- El patron es **event-driven con anillo de buffers lock-free y sharding por instrumento**: los contratos (interfaces, puertos) los define la capa interna y los implementa la externa, nunca al revés.
- Los archivos que crees llevan implementacion real, no stubs: sin `TODO`, sin cuerpos vacios, sin `// getters y setters`.

## Contexto del candidato

Sirve para calibrar el nivel del codigo, no para resolver las fases.

- Brecha que el reto ataca: Sistema master-l2 que evalúa el riesgo de cada orden antes de enviarla al exchange, en menos de 500 microsegundos p99. Consume feed de market data (nivel 2 orderbook + trades), mantiene un modelo de VaR intraday, aplica límites por trader/estrategia/instrumento en tiempo real, y dispara circuit breakers dinámicos que bloquean nuevas órdenes cuando la exposición supera thresholds calibrados por volatilidad. Debe justificar el uso de estructuras lock-free vs mutex, la elección entre C++ vs Rust vs Java LMAX Disruptor, cómo garantiza la consistencia entre múltiples risk engines corriendo en paralelo (consensus vs sharding por instrument), la política de kill switch cuando detecta un algoritmo que se comporta de forma anómala, y la estrategia de replay determinístico para post-mortem de incidents. Incluye compliance con regulaciones MiFID II para trazabilidad de decisiones de riesgo.

---

*Generado por Challenge Generator — Pragma. `README.md` tiene el enunciado completo del reto para la persona. `PROMPT_MEJORA.md` es la variante para pegar en un chat, si se prefiere ese flujo.*
