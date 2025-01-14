package ec.com.sofka.gateway.dto.account;

import java.math.BigDecimal;

public class AccountDTO {
    private String id;
    private String accountNumber;
    private String ownerName;
    private BigDecimal balance;
    private String accountType;

    public AccountDTO() {
    }

    public AccountDTO(BigDecimal balance, String ownerName, String accountNumber, String accountType) {
        this.balance = balance;
        this.ownerName = ownerName;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
    }
    public AccountDTO(String id, BigDecimal balance, String ownerName, String accountNumber, String accountType) {
        this.id = id;
        this.balance = balance;
        this.ownerName = ownerName;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
    }


    public String getId() {
        return id;
    }



    public String getAccountNumber() {
        return accountNumber;
    }


    public String getOwnerName() {
        return ownerName;
    }


    public BigDecimal getBalance() {
        return balance;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
