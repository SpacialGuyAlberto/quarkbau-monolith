-- QuarkBau Monolith Unified Database Initialization
CREATE EXTENSION IF NOT EXISTS postgis;
-- Includes Auth, Inventory, and Planning schemas

-- 1. Auth Schema
CREATE TABLE IF NOT EXISTS organizations (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    slug VARCHAR(255) UNIQUE NOT NULL,
    type VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    role VARCHAR(50),
    enabled BOOLEAN DEFAULT TRUE,
    organization_id BIGINT REFERENCES organizations(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS employees (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    role VARCHAR(50),
    enabled BOOLEAN DEFAULT TRUE,
    employee_type VARCHAR(50),
    organization_id BIGINT REFERENCES organizations(id),
    subcontractor_id BIGINT
);


CREATE TABLE IF NOT EXISTS clusters (
                                        id SERIAL PRIMARY KEY,
                                        name VARCHAR(255) NOT NULL,
                                        description TEXT,
                                        project_id INT REFERENCES public.projects(id),
                                        project_leiter_id INT REFERENCES public.users(id)
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

CREATE TABLE IF NOT EXISTS hueps (
                                     id BIGSERIAL PRIMARY KEY,
                                     streetAddress TEXT NOT NULL,
                                     houseNumber TEXT NOT NULL,
                                     installation_location TEXT NOT NULL,
                                     status TEXT NOT NULL
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

create table subcontractors
(
    id                     bigserial
        primary key,
    average_time_per_meter double precision,
    contact_person         varchar(255),
    defect_rate            double precision,
    email                  varchar(255),
    name                   varchar(255) not null,
    phone                  varchar(255),
    rating                 double precision,
    rework_frequency       double precision,
    status                 varchar(255)
        constraint subcontractors_status_check
            check ((status)::text = ANY
                   ((ARRAY ['PREFERRED'::character varying, 'ACTIVE'::character varying, 'BLOCKED'::character varying])::text[])),
    organization_id        bigint
        constraint fknnnfgbfo3vqlxnst1bsd68bpw
            references organizations
);

alter table subcontractors
    owner to quarkbau;


CREATE TABLE IF NOT EXISTS Rohrverband (
                                           id BIGSERIAL PRIMARY KEY,
                                           type TEXT NOT NULL,
                                           color_code TEXT NOT NULL,
                                           passed_pressure_test BOOLEAN NOT NULL,
                                           segment BIGINT REFERENCES segments(id)

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
    organization_id BIGINT,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION
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
    duct_diameter DOUBLE PRECISION,
    start_address VARCHAR(255),
    end_address VARCHAR(255),
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


CREATE TABLE IF NOT EXISTS hazards (
                                 id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                segment_id serial NOT NULL,
                                FOREIGN KEY (segment_id) REFERENCES public.segments(id),
                                reported_by serial references public.users(id),
                                hazard_type varchar(80),
                                hazard_severity varchar(80),
                                latitude double precision,
                                longitude double precision,
                                description varchar(500),
                                photo_evidence_url varchar(500),
                                status varchar(80),
                                created_at timestamp default now(),
                                updated_at timestamp default now()

);


create table pop
(
    id bigserial primary key,
    name varchar(255),
    location_address   text not null,
    max_capacity_ports integer,
    cluster_id bigint references clusters,
    elevation numeric
);

alter table pop
    owner to quarkbau;

