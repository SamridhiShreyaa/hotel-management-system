-- Seed passwords for all users in this file: password (plain text for local demo)

-- 1) Hotels
INSERT INTO hotels (name, address, city, state, country, phone, email, star_rating) VALUES
    ('The Grand Palace', '12 MG Road', 'Bengaluru', 'Karnataka', 'India', '+91-8040001001', 'contact@grandpalace.com', 5),
    ('City Comfort Inn', '45 Brigade Road', 'Bengaluru', 'Karnataka', 'India', '+91-8040002002', 'hello@citycomfort.com', 3),
    ('Lakeview Suites', '18 Residency Road', 'Mysuru', 'Karnataka', 'India', '+91-8214003003', 'bookings@lakeviewsuites.com', 4);

-- 2) Rooms
INSERT INTO rooms (hotel_id, room_number, room_type, floor_number, price_per_night, max_occupancy, status, description) VALUES
    (1, '101', 'SINGLE', 1, 2500.00, 1, 'AVAILABLE', 'Cozy single room with city view'),
    (1, '102', 'DOUBLE', 1, 4500.00, 2, 'AVAILABLE', 'Spacious double room with queen bed'),
    (1, '201', 'SUITE', 2, 9000.00, 3, 'RESERVED', 'Suite with living area and balcony'),
    (1, '202', 'DELUXE', 2, 6500.00, 2, 'CHECKED_IN', 'Deluxe room with premium amenities'),
    (2, '101', 'SINGLE', 1, 1200.00, 1, 'AVAILABLE', 'Budget single room'),
    (2, '102', 'DOUBLE', 1, 2200.00, 2, 'MAINTENANCE', 'Double room under maintenance'),
    (3, '301', 'DELUXE', 3, 5400.00, 2, 'AVAILABLE', 'Deluxe room with lake view');

-- 3) Base users (admins + guests)
INSERT INTO users (full_name, email, password_hash, phone, role) VALUES
    ('Super Admin', 'admin@hotel.com', 'password', '9999999999', 'ADMIN'),
    ('Operations Admin', 'ops.admin@hotel.com', 'password', '9988776655', 'ADMIN'),
    ('Ravi Kumar', 'ravi@example.com', 'password', '8888888888', 'GUEST'),
    ('Ananya Sharma', 'ananya@example.com', 'password', '8877665544', 'GUEST'),
    ('Rahul Mehta', 'rahul@example.com', 'password', '7766554433', 'GUEST');

-- 4) Admin details (joined inheritance: user_id is PK)
INSERT INTO admins (user_id, department) VALUES
    (1, 'Operations'),
    (2, 'Guest Relations');

-- 5) Guest details (joined inheritance: user_id is PK)
INSERT INTO guests (user_id, address, nationality, id_proof_type, id_proof_no) VALUES
    (3, '10 Park Street, Bengaluru', 'Indian', 'AADHAR', 'XXXX-XXXX-1234'),
    (4, '22 Indiranagar, Bengaluru', 'Indian', 'PASSPORT', 'N1234567'),
    (5, '55 Kuvempunagar, Mysuru', 'Indian', 'DRIVING_LICENSE', 'KA09-2020-998877');

-- 6) Reservations
INSERT INTO reservations (guest_id, room_id, check_in_date, check_out_date, num_guests, total_amount, status, special_requests) VALUES
    (3, 3, '2026-04-10', '2026-04-12', 2, 18000.00, 'CONFIRMED', 'Late evening check-in'),
    (4, 4, '2026-04-05', '2026-04-08', 2, 19500.00, 'CHECKED_IN', 'Need airport pickup'),
    (5, 7, '2026-04-20', '2026-04-22', 2, 10800.00, 'PENDING', 'High floor room');

-- 7) Payments (1-to-1 with reservations)
INSERT INTO payments (reservation_id, amount, payment_method, payment_status, transaction_ref, paid_at) VALUES
    (1, 18000.00, 'UPI', 'SUCCESS', 'TXN-UPI-10001', '2026-04-01 12:30:00'),
    (2, 19500.00, 'CREDIT_CARD', 'SUCCESS', 'TXN-CC-10002', '2026-04-04 18:45:00'),
    (3, 10800.00, 'NET_BANKING', 'INITIATED', 'TXN-NB-10003', NULL);