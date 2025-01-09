package ec.com.sofka.appservice.gateway.dto;

import java.math.BigDecimal;

//This class is used to transfer data between the application and the database -
//Notice how this affect the AccountRepository interface that lives in usecases
//Notice also how this impacts on the driven adapter that implements the AccountRepository interface that lives in usecases.
public class AccountDTO {
    private String accountId;
    private String accountNumber;
    private String name;
    private BigDecimal balance;
    private String status;

    public AccountDTO(String accountId, String name, String accountNumber, BigDecimal balance, String status) {
        this.accountId = accountId;
        this.balance = balance;
        this.name = name;
        this.accountNumber = accountNumber;
        this.status = status;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }


    public String getName() {
        return name;
    }


    public BigDecimal getBalance() {
        return balance;
    }

    public String getStatus() {
        return status;
    }
}