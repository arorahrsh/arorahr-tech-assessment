package nz.co.acme.flights.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class Passenger {
    private String passengerName;

    private String passengerEmail;

    public static Passenger from(BookingRequest request) {
        return new Passenger(request.getPassengerName(), request.getPassengerEmail());
    }
}
