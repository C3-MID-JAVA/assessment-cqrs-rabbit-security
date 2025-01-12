package ec.com.sofka.aggregate.events;

import ec.com.sofka.generics.domain.DomainEvent;

import java.math.BigDecimal;

public class TransactionCreated extends DomainEvent {
    private String transactionId;
    private BigDecimal amount;
    private String type;
    private BigDecimal cost;
    private String idAccount;
    private String status;

    public TransactionCreated(String transactionId, BigDecimal amount, String type, BigDecimal cost, String idAccount, String status) {
        super(EventsEnum.TRANSACTION_CREATED.name());
        this.transactionId = transactionId;
        this.amount = amount;
        this.type = type;
        this.cost = cost;
        this.idAccount = idAccount;
        this.status = status;
    }

    public TransactionCreated(BigDecimal amount, String type, BigDecimal cost, String idAccount, String status) {
        super(EventsEnum.TRANSACTION_CREATED.name());
        this.amount = amount;
        this.type = type;
        this.cost = cost;
        this.idAccount = idAccount;
        this.status = status;
    }

    public TransactionCreated() {
        super(EventsEnum.TRANSACTION_CREATED.name());
    }

    public String getTransactionId() {
        return transactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public String getIdAccount() {
        return idAccount;
    }
    public String getStatus() {
        return status;
    }
}