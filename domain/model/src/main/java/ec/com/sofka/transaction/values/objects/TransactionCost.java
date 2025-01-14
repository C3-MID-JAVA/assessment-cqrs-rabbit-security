package ec.com.sofka.transaction.values.objects;

import ec.com.sofka.generics.interfaces.IValueObject;

import java.math.BigDecimal;

public class TransactionCost implements IValueObject<BigDecimal> {

    private final BigDecimal value;

    private TransactionCost(final BigDecimal value) {
        this.value = validate(value);
    }

    public static TransactionCost of(final BigDecimal value) {
        return new TransactionCost(value);
    }

    @Override
    public BigDecimal getValue() {
        return value;
    }

    private BigDecimal validate(final BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Transaction cost cannot be negative");
        }
        return value;
    }
}
