package ec.com.sofka.aggregates.account.entities.transactionType.values;

import ec.com.sofka.generics.shared.Identity;

public class TransactionTypeId extends Identity {

    public TransactionTypeId() {
        super();
    }

    public TransactionTypeId(final String id) {
        super(id);
    }

    public static TransactionTypeId of(final String id) {
        return new TransactionTypeId(id);
    }
}