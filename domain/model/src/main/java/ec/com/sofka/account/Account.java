package ec.com.sofka.account;

import ec.com.sofka.account.values.AccountId;
import ec.com.sofka.account.values.objects.AccountNumber;
import ec.com.sofka.account.values.objects.Balance;
import ec.com.sofka.generics.utils.Entity;
import ec.com.sofka.user.values.UserId;

//4. Creation of an Entity class - They have logic and behavior, otherwise is a ValueObject.
public class Account extends Entity<AccountId> {
    private final Balance balance;
    private final AccountNumber accountNumber;
    private final UserId userId;

    public Account(AccountId id, Balance balance, AccountNumber accountNumber, UserId userId) {
        super(id);
        this.balance = balance;
        this.accountNumber = accountNumber;
        this.userId = userId;
    }

    public Balance getBalance() {
        return balance;
    }

    public AccountNumber getAccountNumber() {
        return accountNumber;
    }

    public UserId getUserId() {
        return userId;
    }
}
