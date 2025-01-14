package ec.com.sofka.aggregate.values;

import ec.com.sofka.generics.utils.Identity;

// Creation of the ID value object: All Identity classes must follow the same structure
public class OperationId extends Identity {
    // To initialize with the UUID
    public OperationId() {
        super();
    }

    // For reconstruction
    private OperationId(final String id) {
        super(id);
    }

    // Method to access/make instances the id with the private modifier
    public static OperationId of(final String id) {
        return new OperationId(id);
    }
}