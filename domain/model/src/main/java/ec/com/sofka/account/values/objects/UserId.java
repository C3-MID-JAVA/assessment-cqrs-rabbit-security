package ec.com.sofka.account.values.objects;

import ec.com.sofka.generics.interfaces.IValueObject;

public class UserId implements IValueObject<String> {
    private final String value;

    private UserId(final String value) {
        this.value = validate(value);
    }

    public static UserId of(final String value) {
        return new UserId(value);
    }

    @Override
    public String getValue() {
        return value;
    }

    private String validate(final String value){
        if(value == null){
            throw new IllegalArgumentException("The user ID can't be null");
        }

        if(value.isBlank()){
            throw new IllegalArgumentException("The user ID can't be empty");
        }

        if(value.length() < 5){
            throw new IllegalArgumentException("The user ID must have at least 5 characters");
        }

        //if (!value.matches("[a-zA-Z0-9]+")) {
         //   throw new IllegalArgumentException("The user ID must be alphanumeric");
       // }

        return value;
    }
}