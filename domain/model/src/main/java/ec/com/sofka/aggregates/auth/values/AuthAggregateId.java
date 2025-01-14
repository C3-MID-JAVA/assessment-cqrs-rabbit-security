package ec.com.sofka.aggregates.auth.values;

import ec.com.sofka.generics.shared.Identity;

public class AuthAggregateId extends Identity {

    public AuthAggregateId() {
        super();
    }

    public AuthAggregateId(String id) {
        super(id);
    }

    public static AuthAggregateId of(String id) {
        return new AuthAggregateId(id);
    }

}