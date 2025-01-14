package ec.com.sofka.transaction.values.objects;

import ec.com.sofka.generics.interfaces.IValueObject;

public class MarketName implements IValueObject<String> {
    private final String value;

    private MarketName(final String value) {
        this.value = validate(value);
    }

    public static MarketName of(final String value) {
        return new MarketName(value);
    }

    @Override
    public String getValue() {
        return value;
    }

    private String validate(final String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Market name cannot be null or empty");
        }
        if (value.length() < 3) {
            throw new IllegalArgumentException("Market name must have at least 3 characters");
        }
        return value;
    }
}