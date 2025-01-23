package ec.com.sofka.appservice.commands;

import ec.com.sofka.enums.TransactionType;
import ec.com.sofka.generics.utils.Command;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CreateTransactionCommand extends Command {
    private final BigDecimal transactionCost;
    private final BigDecimal amount;
    private final LocalDateTime transactionDate;
    private final TransactionType transactionType;
    private final String accountNumber;

    public CreateTransactionCommand(final BigDecimal transactionCost,
                                    final BigDecimal amount,
                                    final LocalDateTime transactionDate,
                                    final TransactionType transactionType,
                                    final String accountNumber
                                    ) {
        super(null);
        this.transactionCost = transactionCost;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.transactionType = transactionType;
        this.accountNumber = accountNumber;
    }

    public BigDecimal getTransactionCost() {
        return transactionCost;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}
