package ec.com.sofka.account.values.objects;

import ec.com.sofka.ConflictException;
import ec.com.sofka.generics.interfaces.IValueObject;

public class Owner implements IValueObject<String> {
    private final String value;

    private Owner(final String value) {
        this.value = validate(value);
    }

    public static Owner of(final String value) {
        return new Owner(value);
    }

    @Override
    public String getValue() {
        return value;
    }

    private String validate(final String value){
        if(value == null){
            throw new ConflictException("The personal data can't be null");
        }

        if(value.isBlank()){
            throw new ConflictException("The personal data can't be empty");
        }

        if(value.length() < 3){
            throw new ConflictException("The personal data must have at least 3 characters");
        }

        return value;
    }
}