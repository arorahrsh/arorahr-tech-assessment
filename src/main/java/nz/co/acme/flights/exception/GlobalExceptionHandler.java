package nz.co.acme.flights.exception;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import nz.co.acme.flights.model.ErrorResponse;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;

@AllArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger;

    private final Gson gson;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult().getFieldErrors().stream()
                .map(f -> new ErrorResponse.ErrorMessage(f.getDefaultMessage()))
                .toList();
        ErrorResponse response = new ErrorResponse(errors);

        logger.warn("Returning 400 HTTP status code and JSON response: {}", gson.toJson(response));

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