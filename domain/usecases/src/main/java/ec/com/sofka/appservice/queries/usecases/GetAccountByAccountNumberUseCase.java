package ec.com.sofka.appservice.queries.usecases;

import ec.com.sofka.ConflictException;
import ec.com.sofka.aggregate.Customer;
import ec.com.sofka.appservice.queries.query.GetByQuery;
import ec.com.sofka.appservice.queries.responses.AccountResponse;
import ec.com.sofka.appservice.queries.query.GetByElementQuery;
import ec.com.sofka.appservice.gateway.IAccountRepository;
import ec.com.sofka.appservice.gateway.IEventStore;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.generics.interfaces.IUseCase;
import ec.com.sofka.generics.interfaces.IUseCaseGet;
import ec.com.sofka.generics.interfaces.IUseCaseGetBy;
import ec.com.sofka.generics.utils.QueryResponse;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class GetAccountByAccountNumberUseCase implements IUseCaseGetBy<GetByQuery, AccountResponse> {

    private final IAccountRepository repository;

    public GetAccountByAccountNumberUseCase(IAccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<QueryResponse<AccountResponse>> get(GetByQuery request) {

        return repository.findByAccountNumber(request.getElement())
                                        .switchIfEmpty(Mono.error(new ConflictException("Account not found by number account.")))
                                        .map(accountDTO -> new AccountResponse(
                                                accountDTO.getAccountId(),
                                                accountDTO.getAccountNumber(),
                                                accountDTO.getName(),
                                                accountDTO.getBalance(),
                                                accountDTO.getStatus()
                                        ))
                                        .map(QueryResponse::ofSingle);
    }
}