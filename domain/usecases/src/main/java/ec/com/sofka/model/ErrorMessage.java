package ec.com.sofka.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ErrorMessage {
    private String error;
    private String process;
    private String date;

    public ErrorMessage(String error, String process) {
        this.error = error;
        this.process = process;
        this.date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}