package ec.com.sofka.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "users")
public class UserEntity {

    @Id
    private String id;

    private String name;

    private String documentId;

    public UserEntity() {
    }

    public UserEntity(String name, String documentId) {
        this.name = name;
        this.documentId = documentId;
    }

    public UserEntity(String id, String name, String documentId) {
        this.id = id;
        this.name = name;
        this.documentId = documentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
