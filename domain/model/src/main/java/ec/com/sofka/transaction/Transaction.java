package ec.com.sofka.transaction;

import ec.com.sofka.account.values.AccountId;
import ec.com.sofka.generics.utils.Entity;
import ec.com.sofka.transaction.values.TransactionId;
import ec.com.sofka.transaction.values.objects.*;

public class Transaction extends Entity<TransactionId> {
    private final Amount amount;
    private final Fee fee;
    private final FinalAmount finalAmount;
    private final Timestamp timestamp;
    private final Type type;
    private final AccountId accountId;

    public Transaction(TransactionId id, Amount amount, Fee fee, FinalAmount finalAmount, Timestamp timestamp, Type type, AccountId accountId) {
        super(id);
        this.amount = amount;
        this.fee = fee;
        this.finalAmount = finalAmount;
        this.timestamp = timestamp;
        this.type = type;
        this.accountId = accountId;
    }

    public Amount getAmount() {
        return amount;
    }

    public Fee getFee() {
        return fee;
    }

    public FinalAmount getFinalAmount() {
        return finalAmount;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public Type getType() {
        return type;
    }

    public AccountId getAccountId() {
        return accountId;
    }
}