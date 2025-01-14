package ec.com.sofka.handler;

import ec.com.sofka.appservice.queries.usecases.GetAccountByIdUseCase;
import ec.com.sofka.appservice.queries.query.GetByElementQuery;
import ec.com.sofka.appservice.commands.usecases.CreateDepositUseCase;
import ec.com.sofka.appservice.commands.usecases.CreateWithDrawalUseCase;
import ec.com.sofka.appservice.queries.usecases.GetTransactionByAccNumberUseCase;
import ec.com.sofka.data.AccountReqByElementDTO;
import ec.com.sofka.data.TransactionRequestDTO;
import ec.com.sofka.data.TransactionResponseDTO;
import ec.com.sofka.mapper.TransactionDTOMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;


@Component
public class TransactionHandler {

    private final CreateDepositUseCase createDepositUseCase;
    private final CreateWithDrawalUseCase createWithDrawalUseCase;
    private final GetTransactionByAccNumberUseCase getTransactionByAccNumberUseCase;
    private final TransactionDTOMapper transactionMapper;
    private final GetAccountByIdUseCase getAccountByIdUseCase;


    public TransactionHandler(CreateDepositUseCase createDepositUseCase,
                              TransactionDTOMapper transactionMapper,GetAccountByIdUseCase getAccountByIdUseCase,
                              CreateWithDrawalUseCase createWithDrawalUseCase,
                              GetTransactionByAccNumberUseCase getTransactionByAccNumberUseCase) {
        this.createDepositUseCase = createDepositUseCase;
        this.transactionMapper = transactionMapper;
        this.getAccountByIdUseCase = getAccountByIdUseCase;
        this.createWithDrawalUseCase = createWithDrawalUseCase;
        this.getTransactionByAccNumberUseCase = getTransactionByAccNumberUseCase;
    }

    public Mono<TransactionResponseDTO> createDeposit(TransactionRequestDTO transactionRequestDTO) {
        return createDepositUseCase.execute(transactionMapper.toCreateTransactionRequest(transactionRequestDTO))
                .map( transactionResponse -> transactionMapper.toTransactionResponseDTO(transactionResponse));
    }

    public Mono<TransactionResponseDTO> createWithDrawal(TransactionRequestDTO transactionRequestDTO) {
        return createWithDrawalUseCase.execute(transactionMapper.toCreateTransactionRequest(transactionRequestDTO))
                .map( transactionResponse -> transactionMapper.toTransactionResponseDTO(transactionResponse));
    }

    public Mono<List<TransactionResponseDTO>> getTransactionByAccountNumber(AccountReqByElementDTO req) {
        GetByElementQuery request = new GetByElementQuery(req.getCustomerId(), req.getAccountNumber());

        return getTransactionByAccNumberUseCase.get(request)
                .map(queryResponse ->
                        queryResponse.getMultipleResults()
                                .stream()
                                .map(TransactionDTOMapper::toListTransactionResponseDTO)
                                .toList() // Convierte el Stream en una lista
                );
    }
}
