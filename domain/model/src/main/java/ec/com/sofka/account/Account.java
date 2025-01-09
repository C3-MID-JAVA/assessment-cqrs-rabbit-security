package ec.com.sofka.account;


import ec.com.sofka.account.values.AccountId;
import ec.com.sofka.account.values.objects.Balance;
import ec.com.sofka.account.values.objects.NumberAcc;
import ec.com.sofka.account.values.objects.Owner;
import ec.com.sofka.account.values.objects.Status;
import ec.com.sofka.generics.utils.Entity;
import ec.com.sofka.transaction.Transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Account extends Entity<AccountId> {
    private final Balance balance;
    private final NumberAcc numberAcc;
    private final Owner owner;
    private final Status status;
    public Account(AccountId id, Balance balance, NumberAcc numberAcc, Owner owner, Status status) {
        super(id);
        this.balance = balance;
        this.numberAcc = numberAcc;
        this.owner = owner;
        this.status = status;
    }
    public Balance getBalance() {
        return balance;
    }
    public Status getStatus() {
        return status;
    }
    public NumberAcc getAccountNumber() {
        return numberAcc;
    }
    public Owner getOwner() {
        return owner;
    }

}
