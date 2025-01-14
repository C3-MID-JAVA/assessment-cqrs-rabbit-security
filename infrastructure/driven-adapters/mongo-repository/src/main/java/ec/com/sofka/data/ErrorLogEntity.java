package ec.com.sofka.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "error_logs")
public class ErrorLogEntity {
    @Id
    String id;

    @Field("error")
    String error;

    @Field("proccess")
    String proccess;

    @Field("timestamp")
    String timestamp;

    public ErrorLogEntity(String error, String proccess, String timestamp) {
        this.error = error;
        this.proccess = proccess;
        this.timestamp = timestamp;
    }

    public ErrorLogEntity() {
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getProccess() {
        return proccess;
    }

    public void setProccess(String proccess) {
        this.proccess = proccess;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
