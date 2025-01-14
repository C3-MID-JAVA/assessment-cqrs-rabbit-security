package ec.com.sofka.transaction.values.objects;



import ec.com.sofka.generics.interfaces.IValueObject;

public class TransactionType implements IValueObject<String> {
    private final String transactionType;

    private TransactionType(final String transactionType) {
        this.transactionType = validate(transactionType);
    }

    public static TransactionType of(final String transactionType) {
        return new TransactionType(transactionType);
    }

    @Override
    public String getValue() {
        return transactionType;
    }

    private String validate(final String transactionType) {
        if (transactionType == null || transactionType.isBlank()) {
            throw new IllegalArgumentException("The transaction type cannot be null or empty");
        }
        return transactionType;
    }
}

