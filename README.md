# Booking - Property Management System (PMS)

## Overview
**Booking** is a system designed to manage properties and room bookings. Customers can search for available rooms, make bookings, and view or cancel their reservations. Property owners can create blocks, update bookings, and cancel reservations.

---

## Tech Stack

- Java 21
- Spring Boot
- Spring Data Jpa
- Spring Security (JWT)
- H2-Database
- Maven

---

## How to Run

- Run app: `mvn spring-boot:run`

---

## Tests

- Run tests: `mvn test`

---

## Project Structure

```
booking/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/example/booking/
│   │   │   ├── configuration/
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── entity/
│   │   │   ├── enums/
│   │   │   ├── exceptions/
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   ├── BookingApplication.java
│   │   └── resources/
│   └── test/
│       ├── java/com/example/booking/
│       │   ├── controller/
│       │   └── factory/
│       │   └── service/
│       └── resources/
```

---

## Features

- **Authentication**: Users can authenticate via JWT tokens.
- **Room Management**: Property owners can create, update, and delete bookings or blocks.
- **Room Availability**: Customers can search for available rooms based on filters.
- **Booking Management**: Customers can create, update, and cancel bookings. Property owners can manage booking blocks.

---

## API Endpoints

### 1. **Authenticate**
`POST /api/v1/auth/login`
Authenticate and retrieve a JWT token (fixed credentials for tests).

Request:
```json
{
    "username": "guest_user",
    "password": "123456"
}
```

Response:
```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0X3VzZXIiLCJpYXQiOjE3NDAwNTg2MDAsImV4cCI6MTc0MDA2MjIwMH0.qvnz1tYysZ7Q-gAH83FWzMuiVv0l-X2xGin_vf7lBYY"
}
```

### 2. **Get Available Rooms**
`GET /api/v1/rooms`
Fetch all available rooms based on filters.

Request:
```bash
GET localhost:8080/api/v1/rooms?propertyType=HOUSE&guestQuantity=2&startDate=2025-10-15&endDate=2025-10-20
```

Response:
```json
{
    "content": [
        {
            "id": 1,
            "property": {
                "id": 1,
                "name": "Gravata House",
                "type": "HOUSE",
                "user": {
                    "id": 2,
                    "username": "property_owner",
                    "email": "property_owner@example.com",
                    "type": "PROPERTY_OWNER"
                }
            },
            "name": "Entire House",
            "capacity": 10,
            "daily_price": 500.00
        }
    ],
    "pageable": { }
}
```

### 3. **Make a Booking**
`POST /api/v1/bookings`
Create a booking with selected room and guest details.

Request:
```json
{
    "room_id": 1,
    "guest_quantity": 2,
    "start_date": "2025-10-15", 
    "end_date": "2025-10-20",
    "guests": [
        {
            "full_name": "Joao da Silva",
            "document_number": "A12345",
            "birth_date": "1992-08-06",
            "main_guest": true
        },
        {
            "full_name": "Maria da Silva",
            "document_number": "B12345",
            "birth_date": "1994-04-30",
            "main_guest": false
        }
    ]
}
```

Response:
```json
{
    "id": 2,
    "user": {
        "id": 1,
        "username": "guest_user",
        "email": "guest_user@example.com",
        "type": "CUSTOMER"
    },
    "room": {
        "id": 1,
        "property": {
            "id": 1,
            "name": "Natal Dunnas Hotel",
            "type": "HOTEL",
            "user": {
                "id": 2,
                "username": "property_owner",
                "email": "property_owner@example.com",
                "type": "PROPERTY_OWNER"
            }
        },
        "name": "Standard Room",
        "capacity": 2,
        "daily_price": 40.00
    },
    "status": "CONFIRMED",
    "type": "GUEST",
    "guest_quantity": 2,
    "start_date": "2025-10-15",
    "end_date": "2025-10-20",
    "total_price": 200.00,
    "payment_status": "PAID",
    "guests": [
        {
            "id": 3,
            "full_name": "Joao da Silva",
            "document_number": "A12345",
            "birth_date": "1992-08-06",
            "main_guest": true
        },
        {
            "id": 4,
            "full_name": "Maria da Silva",
            "document_number": "B12345",
            "birth_date": "1994-04-30",
            "main_guest": false
        }
    ]
}
```

### 4. **Update an Existing Booking**
`PUT /api/v1/bookings/{id}`
Update an existing booking with new details.

Request:
```json
{
    "room_id": 1,
    "guest_quantity": 2,
    "start_date": "2025-10-16",
    "end_date": "2025-10-18",
    "guests": [
        {
            "full_name": "Fulano da Silva",
            "document_number": "A123452",
            "birth_date": "1990-06-04",
            "main_guest": false
        },
        {
            "full_name": "Sicrano da Silva",
            "document_number": "B123452",
            "birth_date": "1989-07-21",
            "main_guest": true
        }
    ]
}
```

### 5. **Cancel a Booking**
`PUT /api/v1/bookings/cancel/{id}`
Cancel a booking by Id.

Request:
```bash
PUT localhost:8080/api/v1/bookings/cancel/2
```

### 6. **Rebook a Canceled Booking**
`PUT /api/v1/bookings/{id}/rebook`
Rebook a canceled booking.

Request:
```bash
PUT localhost:8080/api/v1/bookings/2/rebook
```

### 7. **Get a Booking by Id**
`GET /api/v1/bookings/{id}`
Get a booking by Id.

Request:
```bash
GET localhost:8080/api/v1/bookings/1
```

### 8. **Get Bookings**
`GET /api/v1/bookings`
Get a Pageable list of Bookings filtered by params.

Request:
```bash
GET localhost:8080/api/v1/bookings?propertyType=HOUSE
```

### 9. **Create a Block**
`POST /api/v1/bookings/blocks`
Create a block to make a room unavailable for bookings.

Request:
```json
{
    "room_id": 1,
    "start_date": "2025-11-01", 
    "end_date": "2025-11-30"
}
```

### 10. **Update a Block**
`PUT /api/v1/bookings/blocks/{id}`
Update an existing block's details.

Request:
```json
{
    "room_id": 1,
    "start_date": "2025-11-01", 
    "end_date": "2025-11-15"
}
```

### 11. **Delete a Booking or Block**
`DELETE /api/v1/bookings/{id}`
Delete an existing booking or block by booking ID.

Request:
```bash
DELETE localhost:8080/api/v1/bookings/2
```
