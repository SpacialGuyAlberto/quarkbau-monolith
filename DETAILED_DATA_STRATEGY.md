# Estrategia de Persistencia Políglota: PostgreSQL vs Neo4j

Este documento detalla qué motor de base de datos debe manejar cada entidad y funcionalidad de QuarkBau para maximizar la eficiencia y la capacidad analítica.

## Tabla de Distribución de Datos

| Área Funcional | Entidad / Concepto | PostgreSQL (Transaccional) | Neo4j (Grafos / Inteligencia) | Razón Técnica |
| :--- | :--- | :---: | :---: | :--- |
| **Identidad** | `User`, `Role`, `Org` | **Principal** | Referencia (ID) | Requiere ACID y seguridad estricta para login. |
| **Planificación** | `Project`, `Organization` | **Principal** | Nodo | El grafo los usa como "anclas" o contextos superiores. |
| **Geometría** | `Segment`, `GisFeature` | **Geometría Real** | Nodo (Metadato) | Postgres (PostGIS/JSONB) maneja coordenadas; el Grafo maneja la *conexión* entre tramos. |
| **Ejecución** | `Crew`, `Subcontractor` | Perfil y Datos Legales | Nodo de Recurso | Neo4j analiza disponibilidad y asignaciones óptimas. |
| **Logística** | `Material`, `Machine`, `SKU` | Stock y Precios | Nodo de Inventario | Neo4j rastrea el "linaje" (dónde se usó qué material). |
| **Finanzas** | `WorkLog`, `BillingUnit` | **Todo** | No recomendado | Los cálculos contables deben ser 100% precisos y auditables en SQL. |
| **Cronograma** | `CrewAssignment`, `Schedule` | Fechas en Calendario | **Camino Crítico** | Neo4j detecta cuellos de botella y "efecto dominó" de retrasos. |
| **Riesgos** | `RiskScore`, `Prediction` | Historial de Riesgos | **Motor de Cálculo** | El grafo identifica patrones de riesgo basados en topología (ej: "zona de suelo rocoso"). |
| **Campo** | `Evidence (Photos)`, `Safety` | Almacenamiento Metadato | No recomendado | No hay valor relacional en una lista de fotos; es puramente lineal. |

---

## Casos de Uso Específicos (Backend + Dashboard)

### 1. ¿Dónde vive la "Verdad"? -> PostgreSQL
Si el **Web Dashboard** necesita mostrar una tabla con los nombres de todos los subcontratistas y sus calificaciones, la consulta va a **PostgreSQL**. Es rápido, paginado y predecible.

### 2. ¿Dónde vive la "Certeza" del Negocio? -> Neo4j
Si el usuario en el dashboard pregunta: *"Si la Cuadrilla Alpha se retrasa 3 días en la Calle X, ¿cuánto dinero en penalizaciones pierdo en el Proyecto B?"*:
1.  **Neo4j** calcula qué segmentos siguen a la Calle X y quiénes los operan.
2.  **PostgreSQL** aporta los precios de `BillingUnit` para calcular el coste final.

### 3. Sincronización (The Bridge)
El **Web Dashboard** no necesita saber que hay dos bases de datos. El API del Monolito actúa como fachada:
*   `GET /api/risk-analysis` -> Consulta a Neo4j por debajo.
*   `GET /api/inventory` -> Consulta a PostgreSQL por debajo.

## Recomendación para el MVP
Para impresionar a las empresas, usa **PostgreSQL** para que el dashboard se vea "limpio" y funcional, y usa **Neo4j** para alimentar un widget de "Impacto de Riesgo" o "Red de Dependencias", que es lo que ningún otro software de construcción les va a mostrar.
