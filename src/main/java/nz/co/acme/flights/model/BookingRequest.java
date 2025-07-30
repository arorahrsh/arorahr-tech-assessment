package nz.co.acme.flights.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.UUID;

@Getter
public class BookingRequest {
    @NotNull
    private UUID flightId;

    @NotBlank
    private String passengerName;

    @Email
    private String passengerEmail;
}
