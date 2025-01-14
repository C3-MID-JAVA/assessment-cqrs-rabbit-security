package ec.com.sofka.aggregate;

import ec.com.sofka.aggregate.events.TransactionCreated;
import ec.com.sofka.aggregate.values.OperationId;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.generics.utils.AggregateRoot;
import ec.com.sofka.transaction.Transaction;

import java.math.BigDecimal;
import java.util.List;

public class Operation extends AggregateRoot<OperationId> {
    private Transaction transaction;

    public Operation() {
        super(new OperationId());
        setSubscription(new OperationHandler(this));
    }

    private Operation(final String id) {
        super(OperationId.of(id));
        setSubscription(new OperationHandler(this));
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public void createTransaction(BigDecimal amount, String type, BigDecimal cost, String idAccount, String status) {
        addEvent(new TransactionCreated(amount, type, cost, idAccount,status)).apply();
    }

    public static Operation from(final String id, List<DomainEvent> events) {
        Operation operation = new Operation(id);
        events.stream()
                .filter(event -> id.equals(event.getAggregateRootId()))
                .forEach(event -> operation.addEvent(event).apply());
        operation.markEventsAsCommitted();
        return operation;
    }
}