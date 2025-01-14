package ec.com.sofka.gateway.dto;

import ec.com.sofka.transaction.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionDTO {
    private String id;
    private BigDecimal amount;
    private BigDecimal fee;
    private BigDecimal finalAmount;
    private TransactionType type;
    private LocalDateTime timestamp;
    private String accountId;

    public TransactionDTO(BigDecimal amount, BigDecimal fee, BigDecimal finalAmount, TransactionType type, LocalDateTime timestamp, String accountId) {
        this.amount = amount;
        this.fee = fee;
        this.finalAmount = finalAmount;
        this.type = type;
        this.timestamp = timestamp;
        this.accountId = accountId;
    }

    public TransactionDTO(String id, BigDecimal amount, BigDecimal fee, BigDecimal finalAmount, TransactionType type, LocalDateTime timestamp, String accountId) {
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

    public String getAccountId() {
        return accountId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
