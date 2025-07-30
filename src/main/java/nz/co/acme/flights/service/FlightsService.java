package nz.co.acme.flights.service;

import lombok.AllArgsConstructor;
import nz.co.acme.flights.model.AirportCode;
import nz.co.acme.flights.model.Flight;
import nz.co.acme.flights.repository.FlightsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class FlightsService {

    private final FlightsRepository flightsRepository;

    public List<Flight> getFlights(AirportCode origin, AirportCode destination, LocalDate travelDate) {
        List<Flight> flights = flightsRepository.findByOriginAndDestination(origin, destination);
        return flights.stream()
                .filter(flight -> flight.getDepartureTime().toLocalDate().equals(travelDate))
                .collect(Collectors.toList());
    }
}