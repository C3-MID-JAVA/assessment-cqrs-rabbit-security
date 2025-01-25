package ec.com.sofka.account.commands.usecases;


import ec.com.sofka.NotFoundException;
import ec.com.sofka.account.Account;
import ec.com.sofka.account.commands.CreateAccountCommand;
import ec.com.sofka.account.queries.responses.AccountResponse;
import ec.com.sofka.aggregate.customer.Customer;
import ec.com.sofka.aggregate.customer.events.EventsEnum;
import ec.com.sofka.aggregate.customer.events.UserCreated;
import ec.com.sofka.gateway.BusEvent;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.generics.interfaces.IUseCaseExecute;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class CreateAccountUseCase implements IUseCaseExecute<CreateAccountCommand, AccountResponse> {
    private final IEventStore repository;
    private final BusEvent busEvent;

    public CreateAccountUseCase(IEventStore repository, BusEvent busEvent) {
        this.repository = repository;
        this.busEvent = busEvent;
    }

    public Mono<AccountResponse> execute(CreateAccountCommand cmd) {
        Mono<UserCreated> userCreatedEvent = repository.findAllAggregateByEvent("customer", EventsEnum.USER_CREATED.name())
                .switchIfEmpty(Mono.empty())
                .map(event -> (UserCreated) event)
                .filter(event -> event.getId().equals(cmd.getUserId()))
                .single();

        return userCreatedEvent.flatMap(userCreated -> {
            Flux<DomainEvent> events = repository.findAggregate(userCreated.getAggregateRootId(), "customer");

            return Customer.from(userCreated.getAggregateRootId(), events)
                    .flatMap(customer -> {
                        if (customer.getUser() == null) {
                            return Mono.error(new NotFoundException("User not found"));
                        }

                        String generatedAccountNumber = UUID.randomUUID().toString().substring(0, 8);

                        customer.createAccount(BigDecimal.valueOf(0), generatedAccountNumber, customer.getUser().getId().getValue());

                        Account newAccount = customer.getAccounts().stream()
                                .filter(account -> account.getAccountNumber().getValue().equals(generatedAccountNumber))
                                .findFirst()
                                .orElseThrow(() -> new IllegalStateException("Account creation failed"));

                        customer.getUncommittedEvents()
                                .stream()
                                .map(repository::save)
                                .forEach(busEvent::sendEventAccountCreated);

                        customer.markEventsAsCommitted();

                        return Mono.just(new AccountResponse(
                                newAccount.getId().getValue(),
                                newAccount.getAccountNumber().getValue(),
                                newAccount.getBalance().getValue(),
                                newAccount.getUserId().getValue(),
                                customer.getId().getValue()
                        ));
                    });
        });
    }
}
