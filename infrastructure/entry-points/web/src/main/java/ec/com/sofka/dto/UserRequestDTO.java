package ec.com.sofka.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRequestDTO {

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 3, max = 50)
    private String name;

    @NotBlank(message = "Document ID cannot be blank")
    @Size(min = 3, max = 15)
    private String documentId;

    public UserRequestDTO() {
    }

    public UserRequestDTO(String name, String documentId) {
        this.name = name;
        this.documentId = documentId;
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
