package nz.co.acme.flights.service;

import lombok.AllArgsConstructor;
import nz.co.acme.flights.model.Booking;
import nz.co.acme.flights.model.Flight;
import nz.co.acme.flights.model.Passenger;
import nz.co.acme.flights.repository.BookingsRepository;
import nz.co.acme.flights.repository.FlightsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Service
public class BookingsService {

    private final FlightsRepository flightsRepository;

    private final BookingsRepository bookingsRepository;

    public Booking createBooking(UUID flightId, Passenger passenger) {
        Flight flight = flightsRepository.findById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight ID not found"));

        Booking booking = new Booking(
                UUID.randomUUID(),
                flight,
                passenger.getPassengerName(),
                passenger.getPassengerEmail(),
                "CONFIRMED",
                LocalDateTime.now());

        return bookingsRepository.save(booking);
    }

    public void deleteBooking(UUID bookingId) {
        boolean exists = bookingsRepository.existsById(bookingId);
        if (!exists) {
            throw new RuntimeException("Booking ID not found: " + bookingId);
        }
        bookingsRepository.deleteById(bookingId);
    }
}