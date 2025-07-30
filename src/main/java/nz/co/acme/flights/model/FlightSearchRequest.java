package nz.co.acme.flights.model;

public record FlightSearchRequest(AirportCode origin, AirportCode destination, String travelDate) {

}
