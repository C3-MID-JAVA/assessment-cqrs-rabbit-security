package ec.com.sofka.transaction.values.objects;



import ec.com.sofka.generics.interfaces.IValueObject;

public class AtmName implements IValueObject<String> {
    private final String value;

    private AtmName(final String value) {
        this.value = validate(value);
    }

    public static AtmName of(final String value) {
        return new AtmName(value);
    }

    @Override
    public String getValue() {
        return value;
    }

    private String validate(final String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ATM name cannot be null or empty");
        }
        if (value.length() < 3) {
            throw new IllegalArgumentException("ATM name must have at least 3 characters");
        }
        return value;
    }
}
