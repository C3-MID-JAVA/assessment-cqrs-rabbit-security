package ec.com.sofka.card.values.objects;

import ec.com.sofka.generics.interfaces.IValueObject;

public class CardHolderName implements IValueObject<String> {
    private final String value;

    private CardHolderName(final String value) {
        this.value = validate(value);
    }

    public static CardHolderName of(final String value) {
        return new CardHolderName(value);
    }

    @Override
    public String getValue() {
        return value;
    }

    private String validate(final String value){
        if(value == null){
            throw new IllegalArgumentException("The card holder name can't be null");
        }

        if(value.isBlank()){
            throw new IllegalArgumentException("The card holder name can't be empty");
        }

        if(value.length() < 3){
            throw new IllegalArgumentException("The card holder name must have at least 3 characters");
        }

        return value;
    }
}