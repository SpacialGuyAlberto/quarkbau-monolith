# Plan de Implementación: Reglas de Segmentación y Gestión de Obra

Este documento detalla los pasos de implementación para integrar las reglas de segmentación orientadas a procesos reales de construcción FTTx (Tiefbau y montaje) dentro de la arquitectura de **QuarkBau**.

---

## 1. Modificaciones al Modelo de Datos (Base de Datos & JPA)

Actualmente, el sistema necesita evolucionar de una estructura plana a una jerárquica, soportando además múltiples fases por segmento y geometrías dinámicas.

### 1.1. Jerarquía: Projekt -> Zentraler Verteiler y DP Bereiche
**Por qué es correcto:** Un proyecto completo (ej. una ciudad) es inmanejable como una lista plana de segmentos. La fibra óptica es una red topológica. Agrupar la obra por áreas de influencia de un POP (Point of Presence) o un NVt / DP (Netzverteiler / Distribution Point) facilita la asignación de recursos y la facturación.
**Recomendación:** La estructura de datos debe ser: `Project` (Proyecto global) ➡️ `Area/Cluster` (Asociado a un POP/DP) ➡️ `Segments` (Las zanjas y cables dentro de esa área).

- **`NetworkArea` (Entidad):** Representa el "DP Bereich" o área de influencia. Usar una geometría 2D (Polígono) es lo correcto porque permite saber qué casas y qué calles pertenecen a un DP específico.
  - `id`: UUID
  - `projectId`: Relación al Proyecto.
  - `type`: Enum (`POP_AREA`, `DP_AREA`).
  - `boundary`: Geometría tipo `POLYGON` (PostGIS) para limitar 2D.

### 1.2. Longitud y Dimensionalidad de los Segmentos
**Sobre los 100m y geometría:** Para el área de cobertura (*Bereich*), usar una geometría 2D (Polígono) es lo correcto. Para los segmentos individuales (la zanja que se excava), lo ideal es una 1D (Polyline/Vector).
Limitar la longitud a ~100m es una excelente idea operativa. En obra civil, esto se llama *Baulos* o *Abschnitt*. Tramos más cortos permiten reportar progreso diario (ej. "hoy excavamos 80m") de forma mucho más precisa que tener un segmento de 1km que se queda "En Progreso" durante semanas.

- **`Segment` (Entidad):** Actualización de la entidad existente para representar la 1D Polyline.
  - `networkAreaId`: Relación al área padre.
  - `plannedGeometry`: `LINESTRING` (El plano original).
  - `asBuiltGeometry`: `LINESTRING` (El plano real ejecutado - *Redlining*).
  - `length`: Calculado basado en la geometría (Validación suave de ~100m).
  - `correctionReason`: Relación a Enum (ver abajo).

### 1.3. Gestión de Tareas y Cuadrillas
- **`SegmentTask` (Nueva Entidad):** Representa cada fase de la obra en un segmento.
  - `id`: UUID
  - `segmentId`: Relación al Segmento.
  - `phase`: Enum (`EXCAVATION`, `PIPING`, `ASPHALT`, `FIBER_BLOWING`, `INSTALLATION`).
  - `crewId`: Relación a la entidad Crew (Subcontrata o interna).
  - `status`: Enum (`PENDING`, `IN_PROGRESS`, `BLOCKED`, `COMPLETED`).
  - `evidenceId`: Relación a los *Nachweisen* (Fotos, firmas).

### 1.4. Nuevos Enumeradores (Enums)
Crear los siguientes Enums en Java (`com.quarkbau.monolith.domain.enums`):
```java
public enum CorrectionReason {
    UNFORESEEN_OBSTACLE,     // Obstáculo imprevisto (ej. roca, tubo no documentado)
    CUSTOMER_REQUEST,        // Petición del cliente/ayuntamiento
    WRONG_ORIGINAL_PLAN,     // Planimetría original incorrecta
    OPTIMIZATION             // Mejora del trazado por eficiencia
}

public enum SegmentPhase {
    EXCAVATION, CABLE_LAYING, ASPHALT, FIBER_BLOWING, INSTALLATION
}
```

---

## 2. Lógica de Negocio y Servicios (Spring Boot)

### 2.1. Gestión de Correcciones (*Redlining*)
Crear un servicio para manejar las actualizaciones del Bauleiter.
- **Método:** `updateSegmentGeometry(UUID segmentId, LineString newGeometry, CorrectionReason reason)`
- **Lógica:**
  1. Validar que el usuario tiene rol `BAULEITER`.
  2. Actualizar el campo `asBuiltGeometry`.
  3. Registrar el `CorrectionReason`.
  4. Disparar un evento de dominio: `SegmentGeometryCorrectedEvent`.

### 2.2. Sistema de Notificaciones
- Crear un listener (`SegmentCorrectionListener`) que escuche el `SegmentGeometryCorrectedEvent`.
- El listener debe generar una notificación (email o in-app) dirigida al usuario con rol `PROJECT_MANAGER` detallando el segmento, la diferencia en metros y la razón.

### 2.3. Validación de Longitud (Regla de los 100m)
- En el servicio de creación de segmentos, añadir una validación: si la longitud de la geometría excede ~100m, emitir un *Warning* o sugerir el particionado del segmento (a menos que un flag indique que es una calle ininterrumpida).

---

## 3. Seguridad y Control de Accesos (Spring Security)

Configurar las reglas de RBAC para los endpoints REST.

- **`ROLE_TEAMLEITER`:**
  - Puede: GET segmentos asignados a su Crew, UPDATE `SegmentTask` (cambiar estado a In Progress/Done), POST *Nachweisen* (evidencias).
  - NO puede: Crear segmentos, editar geometrías, reasignar cuadrillas.
  
- **`ROLE_BAULEITER`:**
  - Puede: UPDATE geometrías (`asBuiltGeometry`), actualizar `CorrectionReason`, visualizar todo el proyecto.
  - NO puede: Crear proyectos globales (reservado para PM).

---

## 4. Endpoints del API REST

Nuevos endpoints necesarios para soportar el flujo:

```http
# Reporte de avance por el Teamleiter
PATCH /api/v1/segments/{segmentId}/tasks/{taskId}/status
Body: { "status": "COMPLETED", "evidenceIds": [...] }

# Redlining por el Bauleiter
PUT /api/v1/segments/{segmentId}/geometry
Body: { 
  "asBuiltGeometry": { "type": "LineString", "coordinates": [...] },
  "correctionReason": "UNFORESEEN_OBSTACLE"
}
```

---

## 5. Cambios en el Frontend (Mobile & Web)

### 5.1. Mobile App (Flutter)
- **Vista Teamleiter:** Lista de tareas por hacer (`SegmentTask`) filtradas por su `Crew_ID`. Botones grandes de "Iniciar" y "Completar" con captura de cámara obligatoria.
- **Vista Bauleiter:** Mapa interactivo. Al tocar un segmento, habilitar un modo de "Edición de Vértices" para arrastrar los puntos `p1`, `p2`, etc. Al guardar, mostrar un Modal pidiendo seleccionar el `CorrectionReason`.

### 5.2. Web Dashboard (Angular)
- Actualizar el mapa principal para pintar `plannedGeometry` (ej. línea punteada azul) y `asBuiltGeometry` (línea sólida verde).
- Añadir un panel de alertas/inbox para el Project Manager que reciba las notificaciones de cambios en los planos.
