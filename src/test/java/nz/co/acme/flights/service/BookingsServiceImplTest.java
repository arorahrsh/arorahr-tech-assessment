package nz.co.acme.flights.service;

import nz.co.acme.flights.exception.BusinessRuleException;
import nz.co.acme.flights.exception.NotFoundException;
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

class BookingsServiceImplTest {

    private FlightsRepository flightsRepository;

    private BookingsRepository bookingsRepository;

    private BookingsService bookingsService;

    @BeforeEach
    void setUp() {
        flightsRepository = mock(FlightsRepository.class);
        bookingsRepository = mock(BookingsRepository.class);
        bookingsService = new BookingsServiceImpl(flightsRepository, bookingsRepository);
    }

    @Test
    void whenCreateValidBooking_thenBookingIsSaved() {
        // arrange
        UUID flightId = UUID.randomUUID();
        Passenger passenger = new Passenger("Jane Doe", "jane@example.com");

        Flight flight = mock(Flight.class);
        when(flightsRepository.findById(flightId)).thenReturn(Optional.of(flight));
        when(bookingsRepository.existsByFlight_FlightIdAndPassengerEmail(flightId, passenger.getPassengerEmail()))
                .thenReturn(false);

        Booking mockSavedBooking = mock(Booking.class);
        when(bookingsRepository.save(any(Booking.class))).thenReturn(mockSavedBooking);

        // act
        Booking result = bookingsService.createBooking(flightId, passenger);

        // assert
        assertNotNull(result);
        verify(bookingsRepository).save(any(Booking.class));
    }

    @Test
    void whenDeleteValidBooking_thenDeletesSuccessfully() {
        // arrange
        UUID bookingId = UUID.randomUUID();

        Flight flight = mock(Flight.class);
        when(flight.getDepartureTime()).thenReturn(ZonedDateTime.now().plusHours(1));

        Booking booking = mock(Booking.class);
        when(booking.getFlight()).thenReturn(flight);

        when(bookingsRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        // act
        bookingsService.deleteBooking(bookingId);

        // assert
        verify(bookingsRepository).deleteById(bookingId);
    }

    @Test
    void whenCreateBookingWithUnknownFlight_thenThrowsException() {
        // arrange
        UUID flightId = UUID.randomUUID();
        Passenger passenger = new Passenger("John Doe", "john@example.com");

        when(flightsRepository.findById(flightId)).thenReturn(Optional.empty());

        // act & assert
        NotFoundException ex = assertThrows(NotFoundException.class, () -> {
            bookingsService.createBooking(flightId, passenger);
        });

        assertEquals("Flight ID not found", ex.getMessage());
    }

    @Test
    void whenCreateBookingForExistingPassenger_thenThrowsException() {
        // arrange
        UUID flightId = UUID.randomUUID();
        Passenger passenger = new Passenger("John Doe", "john@example.com");
        Flight flight = mock(Flight.class);

        when(flightsRepository.findById(flightId)).thenReturn(Optional.of(flight));
        when(bookingsRepository.existsByFlight_FlightIdAndPassengerEmail(flightId, passenger.getPassengerEmail()))
                .thenReturn(true);

        // act & assert
        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () -> {
            bookingsService.createBooking(flightId, passenger);
        });

        assertEquals("Booking already exists for this passenger", ex.getMessage());
    }

    @Test
    void whenDeleteUnknownBooking_thenThrowsException() {
        // arrange
        UUID bookingId = UUID.randomUUID();
        when(bookingsRepository.findById(bookingId)).thenReturn(Optional.empty());

        // act & assert
        NotFoundException ex = assertThrows(NotFoundException.class, () -> {
            bookingsService.deleteBooking(bookingId);
        });

        assertEquals("Booking ID not found", ex.getMessage());
    }

    @Test
    void whenDeletePastBooking_thenThrowsException() {
        // arrange
        UUID bookingId = UUID.randomUUID();

        Flight flight = mock(Flight.class);
        when(flight.getDepartureTime()).thenReturn(ZonedDateTime.now().minusHours(2));

        Booking booking = mock(Booking.class);
        when(booking.getFlight()).thenReturn(flight);

        when(bookingsRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        // act & assert
        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () -> {
            bookingsService.deleteBooking(bookingId);
        });

        assertEquals("Booking cannot be cancelled as flight has already departed", ex.getMessage());
    }
}
