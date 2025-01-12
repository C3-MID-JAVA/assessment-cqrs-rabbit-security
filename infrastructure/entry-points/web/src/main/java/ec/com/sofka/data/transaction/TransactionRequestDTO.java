package ec.com.sofka.data.transaction;

import java.math.BigDecimal;

public class TransactionRequestDTO {
    private String operationId;
    private  BigDecimal amount;
    private  String type;
    private BigDecimal cost;
    private String idAccount;
    private  String status;

    public TransactionRequestDTO(String operationId, BigDecimal amount, String type, BigDecimal cost, String idAccount, String status) {
        this.operationId = operationId;
        this.amount = amount;
        this.type = type;
        this.cost = cost;
        this.idAccount = idAccount;
        this.status = status;
    }

    public String getOperationId() {
        return operationId;
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
