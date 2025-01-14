package ec.com.sofka.aggregate.events;

import ec.com.sofka.enums.TransactionType;
import ec.com.sofka.generics.domain.DomainEvent;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionCreated extends DomainEvent {

    private String transactionId;
    private BigDecimal amount;
    private BigDecimal transactionCost;
    private LocalDateTime date;
    private TransactionType type;
    private String accountId;

    public TransactionCreated(String transactionId, BigDecimal amount, BigDecimal transactionCost, LocalDateTime date, TransactionType type, String accountId) {
        super(EventsEnum.TRANSACTION_CREATED.name());
        this.transactionId = transactionId;
        this.amount = amount;
        this.transactionCost = transactionCost;
        this.date = date;
        this.type = type;
        this.accountId = accountId;
    }

    // Constructor alternativo
    public TransactionCreated(BigDecimal amount, BigDecimal transactionCost, LocalDateTime date, TransactionType type, String accountId) {
        super(EventsEnum.TRANSACTION_CREATED.name());
        this.amount = amount;
        this.transactionCost = transactionCost;
        this.date = date;
        this.type = type;
        this.accountId = accountId;
    }

    // Constructor vacío
    public TransactionCreated() {
        super(EventsEnum.TRANSACTION_CREATED.name());
    }

    public String getTransactionId() {
        return transactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getTransactionCost() {
        return transactionCost;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public TransactionType getType() {
        return type;
    }

    public String getAccountId() {
        return accountId;
    }
}
