package nz.co.acme.flights.exception;

public class BusinessRuleException extends RuntimeException {
    public BusinessRuleException(String msg) {
        super(msg);
    }
}
