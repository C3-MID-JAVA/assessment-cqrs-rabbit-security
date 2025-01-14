package ec.com.sofka.user.queries.responses;

public class UserResponse {
    private final String id;
    private final String name;
    private final String documentId;
    private String customerId;

    public UserResponse(String id, String name, String documentId) {
        this.id = id;
        this.name = name;
        this.documentId = documentId;
    }

    public UserResponse(String id, String name, String documentId, String customerId) {
        this.id = id;
        this.name = name;
        this.documentId = documentId;
        this.customerId = customerId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDocumentId() {
        return documentId;
    }

    public String getCustomerId() {
        return customerId;
    }
}
