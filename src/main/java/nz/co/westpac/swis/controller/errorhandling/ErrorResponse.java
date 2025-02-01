package nz.co.westpac.swis.controller.errorhandling;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private List<ErrorMessage> errors;

    public ErrorResponse(String singleErrorMessage) {
        this.errors = new ArrayList<>();
        this.errors.add(new ErrorMessage(singleErrorMessage));
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}
