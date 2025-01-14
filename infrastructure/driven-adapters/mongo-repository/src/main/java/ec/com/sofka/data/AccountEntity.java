package ec.com.sofka.data;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "accounts")
public class AccountEntity {

    @Id
    private String id;

    private String accountNumber;

    private BigDecimal balance;

    private String userId;

    public AccountEntity() {
    }

    public AccountEntity(String accountNumber, BigDecimal balance, String userId) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.userId = userId;
    }

    public AccountEntity(String id, String accountNumber, BigDecimal balance, String userId) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.userId = userId;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
