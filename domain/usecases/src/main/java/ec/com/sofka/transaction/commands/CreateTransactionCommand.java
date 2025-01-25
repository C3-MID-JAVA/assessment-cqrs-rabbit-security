package ec.com.sofka.transaction.commands;

import ec.com.sofka.generics.utils.Command;
import ec.com.sofka.transaction.TransactionType;

import java.math.BigDecimal;

public class CreateTransactionCommand extends Command {
    private final BigDecimal amount;
    private final TransactionType type;
    private final String accountNumber;

    public CreateTransactionCommand(BigDecimal amount, TransactionType type, String accountNumber) {
        super(null);
        this.amount = amount;
        this.type = type;
        this.accountNumber = accountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}
