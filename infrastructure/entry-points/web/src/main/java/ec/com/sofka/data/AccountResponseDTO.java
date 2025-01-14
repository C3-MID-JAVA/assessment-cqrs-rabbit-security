package ec.com.sofka.data;

import java.io.Serializable;
import java.math.BigDecimal;

public class AccountResponseDTO implements Serializable {

    private String accountNumber;
    private BigDecimal balance;
    private String status;
    private CustomerResponseDTO customer;

    public AccountResponseDTO() {
    }

    public AccountResponseDTO(String accountNumber, BigDecimal balance, String status, CustomerResponseDTO customer) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.status = status;
        this.customer = customer;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CustomerResponseDTO getUser() {
        return customer;
    }

    public void setUser(CustomerResponseDTO customer) {
        this.customer = customer;
    }

}