package ec.com.sofka.account.values.objects;

import ec.com.sofka.exceptions.IlegalArgumentExcep;
import ec.com.sofka.generics.interfaces.IValueObject;

import java.math.BigDecimal;


public class Balance implements IValueObject<BigDecimal> {
    private  BigDecimal value;

    private Balance(final BigDecimal value) {
        this.value = validate(value);
    }

    public static Balance of(final BigDecimal value) {
        return new Balance(value);
    }

    public Balance() {
    }

    @Override
    public BigDecimal getValue() {
        return this.value;
    }

    private BigDecimal validate(final BigDecimal value){
        if(value == null){
            throw new IllegalArgumentException("The balance can't be null");
        }

        if(value.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("The balance must be greater than 0");
        }


        return value;
    }
}
