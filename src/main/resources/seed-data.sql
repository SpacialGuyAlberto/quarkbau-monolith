-- ============================================================
-- SEED DATA — Extended Development Reference Data
-- ============================================================

-- 1. Organizations
INSERT INTO organizations (name, slug, type) VALUES
('QuarkBau GmbH', 'quarkbau-gmbh', 'INTERNAL'),
('Subcon Corp', 'subcon-corp', 'EXTERNAL')
ON CONFLICT (slug) DO NOTHING;

-- 2. Employees
INSERT INTO employees (email, first_name, last_name, role, enabled, employee_type, organization_id) VALUES
('admin@quarkbau.com',    'System',  'Admin',    'ADMIN',   true, 'INTERNAL', 1),
('manager@quarkbau.com',  'Klaus',   'Richter',  'PROJEKT_MANAGER', true, 'INTERNAL', 1),
('engineer@quarkbau.com', 'Sophie',  'Wagner',   'BAULEITER',true, 'INTERNAL', 1),
('luis@quarkbau.com',     'Luis',    'Alberto',  'PROJEKT_MANAGER',  true, 'INTERNAL', 1),
('jens.m@quarkbau.com',   'Jens',    'Müller',   'BAULEITER', true, 'INTERNAL', 1)
ON CONFLICT (email) DO NOTHING;

-- 3. Users (password = "password" bcrypt hashed)
INSERT INTO users (email, password, first_name, last_name, role, enabled, organization_id) VALUES
('admin@quarkbau.com',    '$2a$10$ZZtO9.NNmC.nwGB0wFeDNO7g3j8U0LkGYMhwYittlaUKdyyMrH5k6', 'System',  'Admin',    'ADMIN',   true, 1),
('manager@quarkbau.com',  '$2a$10$ZZtO9.NNmC.nwGB0wFeDNO7g3j8U0LkGYMhwYittlaUKdyyMrH5k6', 'Klaus',   'Richter',  'MANAGER', true, 1),
('engineer@quarkbau.com', '$2a$10$ZZtO9.NNmC.nwGB0wFeDNO7g3j8U0LkGYMhwYittlaUKdyyMrH5k6', 'Sophie',  'Wagner',   'FIELD_EXPERT',true, 1),
('viewer@quarkbau.com',   '$2a$10$ZZtO9.NNmC.nwGB0wFeDNO7g3j8U0LkGYMhwYittlaUKdyyMrH5k6', 'Markus',  'Bauer',    'CLIENT',  true, 1),
('luis@quarkbau.com',     '$2a$10$ZZtO9.NNmC.nwGB0wFeDNO7g3j8U0LkGYMhwYittlaUKdyyMrH5k6', 'Luis',    'Alberto',  'PROJECT_MANAGER',  true, 1),
('jens.m@quarkbau.com',   '$2a$10$ZZtO9.NNmC.nwGB0wFeDNO7g3j8U0LkGYMhwYittlaUKdyyMrH5k6', 'Jens',    'Müller',   'FIELD_EXPERT', true, 1),
('s.koch@quarkbau.com',   '$2a$10$ZZtO9.NNmC.nwGB0wFeDNO7g3j8U0LkGYMhwYittlaUKdyyMrH5k6', 'Sabine',  'Koch',     'MANAGER', true, 1),
('m.schulz@quarkbau.com', '$2a$10$ZZtO9.NNmC.nwGB0wFeDNO7g3j8U0LkGYMhwYittlaUKdyyMrH5k6', 'Michael', 'Schulz',   'CLIENT', true, 1)
ON CONFLICT (email) DO NOTHING;

-- Update Users with employee references
UPDATE users SET employee_id = (SELECT id FROM employees WHERE email = users.email) WHERE employee_id IS NULL;

-- 3. Subcontractors
INSERT INTO subcontractors (name, contact_person, email, phone, status, organization_id) VALUES
('FastBuild GmbH', 'Dieter Bohlen', 'dieter@fastbuild.de', '+49-30-111111', 'ACTIVE', 2);

