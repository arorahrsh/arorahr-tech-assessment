package nz.co.acme.flights.service;

import nz.co.acme.flights.model.AirportCode;
import nz.co.acme.flights.model.Flight;
import nz.co.acme.flights.repository.FlightsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FlightsServiceTest {

    private FlightsRepository flightsRepository;

    private FlightsService flightsService;

    @BeforeEach
    void setUp() {
        flightsRepository = mock(FlightsRepository.class);
        flightsService = new FlightsService(flightsRepository);
    }

    @Test
    void whenFlightsMatchDate_thenReturnMatchingFlights() {
        // arrange
        AirportCode origin = AirportCode.AKL;
        AirportCode destination = AirportCode.WLG;
        LocalDate travelDate = LocalDate.of(2025, 8, 1);

        Flight flight1 = mockFlight("ACME102", "2025-08-01T08:00:00+12:00"); // match
        Flight flight2 = mockFlight("ACME104", "2025-08-01T08:00:00+12:00"); // match
        Flight flight3 = mockFlight("ACME288", "2025-08-02T08:00:00+12:00"); // no match

        when(flightsRepository.findByOriginAndDestination(origin, destination))
                .thenReturn(List.of(flight1, flight2, flight3));

        // act
        List<Flight> result = flightsService.getFlights(origin, destination, travelDate);

        // assert
        assertEquals(2, result.size());
        assertTrue(result.contains(flight1));
        assertTrue(result.contains(flight2));
        assertFalse(result.contains(flight3));
    }

    @Test
    void whenNoFlightsMatchDate_thenReturnEmptyList() {
        // arrange
        AirportCode origin = AirportCode.AKL;
        AirportCode destination = AirportCode.WLG;
        LocalDate travelDate = LocalDate.of(2025, 8, 1);

        Flight flight = mockFlight("ACME102", "2027-06-03T08:00:00+12:00"); // no match

        when(flightsRepository.findByOriginAndDestination(origin, destination))
                .thenReturn(List.of(flight));

        // act
        List<Flight> result = flightsService.getFlights(origin, destination, travelDate);

        // assert
        assertTrue(result.isEmpty());
    }

    @Test
    void whenRepositoryReturnsNoFlights_thenReturnEmptyList() {
        // arrange
        AirportCode origin = AirportCode.AKL;
        AirportCode destination = AirportCode.WLG;
        LocalDate travelDate = LocalDate.of(2025, 8, 1);

        when(flightsRepository.findByOriginAndDestination(origin, destination))
                .thenReturn(List.of());

        // act
        List<Flight> result = flightsService.getFlights(origin, destination, travelDate);

        // assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    private Flight mockFlight(String flightCode, String departureTime) {
        return new Flight(
                UUID.randomUUID(),
                flightCode,
                AirportCode.AKL,
                AirportCode.WLG,
                ZonedDateTime.parse(departureTime),
                ZonedDateTime.parse(departureTime).plusMinutes(60),
                100.00);
    }
}
