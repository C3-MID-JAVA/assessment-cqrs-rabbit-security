package ec.com.sofka.aggregates.account.events;

import ec.com.sofka.aggregates.account.entities.customer.Customer;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.utils.enums.EventsDetailsEnum;
import ec.com.sofka.utils.enums.StatusEnum;

import java.math.BigDecimal;

public class AccountUpdated extends DomainEvent {
    private String accountId;
    private String accountNumber;
    private BigDecimal balance;
    private StatusEnum status;
    private Customer customer;

    public AccountUpdated(String accountId, String accountNumber, BigDecimal balance, StatusEnum status, Customer customer) {
        super(EventsDetailsEnum.ACCOUNT_UPDATED.getEventType());
        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.status = status;
        this.customer = customer;
    }

    public AccountUpdated() {
        super(EventsDetailsEnum.ACCOUNT_CREATED.getEventType());
    }

    public String getAccountId() {
        return accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public Customer getUser() {
        return customer;
    }

}