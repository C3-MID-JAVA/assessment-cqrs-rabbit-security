package ec.com.sofka.queries.query.transaction;

import ec.com.sofka.aggregates.account.entities.transaction.Transaction;
import ec.com.sofka.exceptions.RecordNotFoundException;
import ec.com.sofka.gateway.TransactionRepository;
import ec.com.sofka.gateway.busMessage.ErrorBusMessage;
import ec.com.sofka.generics.interfaces.IUseCaseGetElement;
import ec.com.sofka.generics.shared.PropertyQuery;
import ec.com.sofka.generics.shared.QueryResponse;
import ec.com.sofka.mapper.TransactionMapper;
import ec.com.sofka.model.ErrorMessage;
import ec.com.sofka.queries.responses.TransactionResponse;
import reactor.core.publisher.Mono;

public class FindTransactionByIdUseCase implements IUseCaseGetElement<PropertyQuery, TransactionResponse> {
    private final TransactionRepository transactionRepository;
    private final ErrorBusMessage errorBusMessage;

    public FindTransactionByIdUseCase(TransactionRepository transactionRepository, ErrorBusMessage errorBusMessage) {
        this.transactionRepository = transactionRepository;
        this.errorBusMessage = errorBusMessage;
    }

    @Override
    public Mono<QueryResponse<TransactionResponse>> get(PropertyQuery request) {

        return getById(request.getProperty())
                .map(TransactionMapper::mapToResponseFromModel)
                .flatMap(transactionResponse -> Mono.just(QueryResponse.ofSingle(transactionResponse)));
    }

    public Mono<Transaction> getById(String id) {
        return transactionRepository.findById(id)
                .switchIfEmpty(Mono.defer(() -> {
                    errorBusMessage.sendMsg(new ErrorMessage("Transaction not found", "Get Transaction by Id"));
                    return Mono.error(new RecordNotFoundException("Transaction not found."));
                }))
                .map(TransactionMapper::mapToModelFromDTO);

    }

}