-- ==============================================================================
-- Tabla: crew_assignments
-- Regla de negocio: Cada asignación corresponde a 1 día de trabajo para una cuadrilla.
-- Puede asociarse a un tramo (segment_id) o a un nodo de red (node_id + node_type).
-- ==============================================================================

-- 1. Creación de la tabla
CREATE TABLE IF NOT EXISTS crew_assignments (
    id BIGSERIAL PRIMARY KEY,
    crew_id BIGINT NOT NULL,
    segment_id BIGINT,
    node_id BIGINT,
    node_type VARCHAR(50),
    node_name VARCHAR(255),
    assigned_date DATE NOT NULL,
    start_time TIME,
    end_time TIME,
    status VARCHAR(50) NOT NULL DEFAULT 'SCHEDULED',
    notes TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    -- Llaves foráneas
    CONSTRAINT fk_crew_assignment_crew FOREIGN KEY (crew_id) REFERENCES crews(id) ON DELETE CASCADE,
    CONSTRAINT fk_crew_assignment_segment FOREIGN KEY (segment_id) REFERENCES segments(id) ON DELETE CASCADE,

    -- Restricciones de validación
    CONSTRAINT chk_assignment_target CHECK (segment_id IS NOT NULL OR node_id IS NOT NULL),
    CONSTRAINT chk_assignment_status CHECK (status IN ('SCHEDULED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED')),
    CONSTRAINT chk_assignment_time CHECK (start_time IS NULL OR end_time IS NULL OR end_time > start_time)
);

-- 2. Restricción de unicidad para evitar doble asignación de la cuadrilla en el mismo día (excepto canceladas)
CREATE UNIQUE INDEX IF NOT EXISTS uq_crew_date_active 
ON crew_assignments (crew_id, assigned_date) 
WHERE status != 'CANCELLED';

-- 3. Índices para rendimiento en calendarios y filtros
CREATE INDEX IF NOT EXISTS idx_crew_assignments_crew_date ON crew_assignments (crew_id, assigned_date);
CREATE INDEX IF NOT EXISTS idx_crew_assignments_segment_date ON crew_assignments (segment_id, assigned_date);
CREATE INDEX IF NOT EXISTS idx_crew_assignments_node_date ON crew_assignments (node_id, node_type, assigned_date);
CREATE INDEX IF NOT EXISTS idx_crew_assignments_date_range ON crew_assignments (assigned_date);

-- ==============================================================================
-- Consultas SQL Más Frecuentes
-- ==============================================================================

-- A. Insertar nueva asignación diaria para un tramo
-- INSERT INTO crew_assignments (crew_id, segment_id, assigned_date, start_time, end_time, status, notes)
-- VALUES (1, 5, '2026-10-20', '08:00', '16:30', 'SCHEDULED', 'Tendido de microducto');

-- B. Insertar nueva asignación diaria para un nodo (PoP o NVT)
-- INSERT INTO crew_assignments (crew_id, node_id, node_type, node_name, assigned_date, start_time, end_time, status, notes)
-- VALUES (1, 3, 'NVT', 'NVt-45A', '2026-10-21', '08:00', '16:30', 'SCHEDULED', 'Montaje y conectorización de armario');

-- C. Insertar automáticamente todo un intervalo de fechas para un tramo (Generador de series en PostgreSQL)
-- INSERT INTO crew_assignments (crew_id, segment_id, assigned_date, status, notes)
-- SELECT 1, 5, d::date, 'SCHEDULED', 'Asignación automática por intervalo'
-- FROM generate_series('2026-10-20'::date, '2026-10-25'::date, '1 day'::interval) d
-- ON CONFLICT DO NOTHING;

-- D. Obtener cronograma semanal / mensual unificado (tramos y nodos)
-- SELECT 
--     ca.id,
--     ca.crew_id,
--     c.name AS crew_name,
--     ca.segment_id,
--     COALESCE(s.street_name, '[' || ca.node_type || '] ' || ca.node_name) AS display_name,
--     s.project_id,
--     ca.assigned_date,
--     ca.start_time,
--     ca.end_time,
--     ca.status,
--     ca.notes
-- FROM crew_assignments ca
-- JOIN crews c ON ca.crew_id = c.id
-- LEFT JOIN segments s ON ca.segment_id = s.id
-- WHERE ca.assigned_date BETWEEN '2026-10-19' AND '2026-10-25'
-- ORDER BY ca.assigned_date ASC, ca.start_time ASC;
