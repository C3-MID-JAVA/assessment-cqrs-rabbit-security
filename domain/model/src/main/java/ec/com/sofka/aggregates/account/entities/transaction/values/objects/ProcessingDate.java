package ec.com.sofka.aggregates.account.entities.transaction.values.objects;

import ec.com.sofka.generics.interfaces.IValueObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record ProcessingDate(String value) implements IValueObject<String> {

    public ProcessingDate(String value) {
        this.value = validate(value);
    }

    public static ProcessingDate of(String value) {
        return new ProcessingDate(value);
    }

    private String validate(final String value) {
        if (value == null || value.isEmpty()) {
            return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        }
        return value;
    }

    @Override
    public String getValue() {
        return this.value;
    }

}