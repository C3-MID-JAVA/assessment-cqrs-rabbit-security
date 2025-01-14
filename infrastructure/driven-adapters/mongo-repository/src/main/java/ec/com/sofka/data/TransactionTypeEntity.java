package ec.com.sofka.data;

import ec.com.sofka.utils.enums.StatusEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Document("transaction_types")
public class TransactionTypeEntity {
    @Id
    private String id;

    @Indexed(unique = true)
    @Field(name = "type")
    private String type;

    @Field(name = "description")
    private String description;

    @Field(name = "value")
    private BigDecimal value;

    @Field(name = "transaction_cost")
    private Boolean transactionCost;

    @Field(name = "discount")
    private Boolean discount;

    @Field(name = "status")
    private StatusEnum status;

    public TransactionTypeEntity() {
    }

    public TransactionTypeEntity(String type, String description, BigDecimal value, Boolean transactionCost, Boolean discount, StatusEnum status) {
        this.type = type;
        this.description = description;
        this.value = value;
        this.transactionCost = transactionCost;
        this.discount = discount;
        this.status = status;
    }

    public TransactionTypeEntity(String id, String type, String description, BigDecimal value, Boolean transactionCost, Boolean discount, StatusEnum status) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.value = value;
        this.transactionCost = transactionCost;
        this.discount = discount;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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