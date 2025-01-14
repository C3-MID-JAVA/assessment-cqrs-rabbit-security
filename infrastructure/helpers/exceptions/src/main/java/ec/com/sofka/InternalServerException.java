package ec.com.sofka;

public class InternalServerException extends RuntimeException {
    public InternalServerException(String message) {
        super(message);
    }
}