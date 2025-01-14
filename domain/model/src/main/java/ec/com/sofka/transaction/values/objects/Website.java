package ec.com.sofka.transaction.values.objects;

import ec.com.sofka.generics.interfaces.IValueObject;

public class Website implements IValueObject<String> {
    private final String value;

    private Website(final String value) {
        this.value = validate(value);
    }

    public static Website of(final String value) {
        return new Website(value);
    }

    @Override
    public String getValue() {
        return value;
    }

    private String validate(final String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Website cannot be null or empty");
        }
        return value;
    }
}