package ec.com.sofka.transaction.values.objects;

import ec.com.sofka.generics.interfaces.IValueObject;

import java.math.BigDecimal;

public class FinalAmount implements IValueObject<BigDecimal> {
    private final BigDecimal value;

    private FinalAmount(final BigDecimal value) {
        this.value = validate(value);
    }

    public static FinalAmount of(final BigDecimal value) {
        return new FinalAmount(value);
    }

    @Override
    public BigDecimal getValue() {
        return this.value;
    }

    private BigDecimal validate(final BigDecimal value) {
        if (value == null) {
            throw new IllegalArgumentException("The final amount can't be null");
        }

        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("The final amount must be greater than 0");
        }

        return value;
    }
}
