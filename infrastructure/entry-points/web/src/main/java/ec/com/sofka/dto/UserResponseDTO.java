package ec.com.sofka.dto;

public class UserResponseDTO {
    private String id;
    private String name;
    private String documentId;
    private String customerId;

    public UserResponseDTO(String id, String name, String documentId) {
        this.id = id;
        this.name = name;
        this.documentId = documentId;
    }

    public UserResponseDTO(String id, String name, String documentId, String customerId) {
        this.id = id;
        this.name = name;
        this.documentId = documentId;
        this.customerId = customerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = UserResponseDTO.this.id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
