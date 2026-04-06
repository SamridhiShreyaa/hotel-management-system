-- synchronized copy of ../../../schema.sql (canonical)
-- used by Spring Boot SQL initialization
DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS reservations;
DROP TABLE IF EXISTS rooms;
DROP TABLE IF EXISTS hotels;
DROP TABLE IF EXISTS admins;
DROP TABLE IF EXISTS guests;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    user_id BIGINT NOT NULL AUTO_INCREMENT,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id)
);

CREATE TABLE guests (
    user_id BIGINT NOT NULL,
    address VARCHAR(255),
    nationality VARCHAR(100),
    id_proof_type VARCHAR(50),
    id_proof_no VARCHAR(100),
    PRIMARY KEY (user_id),
    CONSTRAINT fk_guest_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE admins (
    user_id BIGINT NOT NULL,
    department VARCHAR(100),
    PRIMARY KEY (user_id),
    CONSTRAINT fk_admin_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE hotels (
    hotel_id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL,
    address VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100),
    country VARCHAR(100) NOT NULL DEFAULT 'India',
    phone VARCHAR(20),
    email VARCHAR(150),
    star_rating INT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (hotel_id)
);

CREATE TABLE rooms (
    room_id BIGINT NOT NULL AUTO_INCREMENT,
    hotel_id BIGINT NOT NULL,
    room_number VARCHAR(10) NOT NULL,
    room_type VARCHAR(20) NOT NULL,
    floor_number INT,
    price_per_night DECIMAL(10,2) NOT NULL,
    max_occupancy INT NOT NULL DEFAULT 2,
    status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (room_id),
    UNIQUE KEY uq_hotel_room (hotel_id, room_number),
    CONSTRAINT fk_room_hotel FOREIGN KEY (hotel_id) REFERENCES hotels(hotel_id) ON DELETE CASCADE
);

CREATE TABLE reservations (
    reservation_id BIGINT NOT NULL AUTO_INCREMENT,
    guest_id BIGINT NOT NULL,
    room_id BIGINT NOT NULL,
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    num_guests INT NOT NULL DEFAULT 1,
    total_amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    special_requests TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (reservation_id),
    CONSTRAINT fk_res_guest FOREIGN KEY (guest_id) REFERENCES guests(user_id),
    CONSTRAINT fk_res_room FOREIGN KEY (room_id) REFERENCES rooms(room_id)
);

CREATE TABLE payments (
    payment_id BIGINT NOT NULL AUTO_INCREMENT,
    reservation_id BIGINT NOT NULL UNIQUE,
    amount DECIMAL(10,2) NOT NULL,
    payment_method VARCHAR(20) NOT NULL,
    payment_status VARCHAR(20) NOT NULL DEFAULT 'INITIATED',
    transaction_ref VARCHAR(100) UNIQUE,
    paid_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (payment_id),
    CONSTRAINT fk_pay_reservation FOREIGN KEY (reservation_id) REFERENCES reservations(reservation_id)
);