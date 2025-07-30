package nz.co.acme.flights.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "flights")
public class Flight {
    @Id
    private UUID flightId;

    private String flightCode;

    @Enumerated(EnumType.STRING)
    private AirportCode origin;

    @Enumerated(EnumType.STRING)
    private AirportCode destination;

    private String departureTime;

    private String arrivalTime;

    private Double price;
}