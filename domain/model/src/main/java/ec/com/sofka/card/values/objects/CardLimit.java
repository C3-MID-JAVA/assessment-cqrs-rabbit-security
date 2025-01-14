package ec.com.sofka.card.values.objects;

import ec.com.sofka.generics.interfaces.IValueObject;

import java.math.BigDecimal;

public class CardLimit implements IValueObject<BigDecimal> {
    private final BigDecimal value;

    private CardLimit(final BigDecimal value) {
        this.value = validate(value);
    }

    public static CardLimit of(final BigDecimal value) {
        return new CardLimit(value);
    }

    @Override
    public BigDecimal getValue() {
        return value;
    }

    private BigDecimal validate(final BigDecimal value){
        if(value == null){
            throw new IllegalArgumentException("The card limit can't be null");
        }

        if(value.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("The card limit must be greater than zero");
        }

        return value;
    }
}
