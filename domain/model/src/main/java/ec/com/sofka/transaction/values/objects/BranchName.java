package ec.com.sofka.transaction.values.objects;

import ec.com.sofka.generics.interfaces.IValueObject;

public class BranchName implements IValueObject<String> {
    private final String value;

    private BranchName(final String value) {
        this.value = validate(value);
    }

    public static BranchName of(final String value) {
        return new BranchName(value);
    }

    @Override
    public String getValue() {
        return value;
    }

    private String validate(final String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Branch name cannot be null or empty");
        }
        if (value.length() < 3) {
            throw new IllegalArgumentException("Branch name must have at least 3 characters");
        }
        return value;
    }
}