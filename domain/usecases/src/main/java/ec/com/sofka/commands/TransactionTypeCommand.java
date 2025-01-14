package ec.com.sofka.commands;

import ec.com.sofka.generics.shared.Command;
import ec.com.sofka.utils.enums.StatusEnum;

import java.math.BigDecimal;

public class TransactionTypeCommand extends Command {
    private String type;
    private String description;
    private BigDecimal value;
    private Boolean transactionCost;
    private Boolean discount;
    private StatusEnum status;

    public TransactionTypeCommand(String type, String description, BigDecimal value, Boolean transactionCost, Boolean discount, StatusEnum status) {
        super(null);
        this.type = type;
        this.description = description;
        this.value = value;
        this.transactionCost = transactionCost;
        this.discount = discount;
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Boolean getTransactionCost() {
        return transactionCost;
    }

    public void setTransactionCost(Boolean transactionCost) {
        this.transactionCost = transactionCost;
    }

    public Boolean getDiscount() {
        return discount;
    }

    public void setDiscount(Boolean discount) {
        this.discount = discount;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

}