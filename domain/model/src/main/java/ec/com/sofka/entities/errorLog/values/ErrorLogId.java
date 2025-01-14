package ec.com.sofka.entities.errorLog.values;

import ec.com.sofka.generics.shared.Identity;

public class ErrorLogId extends Identity {

    public ErrorLogId() {
        super();
    }

    public ErrorLogId(final String value) {
        super(value);
    }

    public static ErrorLogId of(final String value) {
        return new ErrorLogId(value);
    }

}