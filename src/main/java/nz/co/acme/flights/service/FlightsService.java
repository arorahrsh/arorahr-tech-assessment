package nz.co.acme.flights.service;

import nz.co.acme.flights.model.Flight;
import nz.co.acme.flights.model.FlightRequest;
import java.util.List;

public interface FlightsService {
    List<Flight> getFlights(FlightRequest request);
}