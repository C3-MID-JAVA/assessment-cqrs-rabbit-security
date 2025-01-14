package ec.com.sofka.gateway.dto;

import java.math.BigDecimal;

public class AccountDTO {
    private String id;
    private String accountNumber;
    private BigDecimal balance;
    private String userId;

    public AccountDTO(String id) {
        this.id = id;
    }

    public AccountDTO(BigDecimal balance, String accountNumber, String userId) {
        this.balance = balance;
        this.accountNumber = accountNumber;
        this.userId = userId;
    }

    public AccountDTO(String id, String accountNumber, BigDecimal balance, String userId) {
        this.accountNumber = accountNumber;
        this.id = id;
        this.balance = balance;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }


    public String getUserId() {
        return userId;
    }


    public BigDecimal getBalance() {
        return balance;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
