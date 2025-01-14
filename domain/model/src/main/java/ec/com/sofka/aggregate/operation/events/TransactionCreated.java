package ec.com.sofka.aggregate.operation.events;

import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.transaction.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionCreated extends DomainEvent {
    private String id;
    private BigDecimal amount;
    private BigDecimal fee;
    private BigDecimal finalAmount;
    private TransactionType type;
    private LocalDateTime timestamp;
    private String accountId;

    public TransactionCreated(String id, BigDecimal amount, BigDecimal fee, BigDecimal finalAmount, TransactionType type, LocalDateTime timestamp, String accountId) {
        super(EventsTransactionEnum.TRANSACTION_CREATED.name());
        this.id = id;
        this.amount = amount;
        this.fee = fee;
        this.finalAmount = finalAmount;
        this.type = type;
        this.timestamp = timestamp;
        this.accountId = accountId;
    }

    public TransactionCreated() {
        super(EventsTransactionEnum.TRANSACTION_CREATED.name());
    }

    public String getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public BigDecimal getFinalAmount() {
        return finalAmount;
    }

    public TransactionType getType() {
        return type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getAccountId() {
        return accountId;
    }
}
