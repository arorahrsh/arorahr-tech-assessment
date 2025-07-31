package nz.co.acme.flights.controller;

import com.google.gson.Gson;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import nz.co.acme.flights.model.*;
import nz.co.acme.flights.service.BookingsService;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/v1/bookings")
public class BookingsController {

    private final BookingsService bookingsService;

    private final Logger logger;

    private final Gson gson;

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingRequest request) {
        logger.info("Received POST /v1/bookings with request: {}", gson.toJson(request));

        Booking booking = bookingsService.createBooking(request.getFlightId(), Passenger.from(request));
        logger.info("Created booking successfully [flightId: {}, bookingId: {}]", request.getFlightId(), booking.getBookingId());

        BookingResponse response = new BookingResponse(booking.getBookingId(), booking.getStatus());

        logger.info("Returning 201 HTTP status code and JSON response: {}", gson.toJson(response));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<Void> deleteBooking(@NotNull @PathVariable String bookingId) {
        logger.info("Received DELETE /v1/bookings/{}", bookingId);

        bookingsService.deleteBooking(UUID.fromString(bookingId));
        logger.info("Deleted booking with ID {}, returning 204 HTTP status code", bookingId);

        return ResponseEntity.noContent().build();
    }
}