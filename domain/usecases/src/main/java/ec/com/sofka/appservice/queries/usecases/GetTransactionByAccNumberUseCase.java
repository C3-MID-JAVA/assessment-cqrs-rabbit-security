package ec.com.sofka.appservice.queries.usecases;

import ec.com.sofka.aggregate.Customer;
import ec.com.sofka.appservice.gateway.IAccountRepository;
import ec.com.sofka.appservice.queries.query.GetByElementQuery;
import ec.com.sofka.appservice.queries.query.GetByQuery;
import ec.com.sofka.appservice.queries.responses.TransactionResponse;
import ec.com.sofka.appservice.gateway.IEventStore;
import ec.com.sofka.generics.interfaces.IUseCase;
import ec.com.sofka.appservice.gateway.ITransactionRepository;
import ec.com.sofka.generics.interfaces.IUseCaseGet;
import ec.com.sofka.generics.interfaces.IUseCaseGetBy;
import ec.com.sofka.generics.utils.QueryResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;

public class GetTransactionByAccNumberUseCase implements IUseCaseGetBy<GetByElementQuery, TransactionResponse> {

    private final ITransactionRepository repository;
    private final IEventStore eventRepository;
    private final IAccountRepository accountRepository;
    public GetTransactionByAccNumberUseCase(ITransactionRepository repository, IEventStore eventRepository, IAccountRepository accountRepository) {
        this.repository = repository;
        this.eventRepository = eventRepository;
        this.accountRepository = accountRepository;
    }


    @Override
    public Mono<QueryResponse<TransactionResponse>> get(GetByElementQuery request) {

        return accountRepository.findByAccountNumber(request.getElement())
                .switchIfEmpty(Mono.error(new NoSuchElementException("Transaction not found with account number: " + request.getElement())))
                .flatMap(account -> {
                    return repository.findAllTransactionById(account.getAccountId())
                            .map(transaction -> new TransactionResponse(
                                    transaction.getTransactionId(),
                                    transaction.getAccountId(),
                                    transaction.getTransactionCost(),
                                    transaction.getAmount(),
                                    transaction.getDate(),
                                    transaction.getType()
                            ))
                            .collectList()
                            .flatMap(transactions -> Mono.just(QueryResponse.ofMultiple(transactions)));
                });
    }
}
