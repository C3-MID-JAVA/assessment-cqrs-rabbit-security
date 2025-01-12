package ec.com.sofka.transaction;


import ec.com.sofka.generics.utils.Entity;
import ec.com.sofka.transaction.values.TransactionId;
import ec.com.sofka.transaction.values.objects.*;

public class Transaction extends Entity<TransactionId> {
    private final Amount amount;
    private final Type type;
    private final Cost cost;
    private final IdAccount idAccount;
    private final Status status;

    public Transaction(TransactionId id, Amount amount, Type type, Cost cost, IdAccount idAccount, Status status) {
        super(id);
        this.amount = amount;
        this.type = type;
        this.cost = cost;
        this.idAccount = idAccount;
        this.status = status;
    }

    public Amount getAmount() {
        return amount;
    }

    public Type getType() {
        return type;
    }

    public Cost getCost() {
        return cost;
    }

    public IdAccount getIdAccount() {
        return idAccount;
    }

    public Status getStatus() {
        return status;
    }
}