package nz.co.acme.flights.tests;

import nl.altindag.log.LogCaptor;
import nz.co.acme.flights.model.AirportCode;
import nz.co.acme.flights.model.Flight;
import nz.co.acme.flights.repository.FlightsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.http.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class PostFlightsIntegrationTest {

    @Autowired
    private FlightsRepository flightsRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    private LogCaptor logCaptor;

    @BeforeEach
    void setup() {
        flightsRepository.deleteAll();

        flightsRepository.save(new Flight(UUID.randomUUID(), "ACME101", AirportCode.AKL, AirportCode.WLG,
                ZonedDateTime.parse("2029-07-30T08:00:00+12:00"),
                ZonedDateTime.parse("2029-07-30T09:00:00+12:00"), 89.99));

        flightsRepository.save(new Flight(UUID.randomUUID(), "ACME202", AirportCode.CHC, AirportCode.AKL,
                ZonedDateTime.parse("2029-07-30T10:00:00+12:00"),
                ZonedDateTime.parse("2029-07-30T11:30:00+12:00"), 110.00));

        flightsRepository.save(new Flight(UUID.randomUUID(), "ACME303", AirportCode.AKL, AirportCode.WLG,
                ZonedDateTime.parse("2029-07-31T14:15:00+12:00"),
                ZonedDateTime.parse("2029-07-31T16:00:00+12:00"), 150.50));

        logCaptor = LogCaptor.forRoot();
        logCaptor.clearLogs();
    }

    @Test
    void whenValidRequest_thenReturnsMatchingFlightsAndLogsInOrder() {
        // arrange
        String json = """
            {
              "origin": "AKL",
              "destination": "WLG",
              "date": "30/07/2029"
            }
            """;

        // act
        ResponseEntity<String> response = postHttpRequest(json);

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("ACME101");

        List<String> logs = logCaptor.getInfoLogs();
        assertThat(logs.get(0)).contains("Received POST /v1/flights with search request");
        assertThat(logs.get(1)).contains("Found 1 flights matching search request");
        assertThat(logs.get(2)).contains("Returning 200 HTTP status code and JSON response");
    }

    @Test
    void whenNoFlightsFound_thenReturnsEmptyListAndLogs() {
        // arrange
        String json = """
            {
              "origin": "AKL",
              "destination": "WLG",
              "date": "01/08/2029"
            }
            """;

        // act
        ResponseEntity<String> response = postHttpRequest(json);

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("[]");

        List<String> logs = logCaptor.getInfoLogs();
        assertThat(logs.get(0)).contains("Received POST /v1/flights with search request");
        assertThat(logs.get(1)).contains("Found 0 flights matching search request");
        assertThat(logs.get(2)).contains("Returning 200 HTTP status code and JSON response");
    }

    @Test
    void whenOriginEqualsDestination_thenReturns422AndLogsError() {
        // arrange
        String json = """
            {
              "origin": "AKL",
              "destination": "AKL",
              "date": "30/07/2029"
            }
            """;

        // act
        ResponseEntity<String> response = postHttpRequest(json);

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody()).contains("Origin and destination city cannot be the same");

        List<String> infoLogs = logCaptor.getInfoLogs();
        assertThat(infoLogs.getFirst()).contains("Received POST /v1/flights with search request");

        List<String> warnLogs = logCaptor.getWarnLogs();
        assertThat(warnLogs.getFirst()).contains("Origin and destination city cannot be the same");
    }

    @Test
    void whenDateIsInvalidFormat_thenReturns422AndLogsError() {
        // arrange
        String json = """
            {
              "origin": "AKL",
              "destination": "WLG",
              "date": "invalid-date"
            }
            """;

        // act
        ResponseEntity<String> response = postHttpRequest(json);

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody()).contains("Travel date must be provided in DD/MM/YYYY format");

        List<String> infoLogs = logCaptor.getInfoLogs();
        assertThat(infoLogs.getFirst()).contains("Received POST /v1/flights with search request");

        List<String> warnLogs = logCaptor.getWarnLogs();
        assertThat(warnLogs.getFirst()).contains("Travel date must be provided in DD/MM/YYYY format");
    }

    @Test
    void whenDateIsPast_thenReturns422AndLogsError() {
        // arrange
        String json = """
            {
              "origin": "AKL",
              "destination": "WLG",
              "date": "01/01/2020"
            }
            """;

        // act
        ResponseEntity<String> response = postHttpRequest(json);

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody()).contains("Travel date cannot be in the past");

        List<String> infoLogs = logCaptor.getInfoLogs();
        assertThat(infoLogs.getFirst()).contains("Received POST /v1/flights with search request");

        List<String> warnLogs = logCaptor.getWarnLogs();
        assertThat(warnLogs.getFirst()).contains("Travel date cannot be in the past");
    }

    private ResponseEntity<String> postHttpRequest(String requestBodyJson) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestBodyJson, headers);
        return restTemplate.postForEntity("/v1/flights", entity, String.class);
    }
}
