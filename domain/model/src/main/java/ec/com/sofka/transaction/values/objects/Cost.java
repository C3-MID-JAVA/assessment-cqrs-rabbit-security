package ec.com.sofka.transaction.values.objects;

import ec.com.sofka.generics.interfaces.IValueObject;

import java.math.BigDecimal;

public class Cost implements IValueObject<BigDecimal> {
    private final BigDecimal value;

    private Cost(final BigDecimal value) {
        this.value = validate(value);
    }

    public static Cost of(final BigDecimal value) {
        return new Cost(value);
    }

    @Override
    public BigDecimal getValue() {
        return this.value;
    }

    private BigDecimal validate(final BigDecimal value) {
        if (value == null) {
            throw new IllegalArgumentException("The cost can't be null");
        }
        return value;
    }
}