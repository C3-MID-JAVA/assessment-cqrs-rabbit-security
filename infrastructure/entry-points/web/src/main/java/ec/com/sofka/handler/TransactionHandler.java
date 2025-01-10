package ec.com.sofka.handler;

import ec.com.sofka.appservice.queries.usecases.GetAccountByIdUseCase;
import ec.com.sofka.appservice.queries.query.GetByElementQuery;
import ec.com.sofka.appservice.commands.usecases.CreateDepositUseCase;
import ec.com.sofka.appservice.commands.usecases.CreateWithDrawalUseCase;
import ec.com.sofka.appservice.queries.usecases.GetTransactionByAccNumberUseCase;
import ec.com.sofka.data.AccountReqByIdDTO;
import ec.com.sofka.data.TransactionRequestDTO;
import ec.com.sofka.data.TransactionResponseDTO;
import ec.com.sofka.mapper.TransactionDTOMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Component
public class TransactionHandler {

    private final CreateDepositUseCase createDepositUseCase;
    private final CreateWithDrawalUseCase createWithDrawalUseCase;
    private final GetTransactionByAccNumberUseCase getTransactionByIdUseCase;
    private final TransactionDTOMapper transactionMapper;
    private final GetAccountByIdUseCase getAccountByIdUseCase;


    public TransactionHandler(CreateDepositUseCase createDepositUseCase,
                              TransactionDTOMapper transactionMapper,GetAccountByIdUseCase getAccountByIdUseCase,
                              CreateWithDrawalUseCase createWithDrawalUseCase,
                              GetTransactionByAccNumberUseCase getTransactionByIdUseCase) {
        this.createDepositUseCase = createDepositUseCase;
        this.transactionMapper = transactionMapper;
        this.getAccountByIdUseCase = getAccountByIdUseCase;
        this.createWithDrawalUseCase = createWithDrawalUseCase;
        this.getTransactionByIdUseCase = getTransactionByIdUseCase;
    }

    public Mono<TransactionResponseDTO> createDeposit(TransactionRequestDTO transactionRequestDTO) {
        return createDepositUseCase.execute(transactionMapper.toCreateTransactionRequest(transactionRequestDTO))
                .map( transactionResponse -> transactionMapper.toTransactionResponseDTO(transactionResponse));
    }

    public Mono<TransactionResponseDTO> createWithDrawal(TransactionRequestDTO transactionRequestDTO) {
        return createWithDrawalUseCase.execute(transactionMapper.toCreateTransactionRequest(transactionRequestDTO))
                .map( transactionResponse -> transactionMapper.toTransactionResponseDTO(transactionResponse));
    }

    public Mono<TransactionResponseDTO> getTransactionByAccountNumber(AccountReqByIdDTO req) {
        GetByElementQuery request = new GetByElementQuery(req.getCustomerId(), req.getAccountNumber());

        return getTransactionByIdUseCase.get(request)
                .flatMap(queryResponse ->
                        Mono.justOrEmpty(queryResponse.getSingleResult()) // Acceder al TransactionResponse
                                .flatMap(transactionResponse ->
                                        getAccountByIdUseCase.get(new GetByElementQuery(transactionResponse.getCustomerId(),
                                                        transactionResponse.getAccountId()))
                                                .map(accountResponse -> TransactionDTOMapper.toTransactionResponseDTO(transactionResponse))
                                )
                );
    }

}
