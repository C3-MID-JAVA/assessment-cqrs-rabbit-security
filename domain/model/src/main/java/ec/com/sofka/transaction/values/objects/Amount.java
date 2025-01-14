package ec.com.sofka.transaction.values.objects;


import ec.com.sofka.generics.interfaces.IValueObject;

import java.math.BigDecimal;

public class Amount implements IValueObject<BigDecimal> {
    private final BigDecimal amount;

    private Amount(final BigDecimal amount) {
        this.amount = validate(amount);
    }

    public static Amount of(final BigDecimal amount) {
        return new Amount(amount);
    }

    @Override
    public BigDecimal getValue() {
        return amount;
    }

    private BigDecimal validate(final BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("The amount must not be null or negative");
        }
        return amount;
    }
}