-- 3.5. Crews
INSERT INTO crews (name, foreman_name, size, vehicle_plate, subcontractor_id) VALUES
('Team Alpha',   'Hans Mueller',   5, 'B-XY 1234', 1),
('Team Beta',    'Anna Schmidt',   4, 'B-XY 1235', 1),
('Team Gamma',   'Rolf Dieter',    6, 'B-XY 1236', 1),
('Team Delta',   'Eva Hoffmann',   3, 'B-XY 1237', 1),
('Team Epsilon', 'Lars Becker',    4, 'B-XY 1238', 1),
('Team Zeta',    'Nina Klein',     5, 'B-XY 1239', 1);

-- 4. Suppliers
INSERT INTO suppliers (name, contact_name, email, phone, address, type, created_at, updated_at) VALUES
('Prysmian Deutschland', 'Thomas Keil',   'thomas.keil@prysmian.com',    '+49-89-4567-0',   'Hannoversche Str. 144, Berlin',  'MATERIAL',    NOW(), NOW()),
('Rehau AG',             'Petra Huber',   'petra.huber@rehau.com',       '+49-921-803-0',   'Helmut-Wagner-Str. 1, Rehau',    'MATERIAL',     NOW(), NOW()),
('Corning Inc.',         'James Mitchell','j.mitchell@corning.com',      '+1-607-974-9000', 'One Riverfront Plaza, Corning',  'MATERIAL',    NOW(), NOW()),
('Bauer Technik GmbH',   'Hans Bauer',    'hans.bauer@bauertechnik.de',  '+49-30-7654-321', 'Industriestr. 22, Berlin',       'MACHINERY',NOW(), NOW());

-- 5. Materials (fiber cable, ducts, joints, closures, etc.)
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

-- 6. Projects
INSERT INTO projects (name, description, start_date, end_date, organization_id, latitude, longitude) VALUES
('Berlin Fiber Optics A-1',   'High-speed fiber optic deployment in Berlin Mitte commercial district. Total 12km of underground cabling.', '2025-02-01', '2025-08-31', 1, 52.5200, 13.4050),
('Munich Metro Expansion',    'Fiber network expansion for Munich U-Bahn metro corridor, 8km underground + 3km aerial.',                   '2025-03-15', '2025-11-30', 1, 48.1351, 11.5820),
('Hamburg Port Connectivity', 'Industrial fiber connectivity for Hamburg HafenCity smart port initiative, 5km marine-grade cable.',        '2025-05-01', '2025-12-15', 1, 53.5413, 9.9840),
('Frankfurt Ring',            'Upgrading the financial district backbone with 288-core single mode fiber rings.',                          '2025-04-01', '2025-09-30', 1, 50.1109, 8.6821),
('Stuttgart Residential FTTH', 'FTTH rollout for 500 households in Stuttgart-West area.',                                                  '2025-06-15', '2026-03-01', 1, 48.7758, 9.1829);

-- 7. Segments for Project 1 (Berlin Fiber Optics A-1)
INSERT INTO segments (work_type, current_state, street_name, street_type, soil_type, length,
    start_latitude, start_longitude, end_latitude, end_longitude,
    traffic_level, planned_start_date, planned_end_date, crew_id) VALUES
('EXCAVATION',     'COMPLETED',    'Unter den Linden',     'MAIN_ROAD',   'CLAY',   850.0,  52.5163, 13.3777, 52.5178, 13.3900, 'HIGH',   '2025-02-01', '2025-02-28', 1),
('DUCT_LAYING',    'IN_PROGRESS',  'Friedrichstraße',      'MAIN_ROAD',   'SAND',   620.0,  52.5200, 13.3880, 52.5250, 13.3880, 'HIGH',   '2025-03-01', '2025-03-21', 1),
('FIBER_BLOWING',  'PLANNED',      'Dorotheenstraße',      'SIDE_STREET', 'MIXED',  410.0,  52.5157, 13.3833, 52.5170, 13.3900, 'MEDIUM', '2025-04-01', '2025-04-15', 3),
('EXCAVATION',     'IN_PROGRESS',  'Wilhelmstraße',        'MAIN_ROAD',   'CLAY',   730.0,  52.5080, 13.3820, 52.5160, 13.3820, 'HIGH',   '2025-03-10', '2025-04-10', 3),
('SPLICING',       'PLANNED',      'Mohrenstraße',         'SIDE_STREET', 'GRAVEL', 280.0,  52.5097, 13.3840, 52.5097, 13.3910, 'LOW',    '2025-05-01', '2025-05-10', 1),
('RESTORATION',    'PLANNED',      'Taubenstraße',         'SIDE_STREET', 'SAND',   320.0,  52.5131, 13.3860, 52.5131, 13.3970, 'LOW',    '2025-05-15', '2025-05-25', 3);

