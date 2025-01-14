package ec.com.sofka.card.values.objects;

import ec.com.sofka.generics.interfaces.IValueObject;

public class CardCVV implements IValueObject<String> {
    private final String value;

    private CardCVV(final String value) {
        this.value = validate(value);
    }

    public static CardCVV of(final String value) {
        return new CardCVV(value);
    }

    @Override
    public String getValue() {
        return value;
    }

    private String validate(final String value){
        if(value == null){
            throw new IllegalArgumentException("The card CVV can't be null");
        }

        if(!value.matches("\\d{4}")){
            throw new IllegalArgumentException("The card CVV must be a 4-digit number");
        }

        return value;
    }
}