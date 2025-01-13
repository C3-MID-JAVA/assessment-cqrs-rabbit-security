package ec.com.sofka;

public class AccessDeniedException extends RuntimeException{
    public AccessDeniedException(String message) {
        super(message);
    }

    // Constructor que recibe un mensaje de error y una causa
    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
}
