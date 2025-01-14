package ec.com.sofka.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Document(collection = "accounts")
public class AccountEntity {
    @Id
    private String id;

    @Field(name = "account_number")
    private String accountNumber;

    @Field(name = "account_balance")
    private BigDecimal balance;

    @Field(name = "owner_name")
    private String ownerName;

    @Field(name = "account_type")
    private String accountType;

    public AccountEntity() {
    }

    public AccountEntity(String id, String accountNumber, BigDecimal balance, String ownerName, String accountType) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.ownerName = ownerName;
        this.accountType = accountType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

}
