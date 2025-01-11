package ec.com.sofka.gateway.dto;

import java.math.BigDecimal;

//This class is used to transfer data between the application and the database -
//Notice how this affect the AccountRepository interface that lives in usecases
//Notice also how this impacts on the driven adapter that implements the AccountRepository interface that lives in usecases.
public class AccountDTO {
    private String id;
    private String accountNumber;
    private String name;
    private BigDecimal balance;
    private String status;
    private String idUser;

    public AccountDTO(String id, String name, String accountNumber, BigDecimal balance, String status) {
        this.id = id;
        this.balance = balance;
        this.name = name;
        this.accountNumber = accountNumber;
        this.status = status;
    }

    public AccountDTO(String id, String name, String accountNumber, BigDecimal balance, String status, String idUser) {
        this.id = id;
        this.balance = balance;
        this.name = name;
        this.accountNumber = accountNumber;
        this.status = status;
        this .idUser = idUser;
    }

    public AccountDTO(String name, String accountNumber, BigDecimal balance, String status) {
        this.balance = balance;
        this.name = name;
        this.accountNumber = accountNumber;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getIdUser() {
        return idUser;
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
