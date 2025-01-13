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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;

public class GetAccountByIdUseCase implements IUseCaseGet<GetByElementQuery, AccountResponse> {

    private final IAccountRepository repository;
    private final IEventStore eventRepository;
    public GetAccountByIdUseCase(IAccountRepository repository, IEventStore eventRepository) {
        this.repository = repository;
        this.eventRepository = eventRepository;
    }

    @Override
    public Mono<QueryResponse<AccountResponse>> get(GetByElementQuery request) {

        return repository.findById(request.getElement())
                .switchIfEmpty(Mono.error(new NoSuchElementException("Account not found by id.")))
                .map(
                        accountDTO -> new AccountResponse(
                                accountDTO.getAccountId(),
                                accountDTO.getAccountNumber(),
                                accountDTO.getName(),
                                accountDTO.getBalance(),
                                accountDTO.getStatus()
                        ))
                .map(QueryResponse::ofSingle);
    }

}
