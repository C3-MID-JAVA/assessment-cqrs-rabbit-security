package ec.com.sofka.handlers.transaction;

import ec.com.sofka.data.TransactionResponseDTO;
import ec.com.sofka.mapper.TransactionModelMapper;
import ec.com.sofka.queries.query.transaction.GetAllTransactionsUseCase;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class GetAllTransactionsHandler {
    private final GetAllTransactionsUseCase getAllTransactionsUseCase;

    public GetAllTransactionsHandler(GetAllTransactionsUseCase getAllTransactionsUseCase) {
        this.getAllTransactionsUseCase = getAllTransactionsUseCase;
    }

    public Flux<TransactionResponseDTO> getAll() {
        return getAllTransactionsUseCase.get()
                .flatMap(queryResponse -> Flux.fromIterable(queryResponse.getMultipleResults()))
                .map(TransactionModelMapper::mapToDTO);

    }

}