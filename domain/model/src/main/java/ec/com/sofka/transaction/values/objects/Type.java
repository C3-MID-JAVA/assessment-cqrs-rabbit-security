package ec.com.sofka.transaction.values.objects;

import ec.com.sofka.generics.interfaces.IValueObject;

public class Type implements IValueObject<String> {
    private final String value;

    private Type(final String value) {
        this.value = validate(value);
    }

    public static Type of(final String value) {
        return new Type(value);
    }

    @Override
    public String getValue() {
        return value;
    }

    private String validate(final String value) {
        if (value == null) {
            throw new IllegalArgumentException("The type can't be null");
        }

        if (value.isBlank()) {
            throw new IllegalArgumentException("The type can't be empty");
        }

        if (value.length() >= 50) {
            throw new IllegalArgumentException("The type don't have more 50 characters");
        }

        return value;
    }
}