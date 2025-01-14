package ec.com.sofka.user.values.objects;

import ec.com.sofka.generics.interfaces.IValueObject;

public class DocumentId implements IValueObject<String> {
    private final String value;

    public DocumentId(String value) {
        this.value = validate(value);
    }

    public static DocumentId of(final String value) {
        return new DocumentId(value);
    }

    @Override
    public String getValue() {
        return this.value;
    }

    private String validate(final String value) {
        if (value == null) {
            throw new IllegalArgumentException("The document can't be null");
        }

        if (value.isBlank()) {
            throw new IllegalArgumentException("The document can't be empty");
        }

        return value;
    }
}
