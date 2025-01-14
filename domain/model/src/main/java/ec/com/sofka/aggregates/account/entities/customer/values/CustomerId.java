package ec.com.sofka.aggregates.account.entities.customer.values;

import ec.com.sofka.generics.shared.Identity;

public class CustomerId extends Identity {

    public CustomerId() {
        super();
    }

    public CustomerId(final String id) {
        super(id);
    }

    public static CustomerId of(final String id) {
        return new CustomerId(id);
    }

}