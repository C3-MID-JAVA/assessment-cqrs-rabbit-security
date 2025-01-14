package ec.com.sofka.account.queries.usecases;

import ec.com.sofka.NotFoundException;
import ec.com.sofka.account.queries.query.GetAccountByNumberQuery;
import ec.com.sofka.account.queries.responses.AccountResponse;
import ec.com.sofka.gateway.AccountRepository;
import ec.com.sofka.generics.interfaces.IUseCaseGet;
import ec.com.sofka.generics.utils.QueryResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public class GetAccountByNumberViewUseCase implements IUseCaseGet<GetAccountByNumberQuery, AccountResponse> {
    private final AccountRepository accountRepository;

    public GetAccountByNumberViewUseCase(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Mono<QueryResponse<AccountResponse>> get(GetAccountByNumberQuery query) {
        return accountRepository.findByAccountNumber(query.getAccountNumber())
                .switchIfEmpty(Mono.error(new NotFoundException("Account not found")))
                .map(accountDTO -> QueryResponse.ofSingle(
                        new AccountResponse(
                                accountDTO.getId(),
                                accountDTO.getAccountNumber(),
                                accountDTO.getBalance(),
                                accountDTO.getUserId()
                        )
                ));
    }
}

