package ec.com.sofka.account.queries.usecases;

import ec.com.sofka.account.queries.query.GetAllByUserIdQuery;
import ec.com.sofka.account.queries.responses.AccountResponse;
import ec.com.sofka.gateway.AccountRepository;
import ec.com.sofka.generics.interfaces.IUseCaseGet;
import ec.com.sofka.generics.utils.QueryResponse;
import reactor.core.publisher.Mono;

public class GetAllByUserIdViewUseCase implements IUseCaseGet<GetAllByUserIdQuery, AccountResponse> {
    private final AccountRepository accountRepository;

    public GetAllByUserIdViewUseCase(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Mono<QueryResponse<AccountResponse>> get(GetAllByUserIdQuery request) {
        return accountRepository.getAllByUserId(request.getUserId())
                .map(account ->
                        new AccountResponse(account.getId(), account.getAccountNumber(), account.getBalance(), account.getUserId())
                )
                .collectList()
                .flatMap(accounts -> Mono.just(QueryResponse.ofMultiple(accounts)));
    }
}
