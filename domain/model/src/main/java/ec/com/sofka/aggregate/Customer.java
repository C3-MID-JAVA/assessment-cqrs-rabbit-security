package ec.com.sofka.aggregate;

import ec.com.sofka.account.Account;
import ec.com.sofka.account.values.AccountId;
import ec.com.sofka.aggregate.events.AccountCreated;
import ec.com.sofka.aggregate.events.AccountUpdated;
import ec.com.sofka.aggregate.events.TransactionCreated;
import ec.com.sofka.aggregate.values.CustomerId;
import ec.com.sofka.enums.TransactionType;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.generics.utils.AggregateRoot;
import ec.com.sofka.transaction.Transaction;
import ec.com.sofka.transaction.values.TransactionId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//2. Creation of the aggregate class - The communication between the entities and the external world.
public class Customer extends AggregateRoot<CustomerId> {
    //5. Add the Account to the aggregate: Can't be final bc the aggregate is mutable by EventDomains
    private Account account;

    private List<Transaction> transactions = new ArrayList<>(); // Gestión de transacciones


    //To create the Aggregate the first time, ofc have to set the id as well.
    public Customer() {
        super(new CustomerId());
        //Add the handler to the aggregate
        setSubscription(new CustomerHandler(this));
    }

    //To rebuild the aggregate
    private Customer(final String id) {
        super(CustomerId.of(id));
        //Add the handler to the aggregate
        setSubscription(new CustomerHandler(this));
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    //Remember that User as Aggregate is the open door to interact with the entities
    public void createAccount(String accountNumber, BigDecimal accountBalance, String name, String status) {
        //Add the event to the aggregate
        addEvent(new AccountCreated(new AccountId().getValue(), accountNumber,accountBalance,name,status)).apply();

    }

    public void createTransaction(BigDecimal amount, BigDecimal transactionCost, LocalDateTime date, TransactionType type, String accountId) {
        addEvent(new TransactionCreated(new TransactionId().getValue(),amount,transactionCost,date,type,accountId)).apply();
    }

    //Remember that User as Aggregate is the open door to interact with the entities
    public void updateAccount(String accountId, BigDecimal balance, String accountNumber, String name, String status ) {
        //Add the event to the aggregate
        addEvent(new AccountUpdated(accountId, balance, accountNumber, name, status)).apply();

    }

    public static Mono<Customer> from(final String id, Flux<DomainEvent> events) {
        Customer operation = new Customer(id);

        return events
                .filter(eventsFilter -> id.equals(eventsFilter.getAggregateRootId()))
                .flatMap(event -> Mono.fromRunnable(() -> operation.addEvent(event).apply()))
                .doOnTerminate(operation::markEventsAsCommitted)
                .then(Mono.just(operation));
    }

}