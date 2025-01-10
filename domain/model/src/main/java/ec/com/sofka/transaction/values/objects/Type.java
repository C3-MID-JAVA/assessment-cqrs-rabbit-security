package ec.com.sofka.transaction.values.objects;

import ec.com.sofka.generics.interfaces.IValueObject;
import ec.com.sofka.transaction.TransactionType;

public class Type implements IValueObject<TransactionType> {
    private final TransactionType value;

    private Type(final TransactionType value) {
        this.value = validate(value);
    }

    public static Type of(final TransactionType value) {
        return new Type(value);
    }

    @Override
    public TransactionType getValue() {
        return this.value;
    }

    private TransactionType validate(final TransactionType value) {
        if (value == null) {
            throw new IllegalArgumentException("The type can't be null");
        }
        return value;
    }
}
