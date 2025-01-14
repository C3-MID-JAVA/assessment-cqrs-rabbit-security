package ec.com.sofka.card.values.objects;

import ec.com.sofka.generics.interfaces.IValueObject;

public class CardExpiryDate implements IValueObject<String> {
    private final String value;

    private CardExpiryDate(final String value) {
        this.value = validate(value);
    }

    public static CardExpiryDate of(final String value) {
        return new CardExpiryDate(value);
    }

    @Override
    public String getValue() {
        return value;
    }

    private String validate(final String value){
        if(value == null){
            throw new IllegalArgumentException("The card expiry date can't be null");
        }

        if(!value.matches("(0[1-9]|1[0-2])/\\d{2}")){
            throw new IllegalArgumentException("The card expiry date must be in MM/YY format");
        }

        return value;
    }
}
