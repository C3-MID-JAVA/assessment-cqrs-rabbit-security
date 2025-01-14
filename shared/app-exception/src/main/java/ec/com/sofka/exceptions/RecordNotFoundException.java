package ec.com.sofka.exceptions;

public class RecordNotFoundException  extends RuntimeException {
    public RecordNotFoundException(String message) {
        super(message);
    }
}