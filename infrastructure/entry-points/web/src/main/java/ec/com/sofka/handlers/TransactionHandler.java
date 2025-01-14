package ec.com.sofka.handlers;


import ec.com.sofka.data.TransactionReqDTO;
import ec.com.sofka.mapper.TransactionDTOMapper;
import ec.com.sofka.commands.usecases.CreateTransactionUseCase;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class TransactionHandler {
    private final CreateTransactionUseCase createTransactionUseCase;

    public TransactionHandler(CreateTransactionUseCase createTransactionUseCase) {
        this.createTransactionUseCase = createTransactionUseCase;
    }

    public Mono<TransactionReqDTO> createTransaction(TransactionReqDTO transactionDTO) {
        return createTransactionUseCase.execute(TransactionDTOMapper.toTransactionCommand(transactionDTO))
                .doOnSuccess(transactionDTO1 -> System.out.println("HERE IS " + transactionDTO1.getDescription()))
                .map(TransactionDTOMapper::toReqDTO);
    }
}
