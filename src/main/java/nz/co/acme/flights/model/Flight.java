package nz.co.acme.flights.model;

import java.util.UUID;

public record Flight(UUID flightId, String flightCode, String origin, String destination, String departureTime, String arrivalTime, Double price) {
}
