package ec.com.sofka.card.values.objects;

import ec.com.sofka.generics.interfaces.IValueObject;

public class CardType implements IValueObject<String> {
    private final String value;

    private CardType(final String value) {
        this.value = validate(value);
    }

    public static CardType of(final String value) {
        return new CardType(value);
    }

    @Override
    public String getValue() {
        return value;
    }

    private String validate(final String value){
        if(value == null){
            throw new IllegalArgumentException("The card type can't be null");
        }

        if(value.isBlank()){
            throw new IllegalArgumentException("The card type can't be empty");
        }

        return value;
    }
}