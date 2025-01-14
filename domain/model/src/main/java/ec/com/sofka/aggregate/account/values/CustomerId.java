package ec.com.sofka.aggregate.account.values;

import ec.com.sofka.generics.utils.Identity;


public class CustomerId extends Identity {
    public CustomerId() {
        super();
    }

    private CustomerId(final String id) {
        super(id);
    }

    public static CustomerId of(final String id) {
        return new CustomerId(id);
    }
}
