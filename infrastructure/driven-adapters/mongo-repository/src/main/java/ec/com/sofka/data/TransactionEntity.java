package ec.com.sofka.data;

import ec.com.sofka.transaction.TransactionType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "transactions")
public class TransactionEntity {

    @Id
    private String id;

    private BigDecimal amount;

    private BigDecimal fee;

    private BigDecimal finalAmount;

    private TransactionType type;

    private LocalDateTime timestamp;

    private String accountId;


    public TransactionEntity() {
    }

    public TransactionEntity(BigDecimal amount, BigDecimal fee, BigDecimal finalAmount, TransactionType type, LocalDateTime timestamp, String accountId) {
        this.amount = amount;
        this.fee = fee;
        this.finalAmount = finalAmount;
        this.type = type;
        this.timestamp = timestamp;
        this.accountId = accountId;
    }

    public TransactionEntity(String id, BigDecimal amount, BigDecimal fee, BigDecimal finalAmount, TransactionType type, LocalDateTime timestamp, String accountId) {
        this.id = id;
        this.amount = amount;
        this.fee = fee;
        this.finalAmount = finalAmount;
        this.type = type;
        this.timestamp = timestamp;
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

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public BigDecimal getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(BigDecimal finalAmount) {
        this.finalAmount = finalAmount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
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