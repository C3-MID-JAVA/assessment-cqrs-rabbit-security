package ec.com.sofka.aggregates.account.events;

import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.utils.enums.EventsDetailsEnum;
import ec.com.sofka.utils.enums.StatusEnum;

import java.math.BigDecimal;

public class TransactionTypeCreated extends DomainEvent {
    private String transactionTypeId;
    private String type;
    private String description;
    private BigDecimal value;
    private Boolean transactionCost;
    private Boolean discount;
    private StatusEnum status;

    public TransactionTypeCreated(String transactionTypeId, String type, String description, BigDecimal value, Boolean transactionCost, Boolean discount, StatusEnum status) {
        super(EventsDetailsEnum.TRANSACTION_TYPE_CREATED.getEventType());
        this.transactionTypeId = transactionTypeId;
        this.type = type;
        this.description = description;
        this.value = value;
        this.transactionCost = transactionCost;
        this.discount = discount;
        this.status = status;
    }

    public TransactionTypeCreated(){
        super(EventsDetailsEnum.TRANSACTION_TYPE_CREATED.getEventType());
    }

    public String getTransactionTypeId() {
        return transactionTypeId;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getValue() {
        return value;
    }

    public Boolean getTransactionCost() {
        return transactionCost;
    }

    public Boolean getDiscount() {
        return discount;
    }

    public StatusEnum getStatus() {
        return status;
    }

}