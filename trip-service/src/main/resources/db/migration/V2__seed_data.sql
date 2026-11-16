-- V2__seed_data.sql
-- Seed data for trip-service: vehicle_types, seat_layouts, vehicles, trips

-- =========================
-- vehicle_types
-- =========================
INSERT INTO vehicle_types (type_name, seat_count, description, is_active) VALUES
    ('Giường nằm 40 chỗ',  40, 'Xe giường nằm 2 tầng, 20 giường/tầng', TRUE),
    ('Ghế ngồi 45 chỗ',    45, 'Xe ghế ngồi thông thường 1 tầng',       TRUE),
    ('Limousine 20 chỗ',   20, 'Xe VIP giường nằm cao cấp 1 tầng',      TRUE);

-- =========================
-- seat_layouts — Giường nằm 40 chỗ (id=1): 2 tầng x 2 cột x 10 hàng
-- =========================
INSERT INTO seat_layouts (vehicle_type_id, seat_code, deck_no, row_no, col_no) VALUES
-- Tầng 1
    (1, 'A01', 1,  1, 1), (1, 'A02', 1,  1, 2),
    (1, 'A03', 1,  2, 1), (1, 'A04', 1,  2, 2),
    (1, 'A05', 1,  3, 1), (1, 'A06', 1,  3, 2),
    (1, 'A07', 1,  4, 1), (1, 'A08', 1,  4, 2),
    (1, 'A09', 1,  5, 1), (1, 'A10', 1,  5, 2),
    (1, 'A11', 1,  6, 1), (1, 'A12', 1,  6, 2),
    (1, 'A13', 1,  7, 1), (1, 'A14', 1,  7, 2),
    (1, 'A15', 1,  8, 1), (1, 'A16', 1,  8, 2),
    (1, 'A17', 1,  9, 1), (1, 'A18', 1,  9, 2),
    (1, 'A19', 1, 10, 1), (1, 'A20', 1, 10, 2),
-- Tầng 2
    (1, 'B01', 2,  1, 1), (1, 'B02', 2,  1, 2),
    (1, 'B03', 2,  2, 1), (1, 'B04', 2,  2, 2),
    (1, 'B05', 2,  3, 1), (1, 'B06', 2,  3, 2),
    (1, 'B07', 2,  4, 1), (1, 'B08', 2,  4, 2),
    (1, 'B09', 2,  5, 1), (1, 'B10', 2,  5, 2),
    (1, 'B11', 2,  6, 1), (1, 'B12', 2,  6, 2),
    (1, 'B13', 2,  7, 1), (1, 'B14', 2,  7, 2),
    (1, 'B15', 2,  8, 1), (1, 'B16', 2,  8, 2),
    (1, 'B17', 2,  9, 1), (1, 'B18', 2,  9, 2),
    (1, 'B19', 2, 10, 1), (1, 'B20', 2, 10, 2);

-- =========================
-- seat_layouts — Ghế ngồi 45 chỗ (id=2): 1 tầng, 5 hàng x 9 cột (5 cột trái, 4 cột phải, skip aisle)
-- Mô phỏng sơ đồ 3-2: col 1-3 bên trái, col 4-5 bên phải (không có col vật lý ở giữa)
-- =========================
INSERT INTO seat_layouts (vehicle_type_id, seat_code, deck_no, row_no, col_no) VALUES
    (2, 'S01', 1, 1, 1), (2, 'S02', 1, 1, 2), (2, 'S03', 1, 1, 3), (2, 'S04', 1, 1, 4), (2, 'S05', 1, 1, 5),
    (2, 'S06', 1, 2, 1), (2, 'S07', 1, 2, 2), (2, 'S08', 1, 2, 3), (2, 'S09', 1, 2, 4), (2, 'S10', 1, 2, 5),
    (2, 'S11', 1, 3, 1), (2, 'S12', 1, 3, 2), (2, 'S13', 1, 3, 3), (2, 'S14', 1, 3, 4), (2, 'S15', 1, 3, 5),
    (2, 'S16', 1, 4, 1), (2, 'S17', 1, 4, 2), (2, 'S18', 1, 4, 3), (2, 'S19', 1, 4, 4), (2, 'S20', 1, 4, 5),
    (2, 'S21', 1, 5, 1), (2, 'S22', 1, 5, 2), (2, 'S23', 1, 5, 3), (2, 'S24', 1, 5, 4), (2, 'S25', 1, 5, 5),
    (2, 'S26', 1, 6, 1), (2, 'S27', 1, 6, 2), (2, 'S28', 1, 6, 3), (2, 'S29', 1, 6, 4), (2, 'S30', 1, 6, 5),
    (2, 'S31', 1, 7, 1), (2, 'S32', 1, 7, 2), (2, 'S33', 1, 7, 3), (2, 'S34', 1, 7, 4), (2, 'S35', 1, 7, 5),
    (2, 'S36', 1, 8, 1), (2, 'S37', 1, 8, 2), (2, 'S38', 1, 8, 3), (2, 'S39', 1, 8, 4), (2, 'S40', 1, 8, 5),
    (2, 'S41', 1, 9, 1), (2, 'S42', 1, 9, 2), (2, 'S43', 1, 9, 3), (2, 'S44', 1, 9, 4), (2, 'S45', 1, 9, 5);

