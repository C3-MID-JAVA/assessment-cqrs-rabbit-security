package ec.com.sofka.aggregate.events;

import ec.com.sofka.generics.domain.DomainEvent;

import java.math.BigDecimal;

public class AccountUpdated extends DomainEvent {
    private String accountId;
    private String accountNumber;
    private String owner;
    private String status;
    private BigDecimal balance;


    public AccountUpdated(String accountId,BigDecimal balance, String accountNumber, String owner, String status) {
        super(EventsEnum.ACCOUNT_UPDATED.name());
        this.accountId = accountId;
        this.balance = balance;
        this.accountNumber = accountNumber;
        this.owner = owner;
        this.status = status;

    }

    public AccountUpdated(BigDecimal balance, String accountNumber, String owner, String status) {
        super(EventsEnum.ACCOUNT_UPDATED.name());
        this.balance = balance;
        this.accountNumber = accountNumber;
        this.owner = owner;
        this.status = status;

    }



    public AccountUpdated() {
        super(EventsEnum.ACCOUNT_UPDATED.name());

    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwner() {
        return owner;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getStatus() {
        return status;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
