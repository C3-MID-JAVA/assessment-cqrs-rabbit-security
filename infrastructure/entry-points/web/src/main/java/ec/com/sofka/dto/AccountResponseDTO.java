package ec.com.sofka.dto;

import java.math.BigDecimal;

public class AccountResponseDTO {
    private String customerId;
    private String accountNumber;
    private BigDecimal balance;
    private String userId;

    public AccountResponseDTO(String customerId, String accountNumber, BigDecimal balance, String userId) {
        this.customerId = customerId;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.userId = userId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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
