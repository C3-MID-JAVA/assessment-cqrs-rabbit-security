package ec.com.sofka.card.values.objects;

import ec.com.sofka.generics.interfaces.IValueObject;

public class CardName implements IValueObject<String> {
    private final String value;

    private CardName(final String value) {
        this.value = validate(value);
    }

    public static CardName of(final String value) {
        return new CardName(value);
    }

    @Override
    public String getValue() {
        return value;
    }

    private String validate(final String value){
        if(value == null){
            throw new IllegalArgumentException("The card name can't be null");
        }

        if(value.isBlank()){
            throw new IllegalArgumentException("The card name can't be empty");
        }

        return value;
    }
}
