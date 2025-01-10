package ec.com.sofka.dto;

public class UserResponseDTO {
    private String customerId;
    private String name;
    private String documentId;

    public UserResponseDTO(String customerId, String name, String documentId) {
        this.customerId = customerId;
        this.name = name;
        this.documentId = documentId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = UserResponseDTO.this.customerId;
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
}
