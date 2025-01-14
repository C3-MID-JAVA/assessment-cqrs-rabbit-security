package ec.com.sofka.card.values.objects;


import ec.com.sofka.account.Account;
import ec.com.sofka.generics.interfaces.IValueObject;

public class AccountValue implements IValueObject<Account> {
    private final Account value;

    private AccountValue(final Account value) {
        this.value = validate(value);
    }

    public static AccountValue of(final Account value) {
        return new AccountValue(value);
    }

    @Override
    public Account getValue() {
        return value;
    }

    private Account validate(final Account value) {
        if (value == null) {
            throw new IllegalArgumentException("The account cannot be null");
        }

        if (value.getBalance() == null || value.getBalance().getValue().compareTo(new java.math.BigDecimal("0.00")) < 0) {
            throw new IllegalArgumentException("The account balance must not be null or negative");
        }

        if (value.getAccountNumber() == null || value.getAccountNumber().getValue().isBlank()) {
            throw new IllegalArgumentException("The account number cannot be null or empty");
        }

        if (value.getOwnerName() == null || value.getOwnerName().getValue().isBlank()) {
            throw new IllegalArgumentException("The owner name cannot be null or empty");
        }

        if (value.getType() == null || value.getType().getValue().isBlank()) {
            throw new IllegalArgumentException("The account type cannot be null or empty");
        }

        return value;
    }
}

