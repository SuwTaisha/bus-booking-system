-- V2__seed_data.sql
-- Dữ liệu mẫu cho route-service (địa chỉ, khoảng cách, thời gian chỉ mang tính minh họa)

-- =========================
-- stations: 10 bến xe
-- =========================
INSERT INTO stations (name, province, district, full_address)
VALUES ('Bến xe Miền Đông mới', 'TP. Hồ Chí Minh', 'TP. Thủ Đức',
        'Quốc lộ 1, P. Long Bình, TP. Thủ Đức, TP. Hồ Chí Minh'),
       ('Bến xe Miền Tây', 'TP. Hồ Chí Minh', 'Bình Tân',
        '395 Kinh Dương Vương, P. An Lạc, Q. Bình Tân, TP. Hồ Chí Minh'),
       ('Bến xe Đà Lạt', 'Lâm Đồng', 'Đà Lạt', 'Số 1 Tô Hiến Thành, Phường 3, TP. Đà Lạt, Lâm Đồng'),
       ('Bến xe phía Nam Nha Trang', 'Khánh Hòa', 'Nha Trang', 'Đường 23/10, Xã Vĩnh Phương, TP. Nha Trang, Khánh Hòa'),
       ('Bến xe Trung tâm Cần Thơ', 'Cần Thơ', 'Cái Răng', 'Quốc lộ 1A, P. Hưng Thạnh, Q. Cái Răng, Cần Thơ'),
       ('Bến xe Vũng Tàu', 'Bà Rịa - Vũng Tàu', 'Vũng Tàu', '192 Nam Kỳ Khởi Nghĩa, Phường 3, TP. Vũng Tàu'),
       ('Bến xe Mỹ Đình', 'Hà Nội', 'Nam Từ Liêm', '20 Phạm Hùng, P. Mỹ Đình 2, Q. Nam Từ Liêm, Hà Nội'),
       ('Bến xe Trung tâm Đà Nẵng', 'Đà Nẵng', 'Liên Chiểu', 'Tôn Đức Thắng, P. Hòa Minh, Q. Liên Chiểu, Đà Nẵng'),
       ('Bến xe Phan Thiết', 'Bình Thuận', 'Phan Thiết', 'Số 1 Từ Văn Tư, P. Phú Trinh, TP. Phan Thiết, Bình Thuận'),
       ('Bến xe Buôn Ma Thuột', 'Đắk Lắk', 'Buôn Ma Thuột',
        '71 Nguyễn Chí Thanh, P. Tân Lập, TP. Buôn Ma Thuột, Đắk Lắk');

-- =========================
-- rest_stops: 6 trạm dừng chân (tên mang tính minh họa, thay bằng đối tác thật nếu cần)
-- =========================
INSERT INTO rest_stops (name, full_address)
VALUES ('Trạm dừng chân Long Khánh', 'Quốc lộ 1A, TP. Long Khánh, Đồng Nai'),
       ('Trạm dừng chân Bảo Lộc', 'Quốc lộ 20, TP. Bảo Lộc, Lâm Đồng'),
       ('Trạm dừng chân Đèo Prenn', 'Quốc lộ 20, Đèo Prenn, TP. Đà Lạt, Lâm Đồng'),
       ('Trạm dừng chân Cam Ranh', 'Quốc lộ 1A, TP. Cam Ranh, Khánh Hòa'),
       ('Trạm dừng chân Hà Tĩnh', 'Quốc lộ 1A, TP. Hà Tĩnh, Hà Tĩnh'),
       ('Trạm dừng chân Vĩnh Long', 'Quốc lộ 1A, TP. Vĩnh Long, Vĩnh Long');

