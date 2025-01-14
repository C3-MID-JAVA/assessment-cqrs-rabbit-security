package ec.com.sofka.aggregates.account.entities.transaction;

import ec.com.sofka.aggregates.account.values.objects.Amount;
import ec.com.sofka.generics.shared.Entity;
import ec.com.sofka.aggregates.account.entities.account.Account;
import ec.com.sofka.aggregates.account.entities.transaction.values.TransactionId;
import ec.com.sofka.aggregates.account.entities.transaction.values.objects.ProcessingDate;
import ec.com.sofka.aggregates.account.entities.transaction.values.objects.TransactionAccount;
import ec.com.sofka.aggregates.account.entities.transactionType.TransactionType;


public class Transaction extends Entity<TransactionId> {

    private final TransactionAccount transactionAccount;

    private String details;

    private final Amount amount;

    private final ProcessingDate processingDate;

    private Account account;

    private TransactionType transactionType;

    public Transaction(TransactionId id, TransactionAccount transactionAccount, String details, Amount amount, ProcessingDate processingDate, Account account, TransactionType transactionType) {
        super(id);
        this.transactionAccount = transactionAccount;
        this.details = details;
        this.amount = amount;
        this.processingDate = processingDate;
        this.account = account;
        this.transactionType = transactionType;
    }

    public TransactionAccount getTransactionAccount() {
        return transactionAccount;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDetails() {
        return details;
    }

    public Amount getAmount() {
        return amount;
    }

    public ProcessingDate getProcessingDate() {
        return processingDate;
    }

    public Account getAccount() {
        return account;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

}