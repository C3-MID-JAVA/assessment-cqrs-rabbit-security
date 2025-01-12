package ec.com.sofka.data;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Document(collection = "transaction")
public class TransactionEntity {
    @Id
    private String id;

    private BigDecimal amount;

    private String type;

    private BigDecimal cost;

    private String idAccount; // ID referenciado de la cuenta

    private String status;

    public TransactionEntity() {
    }

    public TransactionEntity(String id,  BigDecimal amount, String type, BigDecimal cost, String idAccount, String status) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.cost = cost;
        this.idAccount = idAccount;
        this.status = status;
    }
    public TransactionEntity( BigDecimal amount, String type, BigDecimal cost, String idAccount, String status) {
        this.amount = amount;
        this.type = type;
        this.cost = cost;
        this.idAccount = idAccount;
        this.status = status;
    }

    public String getStatus() {
        return status;
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



}
