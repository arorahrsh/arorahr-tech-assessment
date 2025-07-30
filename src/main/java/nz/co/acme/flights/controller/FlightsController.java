package nz.co.acme.flights.controller;

import nz.co.acme.flights.model.Flight;
import nz.co.acme.flights.model.FlightSearchRequest;
import nz.co.acme.flights.service.FlightsService;
import nz.co.acme.flights.utils.GsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;

import java.util.List;

@RestController
@RequestMapping("/v1/flights")
public class FlightsController {

    @Autowired
    private FlightsService flightsService;

    @Autowired
    private Logger logger;

    @PostMapping
    public ResponseEntity<List<Flight>> getFlights(@RequestBody FlightSearchRequest params) {
        logger.info("Received POST /v1/flights with JSON request payload: {}", GsonHelper.toJson(params));
        List<Flight> result = flightsService.getFlights(params.origin(), params.destination(), params.travelDate());
        logger.info("Returning 200 HTTP status code and JSON response payload: {}", GsonHelper.toJson(result));
        return ResponseEntity.ok(result);
    }
}