-- =========================
-- seat_layouts — Limousine 20 chỗ (id=3): 1 tầng, 2 cột x 10 hàng
-- =========================
INSERT INTO seat_layouts (vehicle_type_id, seat_code, deck_no, row_no, col_no) VALUES
    (3, 'L01', 1, 1, 1), (3, 'L02', 1, 1, 2),
    (3, 'L03', 1, 2, 1), (3, 'L04', 1, 2, 2),
    (3, 'L05', 1, 3, 1), (3, 'L06', 1, 3, 2),
    (3, 'L07', 1, 4, 1), (3, 'L08', 1, 4, 2),
    (3, 'L09', 1, 5, 1), (3, 'L10', 1, 5, 2),
    (3, 'L11', 1, 6, 1), (3, 'L12', 1, 6, 2),
    (3, 'L13', 1, 7, 1), (3, 'L14', 1, 7, 2),
    (3, 'L15', 1, 8, 1), (3, 'L16', 1, 8, 2),
    (3, 'L17', 1, 9, 1), (3, 'L18', 1, 9, 2),
    (3, 'L19', 1, 10, 1),(3, 'L20', 1, 10, 2);

-- =========================
-- vehicles
-- =========================
INSERT INTO vehicles (vehicle_type_id, license_plate, model, manufacture_year, is_active) VALUES
    (1, '51B-12345', 'Thaco Trường Hải',   2022, TRUE),
    (1, '51B-23456', 'Thaco Trường Hải',   2021, TRUE),
    (2, '51B-34567', 'Hyundai Universe',   2023, TRUE),
    (2, '51B-45678', 'Hyundai Universe',   2022, TRUE),
    (3, '51B-56789', 'Fuso Rosa Limousine', 2024, TRUE),
    (3, '51B-67890', 'Fuso Rosa Limousine', 2023, TRUE);

-- =========================
-- trips
-- Route IDs reference route-service (no FK); use 1 and 2 as representative values
-- Prices in VND
-- =========================
INSERT INTO trips (route_id, vehicle_id, departure_at, estimated_arrival_at, price, status) VALUES
-- Route 1 (e.g. HCM -> Da Lat), vehicle 1 (giường nằm)
    (1, 1,
     now() + interval '1 day',
     now() + interval '1 day 7 hours',
     250000, 'SCHEDULED'),

-- Route 1, vehicle 3 (ghế ngồi), slightly later
    (1, 3,
     now() + interval '1 day 2 hours',
     now() + interval '1 day 9 hours',
     180000, 'SCHEDULED'),

-- Route 2 (e.g. HCM -> Nha Trang), vehicle 5 (limousine)
    (2, 5,
     now() + interval '2 days',
     now() + interval '2 days 9 hours',
     450000, 'SCHEDULED'),

-- Route 2, vehicle 2 (giường nằm)
    (2, 2,
     now() + interval '2 days 3 hours',
     now() + interval '2 days 10 hours',
     250000, 'SCHEDULED'),

-- A completed trip (yesterday)
    (1, 4,
     now() - interval '2 days',
     now() - interval '2 days' + interval '7 hours',
     180000, 'COMPLETED'),

-- A cancelled trip
    (2, 6,
     now() + interval '3 days',
     now() + interval '3 days 9 hours',
     450000, 'CANCELLED');

-- Set arrived_at for the completed trip
UPDATE trips
SET arrived_at = estimated_arrival_at
WHERE status = 'COMPLETED';
