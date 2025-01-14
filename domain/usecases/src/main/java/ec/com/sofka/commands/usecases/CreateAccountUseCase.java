package ec.com.sofka.commands.usecases;


import ec.com.sofka.account.Account;
import ec.com.sofka.aggregate.account.Customer;
import ec.com.sofka.card.Card;
import ec.com.sofka.commands.CreateAccountCommand;
import ec.com.sofka.gateway.BusEvent;
import ec.com.sofka.gateway.IAccountRepository;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.generics.interfaces.IUseCaseExecute;
import ec.com.sofka.queries.responses.account.CreateAccountResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


public class CreateAccountUseCase implements IUseCaseExecute<CreateAccountCommand, CreateAccountResponse> {
    private final IEventStore eventRepository;
    private final BusEvent busEvent;

    public CreateAccountUseCase(IEventStore repository, BusEvent busEvent) {
        this.eventRepository = repository;
        this.busEvent = busEvent;
    }


    @Override
    public Mono<CreateAccountResponse> execute(CreateAccountCommand cmd) {
        return eventRepository.findAllAggregate("customer")
                .collectList()
                .flatMap(events -> {
                    Optional<Customer> existingCustomer = events.stream()
                            .map(event -> Customer.from(event.getAggregateRootId(), Flux.fromIterable(events)).block())
                            .filter(customer -> customer != null &&
                                    customer.getAccount() != null &&
                                    customer.getAccount().getAccountNumber().getValue().equals(cmd.getAccountNumber()))
                            .findFirst();

                    if (existingCustomer.isPresent()) {
                        return Mono.error(new RuntimeException(
                                "Account with number " + cmd.getAccountNumber() + " already exists"));
                    }

                    Customer newCustomer = new Customer();
                    newCustomer.createAccount(
                            cmd.getBalance(),
                            cmd.getAccountNumber(),
                            cmd.getOwnerName(),
                            cmd.getAccountType()
                    );


                    newCustomer.getUncommittedEvents()
                            .stream()
                            .map(eventRepository::save)
                            .forEach(busEvent::sendEventAccountCreated);

                    newCustomer.markEventsAsCommitted();


                    return Mono.just(new CreateAccountResponse(
                            newCustomer.getId().getValue(),
                            newCustomer.getAccount().getAccountNumber().getValue(),
                            newCustomer.getAccount().getOwnerName().getValue(),
                            newCustomer.getAccount().getType().getValue(),
                            newCustomer.getAccount().getBalance().getValue()
                    ));
                });
    }
}

