package ec.com.sofka.aggregate.customer.events;

import ec.com.sofka.generics.domain.DomainEvent;

import java.math.BigDecimal;

public class AccountCreated extends DomainEvent {
    private String id;
    private String accountNumber;
    private BigDecimal balance;
    private String userId;

    public AccountCreated(String id, String accountNumber, BigDecimal balance, String userId) {
        super(EventsEnum.ACCOUNT_CREATED.name());
        this.id = id;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.userId = userId;
    }

    public AccountCreated(String accountNumber, BigDecimal balance, String userId) {
        super(EventsEnum.ACCOUNT_CREATED.name());
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.userId = userId;
    }

    public AccountCreated() {
        super(EventsEnum.ACCOUNT_CREATED.name());
    }

    public String getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getUserId() {
        return userId;
    }
}
