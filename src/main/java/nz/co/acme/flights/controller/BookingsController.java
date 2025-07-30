package nz.co.acme.flights.controller;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import nz.co.acme.flights.model.Booking;
import nz.co.acme.flights.model.BookingRequest;
import nz.co.acme.flights.model.BookingResponse;
import nz.co.acme.flights.model.Passenger;
import nz.co.acme.flights.service.BookingsService;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/v1/bookings")
public class BookingsController {

    private final BookingsService bookingsService;

    private final Logger logger;

    private final Gson gson;

    @PostMapping
    public ResponseEntity<BookingResponse> getFlights(@RequestBody BookingRequest request) {
        logger.info("Received POST /v1/bookings with request: {}", gson.toJson(request));

        Booking booking = bookingsService.createBooking(request.getFlightId(), Passenger.from(request));

        logger.info("Created booking successfully [flightId: {}, bookingId: {}]", request.getFlightId(), booking.getBookingId());
        BookingResponse result = new BookingResponse(booking.getBookingId(), booking.getStatus());

        logger.info("Returning 201 HTTP status code and JSON response payload: {}", gson.toJson(result));
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}