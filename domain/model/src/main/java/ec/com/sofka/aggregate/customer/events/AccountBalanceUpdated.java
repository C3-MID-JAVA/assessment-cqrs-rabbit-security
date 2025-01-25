package ec.com.sofka.aggregate.customer.events;

import ec.com.sofka.generics.domain.DomainEvent;

import java.math.BigDecimal;

public class AccountBalanceUpdated extends DomainEvent {
    private String id;
    private String accountNumber;
    private BigDecimal balance;
    private String userId;

    public AccountBalanceUpdated(String id, String accountNumber, BigDecimal balance, String userId) {
        super(EventsEnum.ACCOUNT_BALANCE_UPDATED.name());
        this.id = id;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.userId = userId;
    }

    public AccountBalanceUpdated(String accountNumber, BigDecimal balance, String userId) {
        super(EventsEnum.ACCOUNT_BALANCE_UPDATED.name());
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.userId = userId;
    }

    public AccountBalanceUpdated() {
        super(EventsEnum.ACCOUNT_BALANCE_UPDATED.name());
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
