package nz.co.acme.flights.service;

import lombok.AllArgsConstructor;
import nz.co.acme.flights.exception.BusinessRuleException;
import nz.co.acme.flights.exception.NotFoundException;
import nz.co.acme.flights.model.Booking;
import nz.co.acme.flights.model.Flight;
import nz.co.acme.flights.model.Passenger;
import nz.co.acme.flights.repository.BookingsRepository;
import nz.co.acme.flights.repository.FlightsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;

@AllArgsConstructor
@Service
class BookingsServiceImpl implements BookingsService {

    private final FlightsRepository flightsRepository;

    private final BookingsRepository bookingsRepository;

    @Override
    public Booking createBooking(UUID flightId, Passenger passenger) {
        Flight flight = flightsRepository.findById(flightId)
                .orElseThrow(() -> new NotFoundException("Flight ID not found"));

        if (bookingsRepository.existsByFlight_FlightIdAndPassengerEmail(flightId, passenger.getPassengerEmail())) {
            throw new BusinessRuleException("Booking already exists for this passenger");
        }

        Booking booking = new Booking(
                UUID.randomUUID(),
                flight,
                passenger.getPassengerName(),
                passenger.getPassengerEmail(),
                "CONFIRMED",
                LocalDateTime.now());

        return bookingsRepository.save(booking);
    }

    @Override
    public void deleteBooking(UUID bookingId) {
        Booking booking = bookingsRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking ID not found"));

        if (booking.getFlight().getDepartureTime().isBefore(ZonedDateTime.now())) {
            throw new BusinessRuleException("Booking cannot be cancelled as flight has already departed");
        }

        bookingsRepository.deleteById(bookingId);
    }
}