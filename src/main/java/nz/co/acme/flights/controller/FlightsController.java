package nz.co.acme.flights.controller;

import nz.co.acme.flights.model.Flight;
import nz.co.acme.flights.model.FlightSearchRequest;
import nz.co.acme.flights.repository.FlightsRepository;
import nz.co.acme.flights.service.FlightsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/v1/flights")
public class FlightsController {

    @Autowired
    private FlightsService flightsService;

    @PostMapping
    public ResponseEntity<List<Flight>> getFlights(@RequestBody FlightSearchRequest params) {
        return ResponseEntity.ok(flightsService.getFlights(params.origin(), params.destination(), params.travelDate()));
    }
}
