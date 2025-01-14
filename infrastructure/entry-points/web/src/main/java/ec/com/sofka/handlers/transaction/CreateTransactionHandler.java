package ec.com.sofka.handlers.transaction;

import ec.com.sofka.commands.usecases.transaction.CreateTransactionUseCase;
import ec.com.sofka.data.TransactionRequestDTO;
import ec.com.sofka.data.TransactionResponseDTO;
import ec.com.sofka.mapper.TransactionModelMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CreateTransactionHandler {
    private final CreateTransactionUseCase createTransactionUseCase;

    public CreateTransactionHandler(CreateTransactionUseCase createTransactionUseCase) {
        this.createTransactionUseCase = createTransactionUseCase;
    }

    public Mono<TransactionResponseDTO> save(TransactionRequestDTO transactionRequestDTO) {
        return createTransactionUseCase.execute(TransactionModelMapper.mapToRequest(transactionRequestDTO))
                .map(TransactionModelMapper::mapToDTO);

    }

}