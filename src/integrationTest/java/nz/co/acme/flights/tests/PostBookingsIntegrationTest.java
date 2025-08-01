package nz.co.acme.flights.tests;

import nl.altindag.log.LogCaptor;
import nz.co.acme.flights.model.AirportCode;
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

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class PostBookingsIntegrationTest {

    @Autowired
    private FlightsRepository flightsRepository;

    @Autowired
    private BookingsRepository bookingsRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    private UUID validFlightId;

    private LogCaptor logCaptor;

    @BeforeEach
    void setup() {
        bookingsRepository.deleteAll();
        flightsRepository.deleteAll();

        validFlightId = UUID.randomUUID();
        flightsRepository.save(new Flight(validFlightId, "ACME999", AirportCode.AKL, AirportCode.WLG,
                ZonedDateTime.parse("2029-08-01T08:00:00+12:00"),
                ZonedDateTime.parse("2029-08-01T09:00:00+12:00"), 99.00));

        logCaptor = LogCaptor.forRoot();
        logCaptor.clearLogs();
    }

    @Test
    void whenValidRequest_thenReturns201AndLogs() {
        // arrange
        String json = """
            {
              "flightId": "%s",
              "passengerName": "Martin Zhu",
              "passengerEmail": "martin.zhu@example.com"
            }
            """.formatted(validFlightId);

        // act
        ResponseEntity<String> response = postHttpRequest(json);

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).contains("CONFIRMED");

        List<String> logs = logCaptor.getInfoLogs();
        assertThat(logs.get(0)).contains("Received POST /v1/bookings with request");
        assertThat(logs.get(1)).contains("Created booking successfully");
        assertThat(logs.get(2)).contains("Returning 201 HTTP status code and JSON response");
    }

    @Test
    void whenFlightIdNotFound_thenReturns404AndLogs() {
        // arrange
        String json = """
            {
              "flightId": "11111111-2222-3333-4444-555555555555",
              "passengerName": "Alice",
              "passengerEmail": "alice@example.com"
            }
            """;

        // act
        ResponseEntity<String> response = postHttpRequest(json);

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("Flight ID not found");

        assertThat(logCaptor.getWarnLogs().getFirst()).contains("Flight ID not found");
    }

    @Test
    void whenMissingFields_thenReturns400() {
        // arrange
        String json = "{}";

        // act
        ResponseEntity<String> response = postHttpRequest(json);

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("The field 'flightId' is invalid or missing");
        assertThat(response.getBody()).contains("The field 'passengerName' is invalid or missing");
        assertThat(response.getBody()).contains("The field 'passengerEmail' is invalid or missing");
    }

    @Test
    void whenDuplicateBooking_thenReturns422() {
        // arrange
        String json = """
            {
              "flightId": "%s",
              "passengerName": "Martin Zhu",
              "passengerEmail": "martin.zhu@example.com"
            }
            """.formatted(validFlightId);
        postHttpRequest(json);

        // act
        ResponseEntity<String> response = postHttpRequest(json);

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody()).contains("Booking already exists for this passenger");
    }

    @Test
    void whenInvalidEmail_thenReturns400() {
        // arrange
        String json = """
            {
              "flightId": "%s",
              "passengerName": "Bob",
              "passengerEmail": "invalid-email"
            }
            """.formatted(validFlightId);

        // act
        ResponseEntity<String> response = postHttpRequest(json);

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("The field 'passengerEmail' is invalid");
    }

    private ResponseEntity<String> postHttpRequest(String requestBodyJson) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestBodyJson, headers);
        return restTemplate.postForEntity("/v1/bookings", entity, String.class);
    }
}