-- 8. Segments for Project 2 (Munich Metro Expansion)
INSERT INTO segments (work_type, current_state, street_name, street_type, soil_type, length,
    start_latitude, start_longitude, end_latitude, end_longitude,
    traffic_level, planned_start_date, planned_end_date, crew_id) VALUES
('EXCAVATION',     'PLANNED',      'Leopoldstraße',        'MAIN_ROAD',   'ROCK',   950.0,  48.1572, 11.5869, 48.1670, 11.5869, 'HIGH',   '2025-03-15', '2025-04-30', 2),
('DUCT_LAYING',    'PLANNED',      'Maximilianstraße',     'MAIN_ROAD',   'SAND',   680.0,  48.1391, 11.5820, 48.1440, 11.5950, 'HIGH',   '2025-05-01', '2025-05-25', 2),
('FIBER_BLOWING',  'PLANNED',      'Schillerstraße',       'SIDE_STREET', 'MIXED',  390.0,  48.1380, 11.5690, 48.1380, 11.5800, 'MEDIUM', '2025-06-01', '2025-06-15', 2);

-- 9. Segments for Project 4 (Frankfurt Ring)
INSERT INTO segments (work_type, current_state, street_name, street_type, soil_type, length,
    start_latitude, start_longitude, end_latitude, end_longitude,
    traffic_level, planned_start_date, planned_end_date, crew_id) VALUES
('EXCAVATION',     'IN_PROGRESS',  'Mainzer Landstraße',   'MAIN_ROAD',   'GRAVEL', 1200.0, 50.1100, 8.6600,  50.1115, 8.6400,  'HIGH',   '2025-04-05', '2025-05-30', 5),
('DUCT_LAYING',    'PLANNED',      'Kaiserstraße',         'MAIN_ROAD',   'SAND',   450.0,  50.1080, 8.6700,  50.1095, 8.6600,  'HIGH',   '2025-06-01', '2025-06-20', 5);

-- 10. Utility Providers
INSERT INTO utility_providers (name, type, contact_email, service_region) VALUES
('Berlin GasNetz GmbH',         'GAS',    'plananauskunft@gasnetz.berlin',   'Berlin'),
('Berliner Wasserbetriebe',      'WATER',  'info@bwb.de',                     'Berlin'),
('Deutsche Telekom Technik',     'TELECOM','trassen@telekom.de',               'Berlin'),
('Stromnetz Berlin GmbH',        'POWER',  'leitungsauskunft@stromnetz.de',   'Berlin'),
('Mainova AG',                   'MULTI',  'auskunft@mainova.de',             'Frankfurt');

-- 11. Inquiries
INSERT INTO inquiries (project_id, utility_provider_id, status, request_date, response_date, analysis_status) VALUES
(1, 1, 'RECEIVED_CLEAR',  '2025-01-10', '2025-01-20', 'COMPLETED'),
(1, 2, 'RECEIVED_CLEAR',  '2025-01-10', '2025-01-25', 'COMPLETED'),
(1, 3, 'PENDING',   '2025-01-15', NULL,          'PENDING'),
(2, 1, 'SENT', '2025-03-01', NULL,          'PENDING'),
(2, 4, 'RECEIVED_CLEAR',  '2025-03-01', '2025-03-10', 'COMPLETED'),
(4, 5, 'RECEIVED_CLEAR',  '2025-03-20', '2025-03-28', 'COMPLETED');

