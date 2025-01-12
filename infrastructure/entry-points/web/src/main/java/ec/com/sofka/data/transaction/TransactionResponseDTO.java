package ec.com.sofka.data.transaction;

import java.math.BigDecimal;

public class TransactionResponseDTO {
    private String operationId;
    private String transactionId;
    private BigDecimal amount;
    private  String type;
    private BigDecimal cost;
    private String idAccount;
    private  String status;

    public TransactionResponseDTO(String operationId, String transactionId, BigDecimal amount, String type, BigDecimal cost, String idAccount, String status) {
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
