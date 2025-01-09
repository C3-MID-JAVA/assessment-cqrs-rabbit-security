package ec.com.sofka.appservice.queries.usecases;

import ec.com.sofka.aggregate.Customer;
import ec.com.sofka.appservice.queries.query.GetByElementQuery;
import ec.com.sofka.appservice.queries.responses.TransactionResponse;
import ec.com.sofka.appservice.gateway.IEventStore;
import ec.com.sofka.generics.interfaces.IUseCase;
import ec.com.sofka.appservice.gateway.ITransactionRepository;
import ec.com.sofka.generics.interfaces.IUseCaseGet;
import ec.com.sofka.generics.utils.QueryResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;

public class GetTransactionByAccNumberUseCase implements IUseCaseGet<GetByElementQuery, TransactionResponse> {

    private final ITransactionRepository repository;
    private final IEventStore eventRepository;
    public GetTransactionByAccNumberUseCase(ITransactionRepository repository, IEventStore eventRepository) {
        this.repository = repository;
        this.eventRepository = eventRepository;
    }


    @Override
    public Mono<QueryResponse<TransactionResponse>> get(GetByElementQuery request) {
        return eventRepository.findAggregate(request.getAggregateId())
                .collectList()
                .flatMap(eventsList -> {
                    if (eventsList.isEmpty()) {
                        return Mono.error(new NoSuchElementException("No events found for the given aggregate ID."));
                    }

                    // Reconstruir el agregado Customer usando los eventos
                    return Customer.from(request.getAggregateId(), Flux.fromIterable(eventsList))
                            .flatMap(customer -> {
                                return Mono.justOrEmpty(
                                                customer.getTransactions().stream()
                                                        .filter(transaction -> transaction.getId().getValue().equals(request.getElement()))
                                                        .findFirst()
                                        )
                                        .switchIfEmpty(Mono.error(new NoSuchElementException("Transaction not found with id: " + request.getElement())))
                                        .map(transaction -> {
                                            TransactionResponse transactionResponse = new TransactionResponse(
                                                    request.getAggregateId(),
                                                    transaction.getId().getValue(),
                                                    transaction.getAccountId().getValue(),
                                                    transaction.getTransactionCost().getValue(),
                                                    transaction.getAmount().getValue(),
                                                    transaction.getDate().getValue(),
                                                    transaction.getType()
                                            );
                                            return QueryResponse.ofSingle(transactionResponse);
                                        });
                            });
                });
    }


}
