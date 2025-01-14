package ec.com.sofka.transaction.values.objects;

import ec.com.sofka.generics.interfaces.IValueObject;

public class IdAccount implements IValueObject<String> {
    private final String value;

    private IdAccount(final String value) {
        this.value = validate(value);
    }

    public static IdAccount of(final String value) {
        return new IdAccount(value);
    }

    @Override
    public String getValue() {
        return value;
    }

    private String validate(final String value) {
        if (value == null) {
            throw new IllegalArgumentException("The account ID can't be null");
        }

        if (value.isBlank()) {
            throw new IllegalArgumentException("The account ID can't be empty");
        }

        if (value.length() < 5) {
            throw new IllegalArgumentException("The account ID must have at least 5 characters");
        }

        return value;
    }
}