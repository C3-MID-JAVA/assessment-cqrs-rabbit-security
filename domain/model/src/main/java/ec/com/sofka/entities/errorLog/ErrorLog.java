package ec.com.sofka.entities.errorLog;

import ec.com.sofka.entities.errorLog.values.ErrorLogId;
import ec.com.sofka.generics.shared.Entity;

public class ErrorLog extends Entity<ErrorLogId> {

    String error;
    String process;
    String timestamp;

    public ErrorLog(ErrorLogId id, String error, String process, String timestamp) {
        super(id);
        this.error = error;
        this.process = process;
        this.timestamp = timestamp;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
