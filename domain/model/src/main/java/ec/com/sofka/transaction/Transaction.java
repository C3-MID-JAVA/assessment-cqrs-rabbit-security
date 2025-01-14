package ec.com.sofka.transaction;

import ec.com.sofka.card.values.objects.AccountValue;
import ec.com.sofka.generics.utils.Entity;
import ec.com.sofka.transaction.values.TransactionId;
import ec.com.sofka.transaction.values.objects.*;

import java.math.BigDecimal;

public abstract class Transaction  extends Entity<TransactionId> {
    private final Description description;
    private final Amount amount;
    private final TransactionType transactionType;
    private TransactionFee transactionFee;
    private final Timestamp timestamp;
    private final AccountValue accountValue;
    private final CardValue cardValue;




    protected Transaction(TransactionId transactionId, Description description, Amount amount, TransactionType transactionType, TransactionFee transactionFee, Timestamp timestamp, AccountValue accountValue, CardValue cardValue) {
        super(transactionId);
        this.description = description;
        this.amount = amount;
        this.transactionType = transactionType;
        this.transactionFee = transactionFee;
        this.timestamp = timestamp;
        this.accountValue = accountValue;
        this.cardValue = cardValue;
    }


    public abstract void processTransaction();

    public Description getDescription() {
        return description;
    }

    public Amount getAmount() {
        return amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public TransactionFee getTransactionFee() {
        return transactionFee;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public AccountValue getAccountValue() {
        return accountValue;
    }

    public CardValue getCardValue() {
        return cardValue;
    }

    public void setTransactionFee(TransactionFee transactionFee) {
        this.transactionFee = transactionFee;
    }
}
