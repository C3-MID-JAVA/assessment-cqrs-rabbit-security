package ec.com.sofka.transaction.values.objects;

import ec.com.sofka.generics.interfaces.IValueObject;

import java.time.LocalDateTime;

public class Timestamp implements IValueObject<LocalDateTime> {
    private final LocalDateTime value;

    private Timestamp(final LocalDateTime value) {
        this.value = validate(value);
    }

    public static Timestamp of(final LocalDateTime value) {
        return new Timestamp(value);
    }

    @Override
    public LocalDateTime getValue() {
        return this.value;
    }

    private LocalDateTime validate(final LocalDateTime value) {
        if (value == null) {
            throw new IllegalArgumentException("The time can't be null");
        }

        return value;
    }
}