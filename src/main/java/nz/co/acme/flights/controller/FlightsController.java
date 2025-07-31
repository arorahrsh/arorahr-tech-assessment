package nz.co.acme.flights.controller;

import com.google.gson.Gson;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nz.co.acme.flights.model.Flight;
import nz.co.acme.flights.model.FlightRequest;
import nz.co.acme.flights.service.FlightsService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;

import java.util.List;

@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/v1/flights")
public class FlightsController {

    private final FlightsService flightsService;

    private final Logger logger;

    private final Gson gson;

    @PostMapping
    public ResponseEntity<List<Flight>> getFlights(@Valid @RequestBody FlightRequest request) {
        logger.info("Received POST /v1/flights with search request: {}", gson.toJson(request));

        List<Flight> result = flightsService.getFlights(request);

        logger.info("Found {} flights matching search request", result.size());

        logger.info("Returning 200 HTTP status code and JSON response: {}", gson.toJson(result));

        return ResponseEntity.ok(result);
    }
}
