package ec.com.sofka.appservice.queries.usecases;

import ec.com.sofka.aggregate.Customer;
import ec.com.sofka.aggregate.events.AccountCreated;
import ec.com.sofka.aggregate.events.AccountUpdated;
import ec.com.sofka.appservice.queries.responses.AccountResponse;
import ec.com.sofka.appservice.gateway.IBusEvent;
import ec.com.sofka.appservice.gateway.IAccountRepository;
import ec.com.sofka.appservice.gateway.IEventStore;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.generics.interfaces.IUseCaseGetEmpty;
import ec.com.sofka.generics.utils.QueryResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.stream.Collectors;

public class GetAllAccountsUseCase implements IUseCaseGetEmpty<AccountResponse> {

    private final IAccountRepository repository;
    private final IBusEvent busMessage;

    private final IEventStore eventRepository;


    public GetAllAccountsUseCase(IAccountRepository repository, IBusEvent busMessage, IEventStore eventRepository) {
        this.repository = repository;
        this.busMessage = busMessage;
        this.eventRepository = eventRepository;
    }


    @Override
    public Mono<QueryResponse<AccountResponse>> get() {
        return eventRepository.findAllAggregates()
                .collectList()
                .flatMap(events -> {
                    // Filtrar eventos por tipo de cuenta
                    Map<String, DomainEvent> mapLatestEvents = events.stream()
                            .filter(event -> event instanceof AccountCreated || event instanceof AccountUpdated)
                            .collect(Collectors.toMap(
                                    DomainEvent::getAggregateRootId,
                                    event -> event,
                                    (existing, replacement) -> existing.getVersion() >= replacement.getVersion() ? existing : replacement
                            ));

                    Flux<DomainEvent> latestEventsFlux = Flux.fromIterable(mapLatestEvents.values());

                    return latestEventsFlux
                            .flatMap(event -> {
                                return Customer.from(event.getAggregateRootId(), Flux.fromIterable(mapLatestEvents.values()));
                            })
                            .map(customer -> new AccountResponse(
                                    customer.getId().getValue(),
                                    customer.getAccount().getId().getValue(),
                                    customer.getAccount().getAccountNumber().getValue(),
                                    customer.getAccount().getOwner().getValue(),
                                    customer.getAccount().getBalance().getValue(),
                                    customer.getAccount().getStatus().getValue()
                            ))
                            .collectList()
                            .flatMap(responses -> Mono.just(QueryResponse.ofMultiple(responses)));
                });
    }


}
