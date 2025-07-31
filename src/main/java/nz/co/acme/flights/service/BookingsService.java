package nz.co.acme.flights.service;

import nz.co.acme.flights.model.Booking;
import nz.co.acme.flights.model.Passenger;
import java.util.UUID;

public interface BookingsService {
    Booking createBooking(UUID flightId, Passenger passenger);

    void deleteBooking(UUID bookingId);
}