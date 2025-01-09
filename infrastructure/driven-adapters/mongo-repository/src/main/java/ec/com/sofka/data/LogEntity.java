package ec.com.sofka.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "logs")
public class LogEntity {

    @Id
    private String id;

    private String message;

    private String entity;

    private LocalDateTime timestamp;

    public LogEntity(String message, String entity, LocalDateTime timestamp) {
        this.message = message;
        this.entity = entity;
        this.timestamp = timestamp;
    }

    public LogEntity(String id, String message, String entity, LocalDateTime timestamp) {
        this.id = id;
        this.message = message;
        this.entity = entity;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }
}
