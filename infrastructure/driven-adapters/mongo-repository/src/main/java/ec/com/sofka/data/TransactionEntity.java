package ec.com.sofka.data;

import ec.com.sofka.enums.TransactionType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "transaction")
public class TransactionEntity {

    @Id
    private String id;

    @Field("amount")
    private BigDecimal amount;

    @Field("transaction_cost")
    private BigDecimal transactionCost;

    @Field("date")
    private LocalDateTime date;

    @Field("transaction_type")
    private TransactionType type;

    @Field("account_id")
    private String accountId;

    public TransactionEntity(String id, BigDecimal amount, BigDecimal transactionCost, LocalDateTime date, TransactionType type, String accountId) {
        this.id = id;
        this.amount = amount;
        this.transactionCost = transactionCost;
        this.date = date;
        this.type = type;
        this.accountId = accountId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getTransactionCost() {
        return transactionCost;
    }

    public void setTransactionCost(BigDecimal transactionCost) {
        this.transactionCost = transactionCost;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
