package ec.com.sofka.card.values.objects;

import ec.com.sofka.generics.interfaces.IValueObject;

public class CardNumber implements IValueObject<String> {
    private final String value;

    private CardNumber(final String value) {
        this.value = validate(value);
    }

    public static CardNumber of(final String value) {
        return new CardNumber(value);
    }

    @Override
    public String getValue() {
        return value;
    }

    private String validate(final String value){
        if(value == null){
            throw new IllegalArgumentException("The card number can't be null");
        }

        if(!value.matches("\\d{16}")){
            throw new IllegalArgumentException("The card number must have 16 digits");
        }

        return value;
    }
}