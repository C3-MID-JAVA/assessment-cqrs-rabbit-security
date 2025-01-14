package ec.com.sofka.transaction.values.objects;


import ec.com.sofka.generics.interfaces.IValueObject;

import java.time.LocalDateTime;

public class Timestamp implements IValueObject<LocalDateTime> {
    private final LocalDateTime timestamp;

    private Timestamp(final LocalDateTime timestamp) {
        this.timestamp = validate(timestamp);
    }

    public static Timestamp of(final LocalDateTime timestamp) {
        return new Timestamp(timestamp);
    }

    @Override
    public LocalDateTime getValue() {
        return timestamp;
    }

    private LocalDateTime validate(final LocalDateTime timestamp) {
        if (timestamp == null) {
            throw new IllegalArgumentException("The timestamp cannot be null");
        }
        return timestamp;
    }
}
