package ec.com.sofka.queries.query.transaction;

import ec.com.sofka.aggregates.account.entities.transaction.Transaction;
import ec.com.sofka.gateway.TransactionRepository;
import ec.com.sofka.generics.interfaces.IUseCaseGet;
import ec.com.sofka.generics.shared.QueryResponse;
import ec.com.sofka.mapper.TransactionMapper;
import ec.com.sofka.queries.responses.TransactionResponse;
import reactor.core.publisher.Flux;


public class GetAllTransactionsUseCase implements IUseCaseGet<TransactionResponse> {
    private final TransactionRepository transactionRepository;

    public GetAllTransactionsUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Flux<QueryResponse<TransactionResponse>> get() {
        return getAll()
                .map(TransactionMapper::mapToResponseFromModel)
                .collectList()
                .flatMapMany(list -> {
                    QueryResponse<TransactionResponse> queryResponse = QueryResponse.ofMultiple(list);
                    return Flux.just(queryResponse);
                });
    }

    public Flux<Transaction> getAll() {
        return transactionRepository.findAll()
                .map(TransactionMapper::mapToModelFromDTO);

    }
}