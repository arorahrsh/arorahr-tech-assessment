package nz.co.acme.flights.controller;

import nz.co.acme.flights.model.Flight;
import nz.co.acme.flights.model.FlightSearchParams;
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

    @PostMapping
    public ResponseEntity<List<Flight>> getFlights(@RequestBody FlightSearchParams params) {
        return ResponseEntity.ok(
                // return mock response for now
                Stream.of("1","2","3").map(c -> new Flight(UUID.randomUUID(), "TEST123", params.origin(), params.destination(), "", "", 99.0)).collect(Collectors.toList())
        );
    }
}
