package ec.com.sofka.exceptions;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }

    // Constructor que recibe un mensaje de error y una causa
    public ConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}