-- =========================
-- routes: 20 tuyến (mỗi cặp bến có chiều đi và chiều về)
-- =========================
INSERT INTO routes (line, estimated_minutes, distance, start_station_id, end_station_id)
SELECT v.line, v.minutes, v.km, s1.id, s2.id
FROM (VALUES ('Bến xe Miền Đông mới', 'Bến xe Đà Lạt', 'Quốc lộ 20', 420, 308),
             ('Bến xe Đà Lạt', 'Bến xe Miền Đông mới', 'Quốc lộ 20', 420, 308),
             ('Bến xe Miền Đông mới', 'Bến xe Đà Lạt', 'Cao tốc Dầu Giây - Liên Khương', 330, 300),
             ('Bến xe Đà Lạt', 'Bến xe Miền Đông mới', 'Cao tốc Dầu Giây - Liên Khương', 330, 300),
             ('Bến xe Miền Đông mới', 'Bến xe phía Nam Nha Trang', 'Quốc lộ 1A', 540, 430),
             ('Bến xe phía Nam Nha Trang', 'Bến xe Miền Đông mới', 'Quốc lộ 1A', 540, 430),
             ('Bến xe Miền Đông mới', 'Bến xe Phan Thiết', 'Cao tốc Dầu Giây - Phan Thiết', 150, 190),
             ('Bến xe Phan Thiết', 'Bến xe Miền Đông mới', 'Cao tốc Dầu Giây - Phan Thiết', 150, 190),
             ('Bến xe Miền Tây', 'Bến xe Trung tâm Cần Thơ', 'Cao tốc TP.HCM - Trung Lương - Mỹ Thuận', 180, 170),
             ('Bến xe Trung tâm Cần Thơ', 'Bến xe Miền Tây', 'Cao tốc TP.HCM - Trung Lương - Mỹ Thuận', 180, 170),
             ('Bến xe Miền Đông mới', 'Bến xe Vũng Tàu', 'Quốc lộ 51', 120, 100),
             ('Bến xe Vũng Tàu', 'Bến xe Miền Đông mới', 'Quốc lộ 51', 120, 100),
             ('Bến xe phía Nam Nha Trang', 'Bến xe Đà Lạt', 'Quốc lộ 27C', 210, 140),
             ('Bến xe Đà Lạt', 'Bến xe phía Nam Nha Trang', 'Quốc lộ 27C', 210, 140),
             ('Bến xe Mỹ Đình', 'Bến xe Trung tâm Đà Nẵng', 'Quốc lộ 1A', 840, 770),
             ('Bến xe Trung tâm Đà Nẵng', 'Bến xe Mỹ Đình', 'Quốc lộ 1A', 840, 770),
             ('Bến xe Trung tâm Đà Nẵng', 'Bến xe phía Nam Nha Trang', 'Quốc lộ 1A', 600, 530),
             ('Bến xe phía Nam Nha Trang', 'Bến xe Trung tâm Đà Nẵng', 'Quốc lộ 1A', 600, 530),
             ('Bến xe Buôn Ma Thuột', 'Bến xe Miền Đông mới', 'Quốc lộ 14', 420, 350),
             ('Bến xe Miền Đông mới', 'Bến xe Buôn Ma Thuột', 'Quốc lộ 14', 420,
              350)) AS v(start_name, end_name, line, minutes, km)
         JOIN stations s1 ON s1.name = v.start_name
         JOIN stations s2 ON s2.name = v.end_name;

-- =========================
-- route_stops: gắn trạm dừng chân vào tuyến
-- (cột: bến đi, bến đến, đường đi, trạm, thứ tự, số phút nghỉ)
-- =========================
INSERT INTO route_stops (route_id, rest_stop_id, stop_order, rest_minutes)
SELECT r.id, rs.id, v.ord, v.mins
FROM (VALUES ('Bến xe Miền Đông mới', 'Bến xe Đà Lạt', 'Quốc lộ 20', 'Trạm dừng chân Long Khánh', 1, 15),
             ('Bến xe Miền Đông mới', 'Bến xe Đà Lạt', 'Quốc lộ 20', 'Trạm dừng chân Bảo Lộc', 2, 30),
             ('Bến xe Miền Đông mới', 'Bến xe Đà Lạt', 'Quốc lộ 20', 'Trạm dừng chân Đèo Prenn', 3, 10),
             ('Bến xe Đà Lạt', 'Bến xe Miền Đông mới', 'Quốc lộ 20', 'Trạm dừng chân Đèo Prenn', 1, 10),
             ('Bến xe Đà Lạt', 'Bến xe Miền Đông mới', 'Quốc lộ 20', 'Trạm dừng chân Bảo Lộc', 2, 30),
             ('Bến xe Đà Lạt', 'Bến xe Miền Đông mới', 'Quốc lộ 20', 'Trạm dừng chân Long Khánh', 3, 15),
             ('Bến xe Miền Đông mới', 'Bến xe phía Nam Nha Trang', 'Quốc lộ 1A', 'Trạm dừng chân Long Khánh', 1, 15),
             ('Bến xe Miền Đông mới', 'Bến xe phía Nam Nha Trang', 'Quốc lộ 1A', 'Trạm dừng chân Cam Ranh', 2, 20),
             ('Bến xe phía Nam Nha Trang', 'Bến xe Miền Đông mới', 'Quốc lộ 1A', 'Trạm dừng chân Cam Ranh', 1, 20),
             ('Bến xe phía Nam Nha Trang', 'Bến xe Miền Đông mới', 'Quốc lộ 1A', 'Trạm dừng chân Long Khánh', 2, 15),
             ('Bến xe Mỹ Đình', 'Bến xe Trung tâm Đà Nẵng', 'Quốc lộ 1A', 'Trạm dừng chân Hà Tĩnh', 1, 30),
             ('Bến xe Trung tâm Đà Nẵng', 'Bến xe Mỹ Đình', 'Quốc lộ 1A', 'Trạm dừng chân Hà Tĩnh', 1, 30),
             ('Bến xe Miền Tây', 'Bến xe Trung tâm Cần Thơ', 'Cao tốc TP.HCM - Trung Lương - Mỹ Thuận',
              'Trạm dừng chân Vĩnh Long', 1, 15),
             ('Bến xe Trung tâm Cần Thơ', 'Bến xe Miền Tây', 'Cao tốc TP.HCM - Trung Lương - Mỹ Thuận',
              'Trạm dừng chân Vĩnh Long', 1, 15)) AS v(start_name, end_name, line, rest_name, ord, mins)
         JOIN stations s1 ON s1.name = v.start_name
         JOIN stations s2 ON s2.name = v.end_name
         JOIN routes r ON r.start_station_id = s1.id
    AND r.end_station_id = s2.id
    AND r.line = v.line
         JOIN rest_stops rs ON rs.name = v.rest_name;