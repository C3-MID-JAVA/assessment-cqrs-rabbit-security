package ec.com.sofka.appservice.queries.responses;

import ec.com.sofka.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionResponse {
    private final String customerId;
    private final String transactionId;
    private final String accountId;
    private final BigDecimal transactionCost;
    private final BigDecimal amount;
    private final LocalDateTime transactionDate;
    private final TransactionType transactionType;

    public TransactionResponse(String customerId, String transactionId,
                               String accountId,
                               BigDecimal transactionCost,
                               BigDecimal amount,
                               LocalDateTime transactionDate,
                               TransactionType transactionType) {
        this.customerId = customerId;
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.transactionCost = transactionCost;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.transactionType = transactionType;
    }

    public TransactionResponse(String transactionId,
                               String accountId,
                               BigDecimal transactionCost,
                               BigDecimal amount,
                               LocalDateTime transactionDate,
                               TransactionType transactionType) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.transactionCost = transactionCost;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.transactionType = transactionType;
        this.customerId = null;
    }


    public String getCustomerId() {
        return customerId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getAccountId() {
        return accountId;
    }

    public BigDecimal getTransactionCost() {
        return transactionCost;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }
}
