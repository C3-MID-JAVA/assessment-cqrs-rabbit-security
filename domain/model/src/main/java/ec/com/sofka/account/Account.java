package ec.com.sofka.account;

import ec.com.sofka.account.values.AccountId;
import ec.com.sofka.account.values.objects.*;
import ec.com.sofka.generics.utils.Entity;

//4. Creation of an Entity class - They have logic and behavior, otherwise is a ValueObject.
public class Account extends Entity<AccountId> {
    private final Balance balance;
    private final NumberAcc numberAcc;
    private final Name name;
    private final Status status;
    private final UserId userId;

    public Account(AccountId id,  NumberAcc numberAcc, Name name, Balance balance, Status status,UserId userId) {
        super(id);
        this.balance = balance;
        this.numberAcc = numberAcc;
        this.name = name;
        this.status = status;
        this.userId = userId;
    }

    public Balance getBalance() {
        return balance;
    }
    public NumberAcc getNumber() {
        return numberAcc;
    }
    public Name getName() {
        return name;
    }
    public Status getStatus() {
        return status;
    }
    public UserId getUserId() {
        return userId;
    }


}
