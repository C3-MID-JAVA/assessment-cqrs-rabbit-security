package ec.com.sofka.aggregates.account.events;

import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.aggregates.account.entities.account.Account;
import ec.com.sofka.aggregates.account.entities.transactionType.TransactionType;
import ec.com.sofka.utils.enums.EventsDetailsEnum;

import java.math.BigDecimal;

public class TransactionCreated extends DomainEvent {
    private String transactionId;
    private String accountNumber;
    private String details;
    private BigDecimal amount;
    private String processingDate;
    private Account account;
    private TransactionType transactionType;

    public TransactionCreated(String transactionId, String accountNumber, String details, BigDecimal amount, String processingDate, Account account, TransactionType transactionType) {
        super(EventsDetailsEnum.TRANSACTION_CREATED.getEventType());
        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.details = details;
        this.amount = amount;
        this.processingDate = processingDate;
        this.account = account;
        this.transactionType = transactionType;
    }

    public TransactionCreated(){
        super(EventsDetailsEnum.TRANSACTION_CREATED.getEventType());
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getDetails() {
        return details;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getProcessingDate() {
        return processingDate;
    }

    public Account getAccount() {
        return account;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

}