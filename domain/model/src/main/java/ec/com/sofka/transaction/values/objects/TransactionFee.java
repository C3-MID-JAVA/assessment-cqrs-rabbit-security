package ec.com.sofka.transaction.values.objects;


import ec.com.sofka.generics.interfaces.IValueObject;

import java.math.BigDecimal;

public class TransactionFee implements IValueObject<BigDecimal> {
    private final BigDecimal transactionFee;

    private TransactionFee(final BigDecimal transactionFee) {
        this.transactionFee = validate(transactionFee);
    }

    public static TransactionFee of(final BigDecimal transactionFee) {
        return new TransactionFee(transactionFee);
    }

    @Override
    public BigDecimal getValue() {
        return transactionFee;
    }

    private BigDecimal validate(final BigDecimal transactionFee) {
        if (transactionFee == null || transactionFee.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("The transaction fee must not be null or negative");
        }
        return transactionFee;
    }
}
