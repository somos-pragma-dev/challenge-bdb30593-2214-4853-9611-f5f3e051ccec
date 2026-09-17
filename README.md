# Diseño y Justificación de un Motor de Risk Scoring para Trading Algorítmico

Diseña un motor de risk scoring en tiempo real para evaluar el riesgo de cada orden antes de enviarla al exchange. El sistema debe procesar las órdenes en menos de 500 microsegundos en el percentil 99, consumir feed de market data (nivel 2 orderbook + trades), mantener un modelo de VaR intraday, aplicar límites por trader/estrategia/instrumento en tiempo real, y disparar circuit breakers dinámicos que bloquean nuevas órdenes cuando la exposición supera thresholds calibrados por volatilidad. El sistema debe cumplir con las regulaciones MiFID II para trazabilidad de decisiones de riesgo.

## Informacion General

| Campo | Valor |
|-------|-------|
| **Tema** | motor de risk scoring en tiempo real para trading algorítmico con circuit breakers dinámicos |
| **Nivel** | master-l2 |
| **Tipo** | mixed |
| **Tiempo estimado** | 2 semanas |

## Fases del Reto

### Fase 0: Configuración del Proyecto

**Objetivo:** Obtener el proyecto base funcional enviando el Código Base a un asistente de IA, que lo analizará, corregirá errores y generará un ZIP listo para usar.

**Tiempo estimado:** 15-30 minutos

**Instrucciones:**

- Asegúrate de tener instalado para ejecutar el proyecto: JDK 17+, Maven 3.9+, IDE con soporte Java.
- Copia todo el contenido del campo **Código Base** de este reto — incluyendo el texto de instrucciones que aparece al inicio.
- Abre un asistente de IA (Claude en claude.ai, ChatGPT o Gemini — se recomienda Claude), pega el contenido copiado en el chat y envíalo.
- El asistente analizará los archivos, corregirá errores y generará un archivo ZIP descargable. Descárgalo y extráelo en la carpeta donde quieras trabajar.
- Ejecuta `mvn compile` en la raíz. Si no hay errores, estás listo.

**Entregable:** El proyecto compila/arranca sin errores.

<details>
<summary>Pistas de conocimiento</summary>

- Copia el Código Base completo incluyendo el texto de instrucciones al inicio — esas instrucciones le indican al asistente exactamente qué hacer con los archivos.
- Si el asistente no genera el ZIP automáticamente al terminar el análisis, escríbele: "genera el ZIP ahora".
- Si el proyecto tiene errores al arrancar, comparte el mensaje de error con el mismo asistente para que lo corrija.

</details>

### Fase 1: Exploración y Modelado Inicial

**Objetivo:** Comprender el problema y modelar el sistema inicial.

**Tiempo estimado:** 3 días

**Instrucciones:**

- Identifica y describe los actores clave del sistema (e.g., feed de market data, risk engine, trader, exchange).
- Modela el flujo de datos y las interacciones entre los componentes.
- Define los umbrales y las condiciones para los circuit breakers.

**Entregable:** Diagrama de relaciones y descripción del modelo inicial.

<details>
<summary>Pistas de conocimiento</summary>

- Considera las propiedades operativas del sistema (latencia, consistencia, disponibilidad).
- Piensa en los edge cases y cómo el sistema debe manejarlos.

</details>

### Fase 2: Evaluación de Decisiones de Diseño

**Objetivo:** Evaluar y justificar las decisiones de diseño críticas.

**Tiempo estimado:** 5 días

**Instrucciones:**

- Evalúa y justifica el uso de estructuras lock-free vs mutex.
- Justifica la elección entre C++ vs Rust vs Java LMAX Disruptor.
- Diseña la estrategia para garantizar la consistencia entre múltiples risk engines corriendo en paralelo (consensus vs sharding por instrument).
- Define la política de kill switch cuando detecta un algoritmo que se comporta de forma anómala.
- Diseña la estrategia de replay determinístico para post-mortem de incidents.

**Entregable:** Documento de decisiones de diseño con pros/contras y justificación.

<details>
<summary>Pistas de conocimiento</summary>

- Considera los trade-offs entre consistencia y disponibilidad.
- Evalúa la escalabilidad y la latencia de las diferentes opciones.

</details>

### Fase 3: Implementación y Optimización

**Objetivo:** Implementar y optimizar el motor de risk scoring.

**Tiempo estimado:** 5 días

**Instrucciones:**

- Implementa el motor de risk scoring basado en las decisiones de diseño.
- Optimiza el rendimiento para asegurar que el sistema procesa las órdenes en menos de 500 microsegundos en el percentil 99.
- Asegura la trazabilidad de decisiones de riesgo conforme a las regulaciones MiFID II.

**Entregable:** Motor de risk scoring implementado y optimizado.

<details>
<summary>Pistas de conocimiento</summary>

- Utiliza técnicas de optimización de rendimiento (e.g., profiling, caching).
- Asegura la trazabilidad de decisiones mediante logs y auditorías.

</details>

## Dimensiones Evaluadas

- **queEs**: ¿Qué es un motor de risk scoring y cuáles son sus componentes clave?
- **paraQueSirve**: ¿Para qué sirve el motor de risk scoring en el contexto del trading algorítmico?
- **comoSeUsa**: ¿Cómo se usa el motor de risk scoring para evaluar el riesgo de las órdenes en tiempo real?
- **erroresComunes**: ¿Cuáles son los errores comunes que pueden ocurrir en la implementación del motor de risk scoring y cómo se pueden mitigar?
- **queDecisionesImplica**: ¿Qué decisiones de diseño implica la implementación del motor de risk scoring y cómo se justifican?

## Criterios de Evaluacion

- Comprensión y modelado del problema.
- Evaluación y justificación de decisiones de diseño.
- Implementación y optimización del motor de risk scoring.
- Cumplimiento de las regulaciones MiFID II para trazabilidad de decisiones de riesgo.

## Como trabajar con un asistente de IA

Hay dos caminos, elegi uno:

- **AGENTS.md** (recomendado) — instrucciones nativas del repo. Abri esta carpeta con tu agente local (Claude Code, Cursor, Codex, Copilot, Gemini) y las carga solo. Sabe que archivos faltan y con que comando se verifica, y completa el scaffold escribiendo en disco.
- **PROMPT_MEJORA.md** — para copiar y pegar en un chat (claude.ai, ChatGPT). Devuelve un ZIP con el proyecto. Sirve si no tenes un agente en el IDE.

Ninguno de los dos resuelve las fases del reto: eso es tu trabajo.

## Verificacion

El proyecto esta listo para trabajar cuando este comando corre sin errores:

```bash
el comando de build o arranque canonico del stack elegido
```

---

*Reto generado automaticamente por Challenge Generator - Pragma*
