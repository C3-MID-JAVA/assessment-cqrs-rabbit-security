package ec.com.sofka.aggregates.auth.entities.user.values.objects;

import ec.com.sofka.exceptions.InvalidFieldException;
import ec.com.sofka.generics.interfaces.IValueObject;

public record Password(String value) implements IValueObject<String> {

    private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).+$";

    public Password(final String value) {
        this.value = isValid(value);
    }

    public static Password of(final String value) {
        return new Password(value);
    }

    private String isValid(final String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidFieldException("Password cannot be null or blank.");
        }
        if (!value.matches(PASSWORD_REGEX)) {
            throw new InvalidFieldException("The password is not secure.");
        }
        return value;
    }

    @Override
    public String getValue() {
        return this.value;
    }

}