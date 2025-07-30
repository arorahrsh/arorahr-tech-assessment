CREATE TABLE FLIGHTS (
    flight_id UUID PRIMARY KEY,
    flight_code VARCHAR(10) NOT NULL,
    origin VARCHAR(3) NOT NULL,
    destination VARCHAR(3) NOT NULL,
    departure_time TIMESTAMP WITH TIME ZONE NOT NULL,
    arrival_time TIMESTAMP WITH TIME ZONE NOT NULL,
    price DECIMAL(5, 2) NOT NULL
);

CREATE TABLE BOOKINGS (
    booking_id UUID PRIMARY KEY,
    flight_id UUID NOT NULL,
    passenger_name VARCHAR(100) NOT NULL,
    passenger_email VARCHAR(255) NOT NULL,
    status VARCHAR(20) DEFAULT 'CONFIRMED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_flight
      FOREIGN KEY (flight_id) REFERENCES FLIGHTS (flight_id)
      ON DELETE CASCADE
);