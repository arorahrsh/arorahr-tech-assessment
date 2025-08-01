package nz.co.acme.flights.tests;

import nl.altindag.log.LogCaptor;
import nz.co.acme.flights.model.AirportCode;
import nz.co.acme.flights.model.Booking;
import nz.co.acme.flights.model.Flight;
import nz.co.acme.flights.repository.BookingsRepository;
import nz.co.acme.flights.repository.FlightsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class DeleteBookingsIntegrationTest {

    @Autowired
    private FlightsRepository flightsRepository;

    @Autowired
    private BookingsRepository bookingsRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    private UUID existingBookingId;
    private LogCaptor logCaptor;

    @BeforeEach
    void setup() {
        bookingsRepository.deleteAll();
        flightsRepository.deleteAll();

        // Add a flight and booking
        UUID flightId = UUID.randomUUID();
        Flight flight = new Flight(flightId, "ACME888", AirportCode.AKL, AirportCode.CHC,
                ZonedDateTime.parse("2029-08-10T12:00:00+12:00"),
                ZonedDateTime.parse("2029-08-10T13:30:00+12:00"), 120.00);
        flightsRepository.save(flight);

        Booking booking = new Booking(UUID.randomUUID(), flight, "testuser@example.com",
                "Test User", "CONFIRMED", LocalDateTime.now());
        bookingsRepository.save(booking);
        existingBookingId = booking.getBookingId();

        logCaptor = LogCaptor.forRoot();
        logCaptor.clearLogs();
    }

    @Test
    void whenValidBookingId_thenDeletesAndLogs() {
        // act
        ResponseEntity<Void> response = restTemplate.exchange("/v1/bookings/" + existingBookingId, HttpMethod.DELETE, null, Void.class);

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(bookingsRepository.findById(existingBookingId)).isEmpty();

        List<String> logs = logCaptor.getInfoLogs();
        assertThat(logs).containsSubsequence(
                "Received DELETE /v1/bookings/" + existingBookingId,
                "Deleted booking with ID " + existingBookingId + ", returning 204 HTTP status code"
        );
    }

    @Test
    void whenBookingIdNotFound_thenReturns404AndLogs() {
        // arrange
        UUID randomId = UUID.fromString("11111111-1111-1111-1111-111111111111");

        // act
        ResponseEntity<String> response = restTemplate.exchange("/v1/bookings/" + randomId, HttpMethod.DELETE, null, String.class);

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("Booking ID not found");

        List<String> warnLogs = logCaptor.getWarnLogs();
        assertThat(warnLogs.getFirst()).contains("Booking ID not found");
    }

    @Test
    void whenIdFormatInvalid_thenReturns400() {
        // act
        ResponseEntity<String> response = restTemplate.exchange("/v1/bookings/invalid-id", HttpMethod.DELETE, null, String.class);

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Invalid value for bookingId: must be a valid UUID");
    }
}
