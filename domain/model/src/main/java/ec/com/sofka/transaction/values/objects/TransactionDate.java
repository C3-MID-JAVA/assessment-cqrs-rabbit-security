package ec.com.sofka.transaction.values.objects;

import ec.com.sofka.generics.interfaces.IValueObject;

import java.time.LocalDateTime;

public class TransactionDate implements IValueObject<LocalDateTime> {

    private final LocalDateTime value;

    private TransactionDate(final LocalDateTime value) {
        this.value = validate(value);
    }

    public static TransactionDate of(final LocalDateTime value) {
        return new TransactionDate(value);
    }

    @Override
    public LocalDateTime getValue() {
        return value;
    }

    private LocalDateTime validate(final LocalDateTime value) {
        if (value == null) {
            throw new IllegalArgumentException("Transaction date cannot be null");
        }
        return value;
    }
}
