package ec.com.sofka.aggregate.transaction;

import ec.com.sofka.aggregate.transaction.events.TransactionCreated;
import ec.com.sofka.aggregate.transaction.values.PaymentId;
import ec.com.sofka.generics.utils.AggregateRoot;
import ec.com.sofka.transaction.Transaction;

public class Payment<T extends Transaction>  extends AggregateRoot<PaymentId> {
    private T transaction;

    public Payment(){
        super(new PaymentId());
        setSubscription(new PaymentHandler<>(this));
    }

    public Payment(final String id){
        super(PaymentId.of(id));
        setSubscription(new PaymentHandler<>(this));
    }

    public void createPayment(TransactionCreated transactionCreated){
        addEvent(transactionCreated).apply();

    }


    public T getTransaction() {
        return transaction;
    }

    public void setTransaction(T transaction) {
        this.transaction = transaction;
    }
}
