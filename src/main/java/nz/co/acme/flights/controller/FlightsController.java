package nz.co.acme.flights.controller;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import nz.co.acme.flights.model.Flight;
import nz.co.acme.flights.model.FlightSearchRequest;
import nz.co.acme.flights.service.FlightsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/v1/flights")
public class FlightsController {

    private FlightsService flightsService;

    private Logger logger;

    private Gson gson;

    @PostMapping
    public ResponseEntity<List<Flight>> getFlights(@RequestBody FlightSearchRequest request) {
        logger.info("Received POST /v1/flights with search request: {}", gson.toJson(request));

        List<Flight> result = flightsService.getFlights(request.getOrigin(), request.getDestination(),
                LocalDate.parse(request.getDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        logger.info("Found {} flights matching search request", result.size());
        logger.info("Returning 200 HTTP status code and JSON response payload: {}", gson.toJson(result));
        return ResponseEntity.ok(result);
    }
}
