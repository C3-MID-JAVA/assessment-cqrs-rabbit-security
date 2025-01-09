package ec.com.sofka.appservice.gateway.dto;

import ec.com.sofka.enums.TransactionType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionDTO {

    private String transactionId;

    private BigDecimal amount;

    private BigDecimal transactionCost;

    private LocalDateTime date;

    private TransactionType type;

    private String accountId;

    public TransactionDTO(String transactionId, BigDecimal amount, BigDecimal transactionCost,
                          LocalDateTime date, TransactionType type, String accountId) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.transactionCost = transactionCost;
        this.date = date;
        this.type = type;
        this.accountId = accountId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getTransactionCost() {
        return transactionCost;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public TransactionType getType() {
        return type;
    }

    public String getAccountId() {
        return accountId;
    }
}
