package ec.com.sofka.transaction.values.objects;

import ec.com.sofka.generics.interfaces.IValueObject;

import java.math.BigDecimal;

public class NetAmount implements IValueObject<BigDecimal> {
    private final BigDecimal value;

    private NetAmount(final BigDecimal value) {
        this.value = validate(value);
    }

    public static NetAmount of(final BigDecimal value) {
        return new NetAmount(value);
    }

    @Override
    public BigDecimal getValue() {
        return this.value;
    }

    private BigDecimal validate(final BigDecimal value) {
        if (value == null) {
            throw new IllegalArgumentException("The net amount can't be null");
        }

        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("The net amount must be greater than 0");
        }

        return value;
    }
}
