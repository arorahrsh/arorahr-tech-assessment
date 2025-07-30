package nz.co.acme.flights.service;

import lombok.AllArgsConstructor;
import nz.co.acme.flights.exception.BusinessRuleException;
import nz.co.acme.flights.model.Flight;
import nz.co.acme.flights.model.FlightRequest;
import nz.co.acme.flights.repository.FlightsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class FlightsService {

    private final FlightsRepository flightsRepository;

    public List<Flight> getFlights(FlightRequest request) {
        this.validateRequest(request);

        LocalDate travelDate = parseDate(request.getDate());

        List<Flight> flights = flightsRepository.findByOriginAndDestination(request.getOrigin(), request.getDestination());
        return flights.stream()
                .filter(flight -> flight.getDepartureTime().toLocalDate().equals(travelDate))
                .collect(Collectors.toList());
    }

    private void validateRequest(FlightRequest request) {
        if (request.getOrigin().equals(request.getDestination()))
            throw new BusinessRuleException("Origin and destination city cannot be the same.");

        LocalDate travelDate = parseDate(request.getDate());
        if (travelDate.isBefore(LocalDate.now()))
            throw new BusinessRuleException("Travel date cannot be in the past.");
    }

    private LocalDate parseDate(String date) {
        try {
            return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (Exception e) {
            throw new BusinessRuleException("Travel date must be provided in DD/MM/YYYY format.");
        }
    }
}