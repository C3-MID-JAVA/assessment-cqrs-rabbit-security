package ec.com.sofka.data;

import ec.com.sofka.transaction.Transaction;
import ec.com.sofka.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionResponseDTO {
    private String transactionId;

    private String accountId;

    private BigDecimal transactionCost;

    private BigDecimal amount;
    private LocalDateTime date;

    private TransactionType transactionType;
   // private String accountNumber;

    public TransactionResponseDTO(String transactionId, String accountId, BigDecimal transactionCost, BigDecimal amount, LocalDateTime date,
                                  TransactionType transactionType) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.transactionCost = transactionCost;
        this.amount = amount;
        this.date = date;
        this.transactionType = transactionType;

    }

    public TransactionResponseDTO(){

    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getTransactionCost() {
        return transactionCost;
    }

    public void setTransactionCost(BigDecimal transactionCost) {
        this.transactionCost = transactionCost;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
}
