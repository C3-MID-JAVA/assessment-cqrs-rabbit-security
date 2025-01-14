package ec.com.sofka.transaction.values.objects;

import ec.com.sofka.generics.interfaces.IValueObject;

public class Description implements IValueObject<String> {
    private final String value;

    private Description(final String value) {
        this.value = validate(value);
    }

    public static Description of(final String value) {
        return new Description(value);
    }

    @Override
    public String getValue() {
        return value;
    }

    private String validate(final String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("The description cannot be null or empty");
        }
        if (value.length() > 255) {
            throw new IllegalArgumentException("The description must not exceed 255 characters");
        }
        return value;
    }
}
