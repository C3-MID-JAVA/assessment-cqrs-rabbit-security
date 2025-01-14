package ec.com.sofka.data;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
@JsonInclude(JsonInclude.Include.NON_NULL)

public class AccountResponseDTO {
    public String customerId;

    public String accountId;

    public String owner;

    public String accountNumber;

    public BigDecimal balance;

    public String status;

    public AccountResponseDTO(String customerId, String accountId, String owner, String accountNumber, BigDecimal balance, String status) {
        this.customerId = customerId;
        this.accountId = accountId;
        this.owner = owner;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.status = status;
    }

    public AccountResponseDTO(String accountId, String owner, String accountNumber, BigDecimal balance, String status) {
        this.accountId = accountId;
        this.owner = owner;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.status = status;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getOwner() {
        return owner;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getStatus() {
        return status;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
