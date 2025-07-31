package nz.co.acme.flights.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class BookingRequest {
    @NotNull
    private UUID flightId;

    @NotBlank
    private String passengerName;

    @Email
    @NotBlank
    private String passengerEmail;
}
