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

-- ============================================================
-- SEED DATA — Development reference data
-- ============================================================

-- Users (password = "password" bcrypt hashed)
INSERT INTO users (email, password, first_name, last_name, role, enabled) VALUES
('admin@quarkbau.com',    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7QA8qzhki', 'System',  'Admin',    'ADMIN',   true),
('manager@quarkbau.com',  '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7QA8qzhki', 'Klaus',   'Richter',  'MANAGER', true),
('engineer@quarkbau.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7QA8qzhki', 'Sophie',  'Wagner',   'ENGINEER',true),
('viewer@quarkbau.com',   '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7QA8qzhki', 'Markus',  'Bauer',    'VIEWER',  true)
ON CONFLICT (email) DO NOTHING;

-- Suppliers
INSERT INTO suppliers (name, contact_name, email, phone, address, type, created_at, updated_at) VALUES
('Prysmian Deutschland', 'Thomas Keil',   'thomas.keil@prysmian.com',    '+49-89-4567-0',   'Hannoversche Str. 144, Berlin',  'CABLE',    NOW(), NOW()),
('Rehau AG',             'Petra Huber',   'petra.huber@rehau.com',       '+49-921-803-0',   'Helmut-Wagner-Str. 1, Rehau',    'DUCT',     NOW(), NOW()),
('Corning Inc.',         'James Mitchell','j.mitchell@corning.com',      '+1-607-974-9000', 'One Riverfront Plaza, Corning',  'FIBER',    NOW(), NOW()),
('Bauer Technik GmbH',   'Hans Bauer',    'hans.bauer@bauertechnik.de',  '+49-30-7654-321', 'Industriestr. 22, Berlin',       'EQUIPMENT',NOW(), NOW());

-- Materials (fiber cable, ducts, joints, closures, etc.)
INSERT INTO materials (sku, name, unit, quantity_on_hand, min_threshold, unit_price, warehouse_location, supplier_id, reorder_mode, created_at, updated_at) VALUES
('FIB-SM-096',  'Single Mode Fiber 96 Core',              'Meters',  8000.0,  1500.0, 1.80,  'Berlin-A1',   1, 'AUTO',   NOW(), NOW()),
('FIB-SM-144',  'Single Mode Fiber 144 Core',             'Meters',  5000.0,  1000.0, 2.50,  'Berlin-A2',   1, 'AUTO',   NOW(), NOW()),
('FIB-SM-288',  'Single Mode Fiber 288 Core',             'Meters',  2000.0,   500.0, 4.20,  'Berlin-A3',   3, 'MANUAL', NOW(), NOW()),
('DUCT-40MM',   'HDPE Duct 40mm',                         'Meters', 10000.0,  2000.0, 1.20,  'Berlin-B1',   2, 'AUTO',   NOW(), NOW()),
('DUCT-63MM',   'HDPE Duct 63mm',                         'Meters',  7500.0,  1500.0, 1.85,  'Berlin-B2',   2, 'AUTO',   NOW(), NOW()),
('JOIN-SM-24',  'Fiber Splice Closure 24F',               'Units',    500.0,   100.0,12.00,  'Berlin-C1',   3, 'AUTO',   NOW(), NOW()),
('JOIN-SM-96',  'Fiber Splice Closure 96F',               'Units',    250.0,    50.0,35.00,  'Berlin-C2',   3, 'MANUAL', NOW(), NOW()),
('CLO-WALL-24', 'Wall Mounted Fiber Closure 24F',         'Units',    200.0,    40.0,22.00,  'Berlin-C3',   3, 'AUTO',   NOW(), NOW()),
('CAB-ARMORED', 'Armored Figure-8 Cable 24F',             'Meters',  3000.0,   600.0, 3.75,  'Berlin-A4',   1, 'AUTO',   NOW(), NOW()),
('PULL-ROPE',   'Fiber Pull Rope 6mm',                    'Meters', 20000.0,  5000.0, 0.15,  'Berlin-D1',   4, 'AUTO',   NOW(), NOW())
ON CONFLICT (sku) DO NOTHING;

-- Field Crews
INSERT INTO field_crews (name, leader, members, current_project, status, location) VALUES
('Team Alpha', 'Hans Mueller',   5, 'Berlin Fiber Optics A-1', 'ACTIVE',    'Berlin-Mitte'),
('Team Beta',  'Anna Schmidt',   4, 'Munich Metro Expansion',  'ACTIVE',    'Munich-Maxvorstadt'),
('Team Gamma', 'Rolf Dieter',    6, 'Berlin Fiber Optics A-1', 'ACTIVE',    'Berlin-Prenzlauer Berg'),
('Team Delta', 'Eva Hoffmann',   3, NULL,                      'AVAILABLE', 'Hamburg-Port');

-- Projects
INSERT INTO projects (name, description, start_date, end_date, organization_id) VALUES
('Berlin Fiber Optics A-1',   'High-speed fiber optic deployment in Berlin Mitte commercial district. Total 12km of underground cabling.', '2025-02-01', '2025-08-31', 1),
('Munich Metro Expansion',     'Fiber network expansion for Munich U-Bahn metro corridor, 8km underground + 3km aerial.',                   '2025-03-15', '2025-11-30', 1),
('Hamburg Port Connectivity',  'Industrial fiber connectivity for Hamburg HafenCity smart port initiative, 5km marine-grade cable.',         '2025-05-01', '2025-12-15', 1);

-- Segments for Project 1 (Berlin Fiber Optics A-1)
INSERT INTO segments (project_id, work_type, current_state, street_name, street_type, soil_type, length,
    start_latitude, start_longitude, end_latitude, end_longitude,
    traffic_level, planned_start_date, planned_end_date, crew_id) VALUES
(1, 'EXCAVATION',     'COMPLETED',    'Unter den Linden',     'MAIN_ROAD',   'CLAY',   850.0,  52.5163, 13.3777, 52.5178, 13.3900, 'HIGH',   '2025-02-01', '2025-02-28', 1),
(1, 'DUCT_LAYING',    'IN_PROGRESS',  'Friedrichstraße',      'MAIN_ROAD',   'SAND',   620.0,  52.5200, 13.3880, 52.5250, 13.3880, 'HIGH',   '2025-03-01', '2025-03-21', 1),
(1, 'FIBER_BLOWING',  'PLANNED',      'Dorotheenstraße',      'SIDE_STREET', 'MIXED',  410.0,  52.5157, 13.3833, 52.5170, 13.3900, 'MEDIUM', '2025-04-01', '2025-04-15', 3),
(1, 'EXCAVATION',     'IN_PROGRESS',  'Wilhelmstraße',        'MAIN_ROAD',   'CLAY',   730.0,  52.5080, 13.3820, 52.5160, 13.3820, 'HIGH',   '2025-03-10', '2025-04-10', 3),
(1, 'SPLICING',       'PLANNED',      'Mohrenstraße',         'SIDE_STREET', 'GRAVEL', 280.0,  52.5097, 13.3840, 52.5097, 13.3910, 'LOW',    '2025-05-01', '2025-05-10', 1),
(1, 'RESTORATION',    'PLANNED',      'Taubenstraße',         'SIDE_STREET', 'SAND',   320.0,  52.5131, 13.3860, 52.5131, 13.3970, 'LOW',    '2025-05-15', '2025-05-25', 3);

-- Segments for Project 2 (Munich Metro Expansion)
INSERT INTO segments (project_id, work_type, current_state, street_name, street_type, soil_type, length,
    start_latitude, start_longitude, end_latitude, end_longitude,
    traffic_level, planned_start_date, planned_end_date, crew_id) VALUES
(2, 'EXCAVATION',     'PLANNED',      'Leopoldstraße',        'MAIN_ROAD',   'ROCK',   950.0,  48.1572, 11.5869, 48.1670, 11.5869, 'HIGH',   '2025-03-15', '2025-04-30', 2),
(2, 'DUCT_LAYING',    'PLANNED',      'Maximilianstraße',     'MAIN_ROAD',   'SAND',   680.0,  48.1391, 11.5820, 48.1440, 11.5950, 'HIGH',   '2025-05-01', '2025-05-25', 2),
(2, 'FIBER_BLOWING',  'PLANNED',      'Schillerstraße',       'SIDE_STREET', 'MIXED',  390.0,  48.1380, 11.5690, 48.1380, 11.5800, 'MEDIUM', '2025-06-01', '2025-06-15', 2);

-- Utility Providers
INSERT INTO utility_providers (name, type, contact_email, service_region) VALUES
('Berlin GasNetz GmbH',         'GAS',    'plananauskunft@gasnetz.berlin',   'Berlin'),
('Berliner Wasserbetriebe',      'WATER',  'info@bwb.de',                     'Berlin'),
('Deutsche Telekom Technik',     'TELECOM','trassen@telekom.de',               'Berlin'),
('Stromnetz Berlin GmbH',        'POWER',  'leitungsauskunft@stromnetz.de',   'Berlin');

-- Inquiries
INSERT INTO inquiries (project_id, utility_provider_id, status, request_date, response_date, analysis_status) VALUES
(1, 1, 'APPROVED',  '2025-01-10', '2025-01-20', 'COMPLETE'),
(1, 2, 'APPROVED',  '2025-01-10', '2025-01-25', 'COMPLETE'),
(1, 3, 'PENDING',   '2025-01-15', NULL,          'PENDING'),
(2, 1, 'REQUESTED', '2025-03-01', NULL,          'PENDING'),
(2, 4, 'APPROVED',  '2025-03-01', '2025-03-10', 'COMPLETE');

-- Workflow Tasks
INSERT INTO workflow_tasks (project_name, current_step, assigned_to, status, due_date) VALUES
('Berlin Fiber Optics A-1',  'Excavation Permit',      'Team Alpha', 'DONE',        CURRENT_DATE - 20),
('Berlin Fiber Optics A-1',  'Utility Inquiry Berlin',  'Admin',      'DONE',        CURRENT_DATE - 15),
('Berlin Fiber Optics A-1',  'Duct Installation S2',   'Team Alpha', 'IN_PROGRESS', CURRENT_DATE + 5),
('Berlin Fiber Optics A-1',  'Traffic Management S4',  'Team Gamma', 'IN_PROGRESS', CURRENT_DATE + 3),
('Munich Metro Expansion',    'Geotechnical Survey',    'Team Beta',  'IN_PROGRESS', CURRENT_DATE + 8),
('Munich Metro Expansion',    'Permit Application',     'Admin',      'PENDING',     CURRENT_DATE + 15),
('Hamburg Port Connectivity', 'Site Assessment',        'Team Delta', 'PENDING',     CURRENT_DATE + 20);

-- Safety Protocols (for first 4 segments)
INSERT INTO safety_protocols (segment_id, regelplan_type, street_category, work_type, required_signs, required_barriers, notes, validated) VALUES
(1, 'B I/2 innerorts', 'innerorts',   'excavation', '["Vorfahrt gewähren","Baustellenausfahrt"]', '["Jersey Barrier","Absperrband"]',      'Full lane closure required nights only', true),
(2, 'B II innerorts',  'innerorts',   'duct_laying', '["Baustelle","30 km/h Zone"]',               '["Leitkegel","Schutzplanke"]',          'One-lane alternating traffic', true),
(3, 'B I/1 innerorts', 'innerorts',   'fiber',       '["Baustelle"]',                              '["Absperrband"]',                       'Pavement access only, no vehicle impact', false),
(4, 'B III innerorts', 'hauptstraße', 'excavation',  '["Vorfahrt gewähren","Umleitungsschild"]',   '["Jersey Barrier","Ampelanlage"]',      'Traffic light required for full closure', false);

-- Traffic Permits
INSERT INTO traffic_permits (segment_id, permit_number, issuing_authority, valid_from, valid_to, status, conditions) VALUES
(1, 'BA-2025-0041', 'Straßenverkehrsbehörde Berlin-Mitte', '2025-02-01', '2025-02-28', 'APPROVED', 'Night works only between 22:00-05:00'),
(2, 'BA-2025-0058', 'Straßenverkehrsbehörde Berlin-Mitte', '2025-03-01', '2025-03-31', 'APPROVED', 'Alternating single-lane traffic'),
(4, 'BA-2025-0072', 'Straßenverkehrsbehörde Berlin-Mitte', '2025-03-10', '2025-04-10', 'PENDING',  NULL);

-- Incidents
INSERT INTO incidents (segment_id, type, severity, description, status, reported_by, reported_at) VALUES
(2, 'BLOCKED',   'MEDIUM',   'Underground gas pipe discovered during excavation, not shown on utility maps. Work halted pending verification.', 'OPEN',     'Hans Mueller', NOW() - INTERVAL '2 days'),
(4, 'WEATHER',   'LOW',      'Heavy rain slowed excavation progress by 1 day. Schedule adjusted.', 'RESOLVED', 'Rolf Dieter',  NOW() - INTERVAL '5 days'),
(1, 'COMPLAINT', 'LOW',      'Noise complaint from adjacent business at Unter den Linden 35 regarding night works.', 'RESOLVED', 'Anna Schmidt', NOW() - INTERVAL '10 days'),
(2, 'SAFETY',    'HIGH',     'Worker slipped in open trench due to rain. First aid administered on scene. No serious injuries.', 'IN_PROGRESS', 'Hans Mueller', NOW() - INTERVAL '1 day');
