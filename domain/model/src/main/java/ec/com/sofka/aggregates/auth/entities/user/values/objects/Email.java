package ec.com.sofka.aggregates.auth.entities.user.values.objects;

import ec.com.sofka.exceptions.InvalidFieldException;
import ec.com.sofka.generics.interfaces.IValueObject;

import java.util.regex.Pattern;

public record Email(java.lang.String value) implements IValueObject<java.lang.String> {

    private static final java.lang.String STRING_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(STRING_REGEX);

    public Email(final java.lang.String value) {
        this.value = isValid(value);
    }

    public static Email of(final java.lang.String value) {
        return new Email(value);
    }

    private java.lang.String isValid(final java.lang.String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidFieldException("Email cannot be null or blank.");
        }
        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new InvalidFieldException("Invalid email format.");
        }
        return value;
    }

    @Override
    public java.lang.String getValue() {
        return this.value;
    }
}
