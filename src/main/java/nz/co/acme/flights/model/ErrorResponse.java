package nz.co.acme.flights.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private List<ErrorMessage> errors;

    public static ErrorResponse from(Exception ex) {
        return new ErrorResponse(List.of(new ErrorMessage(ex.getMessage())));
    }

    public static ErrorResponse from(String msg) {
        return new ErrorResponse(List.of(new ErrorMessage(msg)));
    }

    @Data @AllArgsConstructor
    public static class ErrorMessage {
        private String message;
    }
}