-- password: 123456
INSERT INTO users (username, password, type, email)
VALUES ('guest_user', '$2a$12$OJybB6PGaqCA7N210TMOY.wMNoQovhtWKNvQ34GP1VWcqaaY7MLA6', 'CUSTOMER', 'guest_user@example.com');

-- password: admin
INSERT INTO users (username, password, type, email)
VALUES ('property_owner', '$2a$12$tOEj1qSUERoH3KBHyv17oOd79xx.ihPdC2tFj9kh/c.oNThh0l/8S', 'PROPERTY_OWNER', 'property_owner@example.com');

INSERT INTO properties (name, type, user_id) VALUES
('Gravata House', 'HOUSE', 2),
('Natal Dunnas Hotel', 'HOTEL', 2),
('Alfredo Apartment', 'APARTMENT', 2);

INSERT INTO rooms (property_id, name, capacity, daily_price) VALUES
(1, 'Entire House', 10, 500.00),
(2, 'Standard Room', 2, 40.00),
(2, 'Deluxe Room', 2, 80.00),
(3, 'Entire Apartment', 4, 200.00);

INSERT INTO bookings (user_id, room_id, status, type, guest_quantity, start_date, end_date, total_price, payment_status)
VALUES (1, 1, 'CONFIRMED', 'GUEST', 2, '2025-10-01', '2025-10-05', 2000.00, 'PAID');

INSERT INTO bookings (user_id, room_id, status, type, guest_quantity, start_date, end_date, total_price, payment_status)
VALUES (1, 1, 'CANCELLED', 'GUEST', 2, '2025-10-06', '2025-10-07', 500.00, 'REFUNDED');

INSERT INTO bookings (user_id, room_id, status, type, guest_quantity, start_date, end_date, total_price, payment_status)
VALUES (2, 1, 'BLOCKED', 'BLOCK', null, '2025-11-01', '2025-11-30', null, null);

INSERT INTO bookings (user_id, room_id, status, type, guest_quantity, start_date, end_date, total_price, payment_status)
VALUES (1, 1, 'CONFIRMED', 'GUEST', 2, '2025-12-01', '2025-12-05', 2000.00, 'PAID');

INSERT INTO bookings (user_id, room_id, status, type, guest_quantity, start_date, end_date, total_price, payment_status)
VALUES (1, 1, 'CONFIRMED', 'GUEST', 2, '2025-12-06', '2025-12-10', 2000.00, 'PAID');

INSERT INTO guests (booking_id, full_name, document_number, birth_date, main_guest)
VALUES
    (1, 'Joao Silva', 'A12345', '1992-08-06', TRUE),
    (1, 'Maria Silva', 'B12345', '1994-04-30', FALSE);