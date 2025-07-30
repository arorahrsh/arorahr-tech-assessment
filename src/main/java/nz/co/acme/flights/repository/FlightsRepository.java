package nz.co.acme.flights.repository;

import nz.co.acme.flights.model.AirportCode;
import nz.co.acme.flights.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FlightsRepository extends JpaRepository<Flight, UUID> {
    List<Flight> findByOriginAndDestination(AirportCode origin, AirportCode destination);
}