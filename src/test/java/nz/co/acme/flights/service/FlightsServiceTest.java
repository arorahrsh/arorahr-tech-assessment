package nz.co.acme.flights.service;

import nz.co.acme.flights.exception.BusinessRuleException;
import nz.co.acme.flights.model.AirportCode;
import nz.co.acme.flights.model.Flight;
import nz.co.acme.flights.model.FlightRequest;
import nz.co.acme.flights.repository.FlightsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
        String travelDate = "01/08/2026";

        Flight flight1 = mockFlight("ACME102", "2026-08-01T08:00:00+12:00"); // match
        Flight flight2 = mockFlight("ACME104", "2026-08-01T08:00:00+12:00"); // match
        Flight flight3 = mockFlight("ACME288", "2026-08-02T08:00:00+12:00"); // no match

        when(flightsRepository.findByOriginAndDestination(origin, destination))
                .thenReturn(List.of(flight1, flight2, flight3));

        // act
        FlightRequest request = new FlightRequest(origin, destination, travelDate);
        List<Flight> result = flightsService.getFlights(request);

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

        Flight flight = mockFlight("ACME102", "2026-06-03T08:00:00+12:00"); // no match

        when(flightsRepository.findByOriginAndDestination(origin, destination))
                .thenReturn(List.of(flight));

        // act
        FlightRequest request = new FlightRequest(origin, destination,
                LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        List<Flight> result = flightsService.getFlights(request);

        // assert
        assertTrue(result.isEmpty());
    }

    @Test
    void whenRepositoryReturnsNoFlights_thenReturnEmptyList() {
        // arrange
        AirportCode origin = AirportCode.AKL;
        AirportCode destination = AirportCode.WLG;

        when(flightsRepository.findByOriginAndDestination(origin, destination))
                .thenReturn(List.of());

        // act
        FlightRequest request = new FlightRequest(origin, destination, "20/12/2026");
        List<Flight> result = flightsService.getFlights(request);

        // assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    @Test
    void whenOriginEqualsDestination_thenThrowsException() {
        // arrange
        FlightRequest request = new FlightRequest(AirportCode.AKL, AirportCode.AKL, "01/08/2025");

        // act & assert
        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () -> flightsService.getFlights(request));
        assertEquals("Origin and destination city cannot be the same", ex.getMessage());
    }

    @Test
    void whenTravelDateIsPast_thenThrowsException() {
        // arrange
        LocalDate yesterday = LocalDate.now().minusDays(1);
        FlightRequest request = new FlightRequest(AirportCode.AKL, AirportCode.WLG, yesterday.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        // act & assert
        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () -> flightsService.getFlights(request));
        assertEquals("Travel date cannot be in the past", ex.getMessage());
    }

    @Test
    void whenInvalidDateFormat_thenThrowsException() {
        // arrange
        FlightRequest request = new FlightRequest(AirportCode.AKL, AirportCode.WLG, "2025-08-01");

        // act & assert
        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () -> flightsService.getFlights(request));
        assertEquals("Travel date must be provided in DD/MM/YYYY format", ex.getMessage());
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
