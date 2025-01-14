package ec.com.sofka.user.values.objects;

import ec.com.sofka.generics.interfaces.IValueObject;

public class Name implements IValueObject<String> {
    private final String value;

    public Name(String value) {
        this.value = validate(value);
    }

    public static Name of(final String value) {
        return new Name(value);
    }

    @Override
    public String getValue() {
        return this.value;
    }

    private String validate(final String value) {
        if (value == null) {
            throw new IllegalArgumentException("The name can't be null");
        }

        if (value.isBlank()) {
            throw new IllegalArgumentException("The name can't be empty");
        }

        return value;
    }
}