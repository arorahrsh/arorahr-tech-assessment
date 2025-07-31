package nz.co.acme.flights.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import nz.co.acme.flights.model.ErrorResponse;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;

@AllArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger;

    private final Gson gson;

    /**
     * Handle validation annotations (@NotBlank, @Email, etc.)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult().getFieldErrors().stream()
                .map(f -> new ErrorResponse.ErrorMessage("The field '" + f.getField() + "' is invalid or missing"))
                .toList();
        ErrorResponse response = new ErrorResponse(errors);

        logger.warn("Returning 400 HTTP status code and JSON response: {}", gson.toJson(response));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle illegal arguments e.g. invalid path parameter for DELETE /bookings/{bookingId}
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        String message = ex.getMessage();

        if (message != null && message.startsWith("Invalid UUID string")) {
            ErrorResponse response = ErrorResponse.from("Invalid value for bookingId: must be a valid UUID");

            logger.warn("Returning 400 HTTP status code and JSON response: {}", gson.toJson(response));
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        ErrorResponse response = ErrorResponse.from("Illegal argument exception: " + ex.getMessage());
        logger.warn("Returning 400 HTTP status code and JSON response: {}", gson.toJson(response));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle invalid enum type in JSON (e.g. origin = "WL"). Note: only handles first error.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMalformedInput(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();
        // TODO: build list of all validation errors instead of just the first one
        // for example, if both origin and destination are invalid enums, then return both error messages
        if (cause instanceof InvalidFormatException formatEx) {
            String fieldName = formatEx.getPath().stream()
                    .map(JsonMappingException.Reference::getFieldName)
                    .findFirst()
                    .orElse("unknown");

            String message = "The field '" +  fieldName + "' is invalid or missing";
            ErrorResponse response = ErrorResponse.from(message);

            logger.warn("Returning 400 HTTP status and JSON response: {}", gson.toJson(response));
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        ErrorResponse response = ErrorResponse.from("Request body is invalid or missing");
        logger.warn("Returning 400 HTTP status code due to malformed request: {}", gson.toJson(response));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ErrorResponse> handleBusinessRule(BusinessRuleException ex) {
        ErrorResponse response = ErrorResponse.from(ex);

        logger.warn("Returning 422 HTTP status code and JSON response: {}", gson.toJson(response));
        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex) {
        ErrorResponse response = ErrorResponse.from(ex);

        logger.warn("Returning 404 HTTP status code and JSON response: {}", gson.toJson(response));
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOther(Exception ex) {
        ErrorResponse response = ErrorResponse.from("Internal server error");

        logger.warn("Returning 500 HTTP status code and JSON response: {}", gson.toJson(response), ex);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}