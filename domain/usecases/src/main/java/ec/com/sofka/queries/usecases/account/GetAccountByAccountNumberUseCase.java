package ec.com.sofka.queries.usecases.account;



import ec.com.sofka.account.Account;
import ec.com.sofka.aggregate.account.Customer;
import ec.com.sofka.gateway.IAccountRepository;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.generics.interfaces.IUseCaseGet;
import ec.com.sofka.generics.utils.QueryResponse;
import ec.com.sofka.queries.query.GetAccountQuery;
import ec.com.sofka.queries.responses.account.CreateAccountResponse;
import ec.com.sofka.queries.responses.account.GetAccountByNumResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public class GetAccountByAccountNumberUseCase implements IUseCaseGet<GetAccountQuery, GetAccountByNumResponse> {
    private final IAccountRepository accountRepository;
    private final IEventStore eventStore;

    public GetAccountByAccountNumberUseCase(IAccountRepository accountRepository, IEventStore eventStore) {
        this.accountRepository = accountRepository;
        this.eventStore = eventStore;
    }

    @Override
    public Mono<QueryResponse<GetAccountByNumResponse>> get(GetAccountQuery request) {
        Flux<DomainEvent> events = eventStore.findAggregate(request.getAggregateId());

        return Customer.from(request.getAggregateId(), events)
                .flatMap(customer -> {
                    Account account = customer.getAccount();
                    if (account == null) {
                        return Mono.error(new RuntimeException("Account not found"));
                    }
                    return Mono.just(
                            QueryResponse.ofSingle(new GetAccountByNumResponse(account,
                                    new CreateAccountResponse(
                                            request.getAggregateId(),
                                            account.getAccountNumber().getValue(),
                                            account.getOwnerName().getValue(),
                                            account.getType().getValue(),
                                            account.getBalance().getValue()
                                    )))
                    );
                });
    }
}
