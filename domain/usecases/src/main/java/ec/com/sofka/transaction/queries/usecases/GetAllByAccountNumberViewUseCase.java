package ec.com.sofka.transaction.queries.usecases;

import ec.com.sofka.NotFoundException;
import ec.com.sofka.gateway.AccountRepository;
import ec.com.sofka.gateway.TransactionRepository;
import ec.com.sofka.generics.interfaces.IUseCaseGet;
import ec.com.sofka.generics.utils.QueryResponse;
import ec.com.sofka.transaction.queries.query.GetAllByAccountNumberQuery;
import ec.com.sofka.transaction.queries.responses.TransactionResponse;
import reactor.core.publisher.Mono;


public class GetAllByAccountNumberViewUseCase implements IUseCaseGet<GetAllByAccountNumberQuery, TransactionResponse> {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public GetAllByAccountNumberViewUseCase(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public Mono<QueryResponse<TransactionResponse>> get(GetAllByAccountNumberQuery cmd) {
        return accountRepository.findByAccountNumber(cmd.getAccountNumber())
                .switchIfEmpty(Mono.error(new NotFoundException("Account not found")))
                .flatMap(accountDTO -> {
                    return transactionRepository.getAllByAccountId(accountDTO.getId())
                            .map(transactionDTO -> new TransactionResponse(
                                    transactionDTO.getId(),
                                    transactionDTO.getAmount(),
                                    transactionDTO.getFee(),
                                    transactionDTO.getFinalAmount(),
                                    transactionDTO.getType(),
                                    transactionDTO.getTimestamp(),
                                    transactionDTO.getAccountId()
                            ))
                            .collectList()
                            .flatMap(transactions -> Mono.just(QueryResponse.ofMultiple(transactions)));
                });
    }
}