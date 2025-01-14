package ec.com.sofka.queries.query.transcationType;

import ec.com.sofka.aggregates.account.entities.transactionType.TransactionType;
import ec.com.sofka.exceptions.RecordNotFoundException;
import ec.com.sofka.gateway.TransactionTypeRepository;
import ec.com.sofka.gateway.busMessage.ErrorBusMessage;
import ec.com.sofka.generics.interfaces.IUseCaseGetElement;
import ec.com.sofka.generics.shared.QueryResponse;
import ec.com.sofka.mapper.TransactionTypeMapper;
import ec.com.sofka.model.ErrorMessage;
import ec.com.sofka.generics.shared.PropertyQuery;
import ec.com.sofka.queries.responses.TransactionTypeResponse;
import reactor.core.publisher.Mono;

public class FindTransactionTypeByIdUseCase implements IUseCaseGetElement<PropertyQuery, TransactionTypeResponse> {
    private final TransactionTypeRepository transactionTypeRepository;
    private final ErrorBusMessage errorBusMessage;

    public FindTransactionTypeByIdUseCase(TransactionTypeRepository transactionTypeRepository, ErrorBusMessage errorBusMessage) {
        this.transactionTypeRepository = transactionTypeRepository;
        this.errorBusMessage = errorBusMessage;
    }

    @Override
    public Mono<QueryResponse<TransactionTypeResponse>> get(PropertyQuery request) {

        return getById(request.getProperty())
                .map(TransactionTypeMapper::mapToResponseFromModel)
                .flatMap(transactionTypeResponse -> Mono.just(QueryResponse.ofSingle(transactionTypeResponse)));

    }

    public Mono<TransactionType> getById(String id) {
        return transactionTypeRepository.findById(id)
                .switchIfEmpty(Mono.defer(() -> {
                    errorBusMessage.sendMsg(new ErrorMessage("Transaction Type not found", "Get TransactionType by Id"));
                    return Mono.error(new RecordNotFoundException("Transaction Type not found."));
                }))
                .map(TransactionTypeMapper::mapToModelFromDTO);

    }

}