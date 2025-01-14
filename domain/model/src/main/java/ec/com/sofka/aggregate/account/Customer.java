package ec.com.sofka.aggregate.account;

import ec.com.sofka.account.Account;
import ec.com.sofka.account.values.AccountId;
import ec.com.sofka.aggregate.account.events.AccountBalanceUpdated;
import ec.com.sofka.aggregate.account.events.AccountCreated;
import ec.com.sofka.aggregate.account.events.CardCreated;
import ec.com.sofka.aggregate.account.values.CustomerId;
import ec.com.sofka.card.Card;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.generics.utils.AggregateRoot;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class Customer extends AggregateRoot<CustomerId> {
    private Account account;
    private List<Card> cards = new ArrayList<>();

    public Customer() {
        super(new CustomerId());
        setSubscription(new CustomerHandler(this));
    }


    private Customer(final String id) {
        super(CustomerId.of(id));
        setSubscription(new CustomerHandler(this));
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public void createAccount(BigDecimal accountBalance, String accountNumber, String name, String accountType ) {
        addEvent(new AccountCreated( new AccountId().getValue(),accountNumber,accountBalance, name, accountType)).apply();

    }

    public void createCard(CardCreated createCardEv) {
        addEvent(createCardEv).apply();
    }

    public void updateAccountBalance(String id, String accountNumber, BigDecimal accountBalance, String ownerName, String accountType ) {
        addEvent(new AccountBalanceUpdated(id, accountNumber, accountBalance, ownerName, accountType)).apply();
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
