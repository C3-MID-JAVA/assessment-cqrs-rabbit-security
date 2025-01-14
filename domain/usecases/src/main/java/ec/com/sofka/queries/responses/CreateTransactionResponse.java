package ec.com.sofka.queries.responses;

import java.math.BigDecimal;

public class CreateTransactionResponse {
    private final String operationId;
    private final String transactionId;
    private final BigDecimal amount;
    private final String type;
    private final BigDecimal cost;
    private final String idAccount;
    private final String status;

    public CreateTransactionResponse(String operationId, String transactionId, BigDecimal amount, String type, BigDecimal cost, String idAccount, String status) {
        this.operationId = operationId;
        this.transactionId = transactionId;
        this.amount = amount;
        this.type = type;
        this.cost = cost;
        this.idAccount = idAccount;
        this.status = status;
    }

    public String getOperationId() {
        return operationId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public String getIdAccount() {
        return idAccount;
    }

    public String getStatus() {
        return status;
    }
}