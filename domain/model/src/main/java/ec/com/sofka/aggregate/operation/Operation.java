package ec.com.sofka.aggregate.operation;

import ec.com.sofka.aggregate.operation.events.TransactionCreated;
import ec.com.sofka.aggregate.operation.values.OperationId;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.generics.utils.AggregateRoot;
import ec.com.sofka.transaction.Transaction;
import ec.com.sofka.transaction.TransactionType;
import ec.com.sofka.transaction.values.TransactionId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Operation extends AggregateRoot<OperationId> {
    private Transaction transaction;

    public Operation() {
        super(new OperationId());
        setSubscription(new OperationHandler(this));
    }

    public Operation(final String id) {
        super(OperationId.of(id));
        setSubscription(new OperationHandler(this));
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public void createTransaction(BigDecimal amount, BigDecimal fee, BigDecimal netAmount, LocalDateTime timestamp, TransactionType type, String accuontId) {
        addEvent(new TransactionCreated(new TransactionId().getValue(), amount, fee, netAmount, type, timestamp, accuontId)).apply();
    }

    public static Mono<Operation> from(final String id, Flux<DomainEvent> events) {
        Operation operation = new Operation(id);

        return events
                .filter(eventsFilter -> id.equals(eventsFilter.getAggregateRootId()))
                .flatMap(event -> Mono.fromRunnable(() -> operation.addEvent(event).apply()))
                .then(Mono.just(operation));
    }
}
