package ec.com.sofka.gateway.dto;

import ec.com.sofka.utils.enums.StatusEnum;

import java.math.BigDecimal;

public class AccountDTO {
    private String id;
    private String accountNumber;
    private BigDecimal balance;
    private StatusEnum status;
    private CustomerDTO customerDTO;

    public AccountDTO() {}

    public AccountDTO(String id, String accountNumber, BigDecimal balance, StatusEnum status, CustomerDTO customerDTO) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.status = status;
        this.customerDTO = customerDTO;
    }

    public AccountDTO(String accountNumber, BigDecimal balance, StatusEnum status, CustomerDTO customerDTO) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.status = status;
        this.customerDTO = customerDTO;
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

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public CustomerDTO getUser() {
        return customerDTO;
    }

    public void setUser(CustomerDTO customerDTO) {
        this.customerDTO = customerDTO;
    }

}