-- 12. Workflow Tasks
INSERT INTO workflow_tasks (project_name, current_step, assigned_to, status, due_date) VALUES
('Berlin Fiber Optics A-1',  'Excavation Permit',      'Team Alpha', 'DONE',        CURRENT_DATE - 20),
('Berlin Fiber Optics A-1',  'Utility Inquiry Berlin',  'Admin',      'DONE',        CURRENT_DATE - 15),
('Berlin Fiber Optics A-1',  'Duct Installation S2',   'Team Alpha', 'IN_PROGRESS', CURRENT_DATE + 5),
('Berlin Fiber Optics A-1',  'Traffic Management S4',  'Team Gamma', 'IN_PROGRESS', CURRENT_DATE + 3),
('Munich Metro Expansion',    'Geotechnical Survey',    'Team Beta',  'IN_PROGRESS', CURRENT_DATE + 8),
('Munich Metro Expansion',    'Permit Application',     'Admin',      'PENDING',     CURRENT_DATE + 15),
('Hamburg Port Connectivity', 'Site Assessment',        'Team Delta', 'PENDING',     CURRENT_DATE + 20),
('Frankfurt Ring',            'Network Design Auth',    'Jens M.',    'DONE',        CURRENT_DATE - 5);

-- 13. Safety Protocols (for first 4 segments)
INSERT INTO safety_protocols (segment_id, regelplan_type, street_category, work_type, required_signs, required_barriers, notes, validated) VALUES
(1, 'B I/2 innerorts', 'innerorts',   'excavation', '["Vorfahrt gewähren","Baustellenausfahrt"]', '["Jersey Barrier","Absperrband"]',      'Full lane closure required nights only', true),
(2, 'B II innerorts',  'innerorts',   'duct_laying', '["Baustelle","30 km/h Zone"]',               '["Leitkegel","Schutzplanke"]',          'One-lane alternating traffic', true),
(3, 'B I/1 innerorts', 'innerorts',   'fiber',       '["Baustelle"]',                              '["Absperrband"]',                       'Pavement access only, no vehicle impact', false),
(4, 'B III innerorts', 'hauptstraße', 'excavation',  '["Vorfahrt gewähren","Umleitungsschild"]',   '["Jersey Barrier","Ampelanlage"]',      'Traffic light required for full closure', false);

-- 14. Traffic Permits
INSERT INTO traffic_permits (segment_id, permit_number, issuing_authority, valid_from, valid_to, status, conditions) VALUES
(1, 'BA-2025-0041', 'Straßenverkehrsbehörde Berlin-Mitte', '2025-02-01', '2025-02-28', 'APPROVED', 'Night works only between 22:00-05:00'),
(2, 'BA-2025-0058', 'Straßenverkehrsbehörde Berlin-Mitte', '2025-03-01', '2025-03-31', 'APPROVED', 'Alternating single-lane traffic'),
(4, 'BA-2025-0072', 'Straßenverkehrsbehörde Berlin-Mitte', '2025-03-10', '2025-04-10', 'PENDING',  NULL);

-- 15. Incidents
INSERT INTO incidents (segment_id, type, severity, description, status, reported_by, reported_at) VALUES
(2, 'BLOCKED',   'MEDIUM',   'Underground gas pipe discovered during excavation, not shown on utility maps. Work halted pending verification.', 'OPEN',     'Hans Mueller', NOW() - INTERVAL '2 days'),
(4, 'WEATHER',   'LOW',      'Heavy rain slowed excavation progress by 1 day. Schedule adjusted.', 'RESOLVED', 'Rolf Dieter',  NOW() - INTERVAL '5 days'),
(1, 'COMPLAINT', 'LOW',      'Noise complaint from adjacent business at Unter den Linden 35 regarding night works.', 'RESOLVED', 'Anna Schmidt', NOW() - INTERVAL '10 days'),
(2, 'SAFETY',    'HIGH',     'Worker slipped in open trench due to rain. First aid administered on scene. No serious injuries.', 'IN_PROGRESS', 'Hans Mueller', NOW() - INTERVAL '1 day'),
(9, 'LOGISTICS', 'MEDIUM',   'Delay in delivery of HDPE Duct 40mm at Frankfurt site.', 'OPEN', 'Lars Becker', NOW() - INTERVAL '3 hours');
