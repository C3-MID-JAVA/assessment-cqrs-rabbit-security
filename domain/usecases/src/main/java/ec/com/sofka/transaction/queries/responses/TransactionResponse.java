package ec.com.sofka.transaction.queries.responses;

import ec.com.sofka.transaction.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionResponse {
    private final String operationId;
    private final BigDecimal amount;
    private final BigDecimal fee;
    private final BigDecimal finalAmount;
    private final TransactionType type;
    private final LocalDateTime timestamp;
    private final String accountId;

    public TransactionResponse(String operationId, BigDecimal amount, BigDecimal fee, BigDecimal netAmount, TransactionType type, LocalDateTime timestamp, String accountId) {
        this.operationId = operationId;
        this.amount = amount;
        this.fee = fee;
        this.finalAmount = netAmount;
        this.type = type;
        this.timestamp = timestamp;
        this.accountId = accountId;
    }

    public String getOperationId() {
        return operationId;
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
