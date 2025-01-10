package ec.com.sofka.user.request;

import ec.com.sofka.generics.utils.Request;

public class CreateUserRequest extends Request {
    private final String name;
    private final String documentId;

    public CreateUserRequest(String name, String documentId) {
        super(null);
        this.name = name;
        this.documentId = documentId;
    }

    public String getName() {
        return name;
    }

    public String getDocumentId() {
        return documentId;
    }
}
