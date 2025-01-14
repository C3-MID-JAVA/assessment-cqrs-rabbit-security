package ec.com.sofka.aggregate.operation.values;

import ec.com.sofka.generics.utils.Identity;

public class OperationId extends Identity {
    public OperationId() {
        super();
    }

    private OperationId(final String id) {
        super(id);
    }

    public static OperationId of(final String id) {
        return new OperationId(id);
    }
}