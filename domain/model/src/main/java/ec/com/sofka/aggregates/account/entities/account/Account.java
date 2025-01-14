package ec.com.sofka.aggregates.account.entities.account;

import ec.com.sofka.aggregates.account.entities.account.values.AccountId;
import ec.com.sofka.aggregates.account.entities.account.values.objects.AccountNumber;
import ec.com.sofka.aggregates.account.values.objects.Amount;
import ec.com.sofka.generics.shared.Entity;
import ec.com.sofka.aggregates.account.entities.customer.Customer;
import ec.com.sofka.utils.enums.StatusEnum;

public class Account extends Entity<AccountId> {
    private final AccountNumber accountNumber;
    private Amount balance;
    private StatusEnum status;
    private Customer customer;

    public Account(AccountId id, AccountNumber accountNumber, Amount balance, StatusEnum status, Customer customer) {
        super(id);
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.status = status;
        this.customer = customer;
    }

    public AccountNumber getAccountNumber() {
        return accountNumber;
    }

    public Amount getBalance() {
        return balance;
    }

    public void setBalance(Amount balance) {
        this.balance = balance;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public Customer getUser() {
        return customer;
    }

    public void setUser(Customer customer) {
        this.customer = customer;
    }
}