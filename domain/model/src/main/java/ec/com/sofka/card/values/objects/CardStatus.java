package ec.com.sofka.card.values.objects;

import ec.com.sofka.generics.interfaces.IValueObject;

public class CardStatus implements IValueObject<String> {
    private final String value;

    private CardStatus(final String value) {
        this.value = validate(value);
    }

    public static CardStatus of(final String value) {
        return new CardStatus(value);
    }

    @Override
    public String getValue() {
        return value;
    }

    private String validate(final String value){
        if(value == null){
            throw new IllegalArgumentException("The card status can't be null");
        }

        if(value.isBlank()){
            throw new IllegalArgumentException("The card status can't be empty");
        }

        return value;
    }
}
