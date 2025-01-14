package ec.com.sofka.aggregates.account.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import ec.com.sofka.aggregates.account.entities.customer.Customer;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.utils.enums.EventsDetailsEnum;
import ec.com.sofka.utils.enums.StatusEnum;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountCreated extends DomainEvent {
    @JsonProperty(value = "accountId")
    private String accountId;
    private String accountNumber;
    private BigDecimal balance;
    private StatusEnum status;
    private Customer customer;

    public AccountCreated(String accountId, String accountNumber, BigDecimal balance, StatusEnum status, Customer customer) {
        super(EventsDetailsEnum.ACCOUNT_CREATED.getEventType());
        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.status = status;
        this.customer = customer;
    }

    public AccountCreated() {
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