package nz.co.acme.flights.service;

import nz.co.acme.flights.model.AirportCode;
import nz.co.acme.flights.model.Flight;
import nz.co.acme.flights.repository.FlightsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightsService {

    @Autowired
    private FlightsRepository flightsRepository;

    public List<Flight> getFlights(AirportCode origin, AirportCode destination, String travelDate) {
        return flightsRepository.findByOriginAndDestination(origin, destination);
    }
}