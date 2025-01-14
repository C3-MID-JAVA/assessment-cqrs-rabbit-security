package ec.com.sofka.account.values.objects;

import ec.com.sofka.generics.interfaces.IValueObject;

public class OwnerName implements IValueObject<String> {
    private String value;

    private OwnerName(final String value) {
        this.value = validate(value);
    }

    public OwnerName() {
    }

    public static OwnerName of(final String value) {
        return new OwnerName(value);
    }

    @Override
    public String getValue() {
        return value;
    }

    private String validate(final String value){
        if(value == null){
            throw new IllegalArgumentException("The personal data can't be null");
        }

        if(value.isBlank()){
            throw new IllegalArgumentException("The personal data can't be empty");
        }

        if(value.length() < 3){
            throw new IllegalArgumentException("The personal data must have at least 3 characters");
        }

        return value;
    }
}
