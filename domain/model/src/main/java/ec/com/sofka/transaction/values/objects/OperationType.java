package ec.com.sofka.transaction.values.objects;



import ec.com.sofka.generics.interfaces.IValueObject;

public class OperationType implements IValueObject<String> {
    private final String value;

    private OperationType(final String value) {
        this.value = validate(value);
    }

    public static OperationType of(final String value) {
        return new OperationType(value);
    }

    @Override
    public String getValue() {
        return value;
    }

    private String validate(final String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Operation type cannot be null or empty");
        }
        if (value.length() < 2) {
            throw new IllegalArgumentException("Operation type must have at least 2 characters");
        }
        return value;
    }
}

