package ec.com.sofka.gateway.dto;

import java.math.BigDecimal;

public class TransactionDTO {
    private String id;
    private BigDecimal amount;
    private String type;
    private BigDecimal cost;
    private String idAccount;
    private String status;

    public TransactionDTO(BigDecimal amount, String type, BigDecimal cost, String idAccount, String status) {
        this.amount = amount;
        this.type = type;
        this.cost = cost;
        this.idAccount = idAccount;
        this.status=status;
    }

    public TransactionDTO(String id, BigDecimal amount, String type, BigDecimal cost, String idAccount, String status) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.cost = cost;
        this.idAccount = idAccount;
        this.status=status;
    }

    public String getId() {
        return id;
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