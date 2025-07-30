package nz.co.acme.flights.repository;

import nz.co.acme.flights.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookingsRepository extends JpaRepository<Booking, UUID> {
    boolean existsByFlight_FlightIdAndPassengerEmail(UUID flightId, String passengerEmail);
}