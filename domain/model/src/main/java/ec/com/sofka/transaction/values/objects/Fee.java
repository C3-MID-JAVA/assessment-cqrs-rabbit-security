package ec.com.sofka.transaction.values.objects;

import ec.com.sofka.generics.interfaces.IValueObject;

import java.math.BigDecimal;

public class Fee  implements IValueObject<BigDecimal> {
    private final BigDecimal value;

    private Fee(final BigDecimal value) {
        this.value = validate(value);
    }

    public static Fee of(final BigDecimal value) {
        return new Fee(value);
    }

    @Override
    public BigDecimal getValue() {
        return this.value;
    }

    private BigDecimal validate(final BigDecimal value) {
        if (value == null) {
            throw new IllegalArgumentException("The fee can't be null");
        }

        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("The fee must be greater than 0");
        }

        return value;
    }
}