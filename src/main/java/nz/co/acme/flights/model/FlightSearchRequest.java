package nz.co.acme.flights.model;

import lombok.Getter;

@Getter
public class FlightSearchRequest {
    private AirportCode origin;

    private AirportCode destination;

    private String date; // format: DD/MM/YYYY
}
