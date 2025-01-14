package ec.com.sofka.account;


import ec.com.sofka.account.values.AccountId;
import ec.com.sofka.account.values.objects.AccountType;
import ec.com.sofka.account.values.objects.Balance;
import ec.com.sofka.account.values.objects.OwnerName;
import ec.com.sofka.account.values.objects.AccountNumber;
import ec.com.sofka.generics.utils.Entity;



public class Account extends Entity<AccountId> {
    private final Balance balance;
    private final AccountNumber accountNumber;
    private final OwnerName ownerName;
    private final AccountType type;

    public Account(AccountId id, Balance balance, AccountNumber numberAcc, OwnerName name, AccountType type) {
        super(id);
        this.balance = balance;
        this.accountNumber = numberAcc;
        this.ownerName = name;
        this.type = type;
    }

    public Account() {
        super(null);
        this.balance = null;
        this.accountNumber = null;
        this.ownerName = null;
        this.type = null;
    }

    public Balance getBalance() {
        return balance;
    }

    public AccountNumber getAccountNumber() {
        return accountNumber;
    }
    public OwnerName getOwnerName() {
        return ownerName;
    }

    public AccountType getType() {
        return type;
    }
}
