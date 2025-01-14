package ec.com.sofka.exceptions;

public class TransactionRejectedException  extends RuntimeException {
    public TransactionRejectedException(String message) {
        super(message);
    }
}