package ec.com.sofka.aggregate.account.events;

import ec.com.sofka.generics.domain.DomainEvent;

import java.math.BigDecimal;

public class AccountBalanceUpdated extends DomainEvent {

    private String id;
    private String accountNumber;
    private BigDecimal accountBalance;
    private String ownerName;
    private String accountType;

    public AccountBalanceUpdated(String id, String accountNumber, BigDecimal accountBalance, String ownerName, String accountType) {
        super(EventsEnum.ACCOUNT_BALANCE_UPDATED.name());
        this.id = id;
        this.accountNumber = accountNumber;
        this.accountBalance = accountBalance;
        this.ownerName = ownerName;
        this.accountType = accountType;
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

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getAccountType() {
        return accountType;
    }
}
