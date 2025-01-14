package ec.com.sofka.transaction.command;

import ec.com.sofka.generics.utils.Command;
import ec.com.sofka.transaction.TransactionType;

import java.math.BigDecimal;

public class CreateTransactionCommand extends Command {
    private final BigDecimal amount;
    private final TransactionType type;
    private final String customerId;
    private final String accountNumber;

    public CreateTransactionCommand(BigDecimal amount, TransactionType type, String accountNumber, String customerId) {
        super(null);
        this.amount = amount;
        this.type = type;
        this.customerId = customerId;
        this.accountNumber = accountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}
