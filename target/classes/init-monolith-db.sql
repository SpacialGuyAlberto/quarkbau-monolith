-- QuarkBau Monolith Unified Database Initialization
-- Includes Auth, Inventory, and Planning schemas

-- 1. Auth Schema
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    role VARCHAR(50),
    enabled BOOLEAN DEFAULT TRUE,
    organization_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Inventory Schema
CREATE TABLE IF NOT EXISTS suppliers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    contact_name VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(255),
    address VARCHAR(255),
    type VARCHAR(50),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS materials (
    id BIGSERIAL PRIMARY KEY,
    sku VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    unit VARCHAR(50) NOT NULL,
    quantity_on_hand DOUBLE PRECISION NOT NULL,
    min_threshold DOUBLE PRECISION NOT NULL,
    unit_price DECIMAL(19, 2),
    warehouse_location VARCHAR(255),
    supplier_id BIGINT REFERENCES suppliers(id),
    reorder_mode VARCHAR(50),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS machines (
    id BIGSERIAL PRIMARY KEY,
    serial_number VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    current_location VARCHAR(255),
    assigned_crew_id BIGINT,
    created_at TIMESTAMP,
    last_maintenance_date TIMESTAMP
);

-- 3. Planning Schema
CREATE TABLE IF NOT EXISTS projects (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    start_date DATE,
    end_date DATE,
    organization_id BIGINT
);

CREATE TABLE IF NOT EXISTS segments (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT REFERENCES projects(id),
    work_type VARCHAR(50),
    current_state VARCHAR(50),
    street_name VARCHAR(255),
    street_type VARCHAR(50),
    soil_type VARCHAR(50),
    length DOUBLE PRECISION,
    start_latitude DOUBLE PRECISION,
    start_longitude DOUBLE PRECISION,
    end_latitude DOUBLE PRECISION,
    end_longitude DOUBLE PRECISION,
    traffic_level VARCHAR(50),
    planned_start_date DATE,
    planned_end_date DATE,
    custom_fields JSONB,
    crew_id BIGINT,
    version BIGINT
);

-- 4. Infrastructure Schema
CREATE TABLE IF NOT EXISTS utility_providers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50),
    contact_email VARCHAR(255),
    service_region VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS inquiries (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT,
    utility_provider_id BIGINT REFERENCES utility_providers(id),
    status VARCHAR(50),
    request_date DATE,
    response_date DATE,
    response_file_url VARCHAR(255),
    analysis_result TEXT,
    analysis_status VARCHAR(50)
);

-- 5. Workflow Schema
CREATE TABLE IF NOT EXISTS workflow_tasks (
    id BIGSERIAL PRIMARY KEY,
    project_name VARCHAR(255),
    current_step VARCHAR(255),
    assigned_to VARCHAR(255),
    status VARCHAR(50),
    due_date DATE
);

-- 6. Safety & Traffic Schema
CREATE TABLE IF NOT EXISTS incidents (
    id BIGSERIAL PRIMARY KEY,
    segment_id BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL,
    severity VARCHAR(50) NOT NULL,
    description TEXT,
    status VARCHAR(50),
    reported_by VARCHAR(255),
    reported_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS incident_photos (
    incident_id BIGINT REFERENCES incidents(id),
    photo_url VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS safety_protocols (
    id BIGSERIAL PRIMARY KEY,
    segment_id BIGINT NOT NULL,
    regelplan_type VARCHAR(255),
    street_category VARCHAR(255),
    work_type VARCHAR(255),
    required_signs TEXT,
    required_barriers TEXT,
    notes TEXT,
    validated BOOLEAN
);

CREATE TABLE IF NOT EXISTS traffic_permits (
    id BIGSERIAL PRIMARY KEY,
    segment_id BIGINT NOT NULL,
    permit_number VARCHAR(255) NOT NULL,
    issuing_authority VARCHAR(255),
    valid_from DATE,
    valid_to DATE,
    status VARCHAR(50),
    conditions TEXT
);

-- 7. Field App Schema
CREATE TABLE IF NOT EXISTS field_crews (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    leader VARCHAR(255),
    members INT,
    current_project VARCHAR(255),
    status VARCHAR(50),
    location VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS evidence (
    id BIGSERIAL PRIMARY KEY,
    segment_id BIGINT NOT NULL,
    step_name VARCHAR(255) NOT NULL,
    photo_url VARCHAR(255) NOT NULL,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    created_at TIMESTAMP,
    user_id BIGINT
);

-- SEED DATA
INSERT INTO users (email, password, first_name, last_name, role, enabled) VALUES 
('admin@quarkbau.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7QA8qzhki', 'System', 'Admin', 'ADMIN', true)
ON CONFLICT (email) DO NOTHING;

INSERT INTO materials (sku, name, unit, quantity_on_hand, min_threshold, unit_price) VALUES 
('FIB-SM-144', 'Single Mode Fiber 144 Core', 'Meters', 5000.0, 1000.0, 2.50),
('DUCT-40MM', 'HDPE Duct 40mm', 'Meters', 10000.0, 2000.0, 1.20)
ON CONFLICT (sku) DO NOTHING;

INSERT INTO utility_providers (name, type, contact_email, service_region) VALUES 
('Berlin GasNetz GmbH', 'GAS', 'plananauskunft@gasnetz.berlin', 'Berlin'),
('Berliner Wasserbetriebe', 'WATER', 'info@bwb.de', 'Berlin'),
('Deutsche Telekom Technik', 'TELECOM', 'trassen@telekom.de', 'Berlin');

INSERT INTO workflow_tasks (project_name, current_step, assigned_to, status, due_date) VALUES 
('Berlin Fiber Deployment', 'Excavation', 'Team Alpha', 'in-progress', CURRENT_DATE + 5),
('Munich Network Expansion', 'Planning', 'Team Beta', 'pending', CURRENT_DATE + 10);

INSERT INTO field_crews (name, leader, members, current_project, status, location) VALUES 
('Team Alpha', 'Hans Mueller', 5, 'Berlin Fiber Deployment', 'active', 'Berlin Mitte'),
('Team Beta', 'Anna Schmidt', 4, 'Munich Network Expansion', 'active', 'Munich Center');
