package ec.com.sofka.exceptions;

import java.util.List;

public class RequestValidationException extends RuntimeException {
    private final List<String> errors;

    public RequestValidationException(List<String> errors) {
        super("Validation failed");
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }

}