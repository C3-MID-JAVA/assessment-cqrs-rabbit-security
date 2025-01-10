package ec.com.sofka.appservice.queries.usecases;

import ec.com.sofka.ConflictException;
import ec.com.sofka.aggregate.Customer;
import ec.com.sofka.appservice.queries.responses.AccountResponse;
import ec.com.sofka.appservice.queries.query.GetByElementQuery;
import ec.com.sofka.appservice.gateway.IAccountRepository;
import ec.com.sofka.appservice.gateway.IEventStore;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.generics.interfaces.IUseCase;
import ec.com.sofka.generics.interfaces.IUseCaseGet;
import ec.com.sofka.generics.utils.QueryResponse;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class GetAccountByAccountNumberUseCase implements IUseCaseGet<GetByElementQuery, AccountResponse> {

    private final IAccountRepository repository;
    private final IEventStore eventRepository;

    public GetAccountByAccountNumberUseCase(IAccountRepository repository, IEventStore eventRepository) {
        this.repository = repository;
        this.eventRepository = eventRepository;
    }

    @Override
    public Mono<QueryResponse<AccountResponse>> get(GetByElementQuery request) {

        return eventRepository.findAggregate(request.getAggregateId())
                .collectList()
                .flatMap(events -> {
                    if (events.isEmpty()) {
                        return Mono.error(new ConflictException("No events found for the given aggregate ID."));
                    }
                    return Customer.from(request.getAggregateId(), Flux.fromIterable(events))
                            .flatMap(customer -> {
                                return repository.findByAccountNumber(customer.getAccount().getAccountNumber().getValue())
                                        .switchIfEmpty(Mono.error(new ConflictException("Account not found by number account.")))
                                        .map(accountDTO -> new AccountResponse(
                                                request.getAggregateId(),
                                                accountDTO.getAccountId(),
                                                accountDTO.getAccountNumber(),
                                                accountDTO.getName(),
                                                accountDTO.getBalance(),
                                                accountDTO.getStatus()
                                        ))
                                        .map(QueryResponse::ofSingle);
                            });
                });
    }

}