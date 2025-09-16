# Hilton Hotel Chain Management System

A Spring Boot application for managing the Hilton Hotel Chain, including hotel branches, rooms, guests, and reservations.

## Features

- Hotel Management
  - Add new hotels
  - View hotel details
  - List all hotels
- Room Management
  - Add rooms to hotels
  - View room details
  - List all rooms in a hotel
  - Check room availability
- Guest Management
  - Register new guests
  - View guest details
  - List guests by hotel
- Reservation System
  - Make room reservations
  - Cancel reservations
  - View reservation details
  - List reservations by hotel/guest

## Technologies

- Java 17
- Spring Boot 3.1.4
- Spring Data JPA
- PostgreSQL
- Lombok
- Maven

## Prerequisites

- JDK 17 or later
- Maven 3.6+
- PostgreSQL 12+

## Database Setup

1. Install PostgreSQL if not already installed
2. Create a new database:
   ```sql
   CREATE DATABASE hilton_hotel_chain;
   ```
3. Update database configuration in `src/main/resources/application.properties` if needed:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/hilton_hotel_chain
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

## Building and Running

1. Clone the repository:
   ```bash
   git clone https://github.com/stefi19/HiltonHotelChain.git
   cd HiltonHotelChain
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

## API Endpoints

### Hotels
- POST `/api/hotels` - Add a new hotel
- GET `/api/hotels/{hotelId}` - Get hotel details
- GET `/api/hotels` - List all hotels

### Rooms
- POST `/api/rooms` - Add a new room
- GET `/api/rooms/{roomNumber}` - Get room details
- GET `/api/rooms/hotel/{hotelId}` - List all rooms in a hotel
- GET `/api/rooms/hotel/{hotelId}/available` - List available rooms in a hotel

### Guests
- POST `/api/guests` - Register a new guest
- GET `/api/guests/{guestId}` - Get guest details
- GET `/api/guests/hotel/{hotelId}` - List guests in a hotel

### Reservations
- POST `/api/reservations` - Make a new reservation
- POST `/api/reservations/{reservationId}/cancel` - Cancel a reservation
- GET `/api/reservations/{reservationId}` - Get reservation details
- GET `/api/reservations/hotel/{hotelId}` - List reservations by hotel
- GET `/api/reservations/guest/{guestId}` - List reservations by guest

## Example Request/Response

### Adding a new hotel
```json
POST /api/hotels
Request:
{
    "name": "Hilton Downtown",
    "location": "123 Main St, New York, NY 10001"
}

Response:
{
    "hotelId": 1,
    "name": "Hilton Downtown",
    "location": "123 Main St, New York, NY 10001"
}
```

### Making a reservation
```json
POST /api/reservations
Request:
{
    "guestId": 1,
    "hotelId": 1,
    "checkInDate": "2025-09-20",
    "checkOutDate": "2025-09-25"
}

Response:
{
    "reservationId": 1,
    "guestId": 1,
    "hotelId": 1,
    "checkInDate": "2025-09-20",
    "checkOutDate": "2025-09-25",
    "reservationDate": "2025-09-16",
    "status": "CONFIRMED"
}
```

## Error Handling

The API uses standard HTTP status codes:
- 200: Success
- 400: Bad Request
- 404: Not Found
- 500: Internal Server Error

Error responses include a message describing the error.

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.