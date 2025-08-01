package nz.co.acme.flights.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class FlightRequest {
    @NotNull
    private AirportCode origin;

    @NotNull
    private AirportCode destination;

    @NotBlank
    private String date; // format: DD/MM/YYYY
}
