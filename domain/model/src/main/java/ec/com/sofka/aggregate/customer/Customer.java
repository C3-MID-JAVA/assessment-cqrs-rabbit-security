package ec.com.sofka.aggregate.customer;

import ec.com.sofka.account.Account;
import ec.com.sofka.account.values.AccountId;
import ec.com.sofka.aggregate.customer.events.AccountBalanceUpdated;
import ec.com.sofka.aggregate.customer.events.AccountCreated;
import ec.com.sofka.aggregate.customer.events.UserCreated;
import ec.com.sofka.aggregate.customer.values.CustomerId;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.generics.utils.AggregateRoot;
import ec.com.sofka.user.User;
import ec.com.sofka.user.values.UserId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Customer extends AggregateRoot<CustomerId> {
    private User user;
    private List<Account> accounts = new ArrayList<>();

    public Customer() {
        super(new CustomerId());
        setSubscription(new CustomerHandler(this));
    }

    private Customer(final String id) {
        super(CustomerId.of(id));
        setSubscription(new CustomerHandler(this));
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void createAccount(BigDecimal balance, String accountNumber, String userId) {
        addEvent(new AccountCreated(new AccountId().getValue(), accountNumber, balance, userId)).apply();
    }

    public void updateAccountBalance(String id, BigDecimal balance, String accountNumber, String userId) {
        addEvent(new AccountBalanceUpdated(id, accountNumber, balance, userId)).apply();
    }

    public void createUser(String name, String documentId) {
        addEvent(new UserCreated(new UserId().getValue(), name, documentId)).apply();
    }

    public static Mono<Customer> from(final String id, Flux<DomainEvent> events) {
        Customer customer = new Customer(id);
        return events
                .filter(eventsFilter -> id.equals(eventsFilter.getAggregateRootId()))
                .concatMap(event -> Mono.just(event)
                        .doOnNext(e -> customer.addEvent(e).apply())
                )
                .doOnTerminate(customer::markEventsAsCommitted)
                .then(Mono.just(customer));
    }
}
