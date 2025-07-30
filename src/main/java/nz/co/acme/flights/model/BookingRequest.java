package nz.co.acme.flights.model;

import lombok.Getter;

import java.util.UUID;

@Getter
public class BookingRequest {
    private UUID flightId;

    private String passengerName;

    private String passengerEmail;
}
