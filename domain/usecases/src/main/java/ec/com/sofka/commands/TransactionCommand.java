package ec.com.sofka.commands;

import ec.com.sofka.generics.shared.Command;

import java.math.BigDecimal;

public class TransactionCommand extends Command {

    private BigDecimal amount;
    private String processingDate;
    private String accountNumber;
    private String details;
    private String transactionTypeId;
    private String accountAggregateId;

    public TransactionCommand(BigDecimal amount, String processingDate, String accountNumber, String details, String transactionTypeId, String accountAggregateId) {
        super(null);
        this.amount = amount;
        this.processingDate = processingDate;
        this.accountNumber = accountNumber;
        this.details = details;
        this.transactionTypeId = transactionTypeId;
        this.accountAggregateId = accountAggregateId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getProcessingDate() {
        return processingDate;
    }

    public void setProcessingDate(String processingDate) {
        this.processingDate = processingDate;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTransactionTypeId() {
        return transactionTypeId;
    }

    public void setTransactionTypeId(String transactionTypeId) {
        this.transactionTypeId = transactionTypeId;
    }

    public String getAccountAggregateId() {
        return accountAggregateId;
    }

    public void setAccountAggregateId(String accountAggregateId) {
        this.accountAggregateId = accountAggregateId;
    }
}