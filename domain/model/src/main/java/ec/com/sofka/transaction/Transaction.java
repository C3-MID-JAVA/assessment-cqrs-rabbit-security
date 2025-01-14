package ec.com.sofka.transaction;

import ec.com.sofka.account.values.AccountId;
import ec.com.sofka.enums.TransactionType;
import ec.com.sofka.generics.utils.Entity;
import ec.com.sofka.transaction.values.TransactionId;
import ec.com.sofka.transaction.values.objects.Amount;
import ec.com.sofka.transaction.values.objects.TransactionCost;
import ec.com.sofka.transaction.values.objects.TransactionDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction extends Entity<TransactionId> {

    private final Amount amount;
    private final TransactionCost transactionCost;
    private final TransactionDate date;
    private final TransactionType type; // Usando el enum directamente
    private final AccountId accountId;

    public Transaction(TransactionId id,Amount amount, TransactionCost transactionCost, TransactionDate date, TransactionType type, AccountId accountId) {
        super(id);
        this.amount = amount;
        this.transactionCost = transactionCost;
        this.date = date;
        this.type = type;
        this.accountId = accountId;
    }

    public Amount getAmount() {
        return amount;
    }

    public TransactionCost getTransactionCost() {
        return transactionCost;
    }

    public TransactionDate getDate() {
        return date;
    }

    public TransactionType getType() {
        return type;
    }

    public AccountId getAccountId() {
        return accountId;
    }
}
