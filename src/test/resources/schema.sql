CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(60) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    type VARCHAR(50) NOT NULL CHECK (type IN ('CUSTOMER', 'PROPERTY_OWNER'))
);

CREATE TABLE properties (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(20) NOT NULL CHECK (type IN ('APARTMENT', 'HOUSE', 'HOTEL')),
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE rooms (
    id SERIAL PRIMARY KEY,
    property_id INT NOT NULL,
    name VARCHAR(50) NOT NULL,
    capacity INT NOT NULL,
    daily_price DECIMAL(10,2) NOT NULL,
    last_booking_at TIMESTAMP,
    FOREIGN KEY (property_id) REFERENCES properties(id)
);

CREATE TABLE bookings (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    room_id INT NOT NULL,
    status VARCHAR(10) NOT NULL CHECK (status IN ('CONFIRMED', 'CANCELLED', 'PENDING', 'BLOCKED')),
    type VARCHAR(15) NOT NULL CHECK (type IN ('GUEST', 'BLOCK')),
    guest_quantity INT,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    total_price DECIMAL(10,2),
    payment_status VARCHAR(10) CHECK (payment_status IN ('PENDING', 'PAID', 'REFUNDED')),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (room_id) REFERENCES rooms(id)
);

CREATE TABLE guests (
    id SERIAL PRIMARY KEY,
    booking_id INT NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    document_number VARCHAR(60) NOT NULL,
    birth_date DATE NOT NULL,
    main_guest BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (booking_id) REFERENCES bookings(id)
);