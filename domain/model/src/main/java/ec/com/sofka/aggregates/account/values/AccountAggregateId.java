package ec.com.sofka.aggregates.account.values;

import ec.com.sofka.generics.shared.Identity;

public class AccountAggregateId extends Identity {

    public AccountAggregateId() {
        super();
    }

    public AccountAggregateId(final String value) {
        super(value);
    }

    public static AccountAggregateId of(final String value) {
        return new AccountAggregateId(value);
    }

}