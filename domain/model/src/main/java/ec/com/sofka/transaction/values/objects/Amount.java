package ec.com.sofka.transaction.values.objects;

import ec.com.sofka.generics.interfaces.IValueObject;

import java.math.BigDecimal;

public class Amount implements IValueObject<BigDecimal> {

    private final BigDecimal value;

    public Amount(BigDecimal value) {
        this.value = validate(value);
    }

    public static Amount of(final BigDecimal value) {
        return new Amount(value);
    }

    @Override
    public BigDecimal getValue() {
        return value;
    }

    private BigDecimal validate(final BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        return value;
    }
}
