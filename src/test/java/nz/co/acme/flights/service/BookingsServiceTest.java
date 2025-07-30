package nz.co.acme.flights.service;

import nz.co.acme.flights.model.AirportCode;
import nz.co.acme.flights.model.Booking;
import nz.co.acme.flights.model.Flight;
import nz.co.acme.flights.model.Passenger;
import nz.co.acme.flights.repository.BookingsRepository;
import nz.co.acme.flights.repository.FlightsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingsServiceTest {

    private FlightsRepository flightsRepository;

    private BookingsRepository bookingsRepository;

    private BookingsService bookingsService;

    @BeforeEach
    void setUp() {
        flightsRepository = mock(FlightsRepository.class);
        bookingsRepository = mock(BookingsRepository.class);
        bookingsService = new BookingsService(flightsRepository, bookingsRepository);
    }

    @Test
    void whenFlightIdValid_thenBookingIsCreated() {
        // arrange
        UUID flightId = UUID.randomUUID();
        Passenger passenger = new Passenger("Test passenger", "passenger@test.com");
        Flight flight = new Flight(flightId, "ACME123", AirportCode.AKL, AirportCode.WLG,
                ZonedDateTime.now(), ZonedDateTime.now().plusHours(1), 100.0);

        when(flightsRepository.findById(flightId)).thenReturn(Optional.of(flight));
        when(bookingsRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // act
        Booking booking = bookingsService.createBooking(flightId, passenger);

        // assert
        assertNotNull(booking.getBookingId());
        assertEquals("Test passenger", booking.getPassengerName());
        assertEquals("passenger@test.com", booking.getPassengerEmail());
        assertEquals("CONFIRMED", booking.getStatus());
        assertEquals(flight, booking.getFlight());
        verify(bookingsRepository).save(any(Booking.class));
    }

    @Test
    void whenFlightIdNotFound_thenThrowException() {
        // arrange
        UUID flightId = UUID.randomUUID();
        Passenger passenger = new Passenger("Test passenger", "passenger@test.com");

        when(flightsRepository.findById(flightId)).thenReturn(Optional.empty());

        // act & assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookingsService.createBooking(flightId, passenger));

        assertEquals("Flight ID not found", exception.getMessage());
        verify(bookingsRepository, never()).save(any());
    }
}
