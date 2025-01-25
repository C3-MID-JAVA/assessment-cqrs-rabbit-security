package ec.com.sofka.account.queries.usecases;

import ec.com.sofka.account.queries.responses.AccountResponse;
import ec.com.sofka.account.queries.responses.AccountUserResponse;
import ec.com.sofka.gateway.AccountRepository;
import ec.com.sofka.generics.interfaces.IUseCaseEmptyGet;
import ec.com.sofka.generics.utils.QueryResponse;
import ec.com.sofka.user.queries.responses.UserResponse;
import reactor.core.publisher.Mono;

public class GetAllAccountsViewUseCase implements IUseCaseEmptyGet<AccountUserResponse> {
    private final AccountRepository accountRepository;

    public GetAllAccountsViewUseCase(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Mono<QueryResponse<AccountUserResponse>> get() {
        return accountRepository.getAllWithUsers()
                .map(accountDTO ->
                        new AccountUserResponse(
                                accountDTO.getId(),
                                accountDTO.getAccountNumber(),
                                accountDTO.getBalance(),
                                accountDTO.getUserId(),
                                new UserResponse(
                                        accountDTO.getUser().getId(),
                                        accountDTO.getUser().getName(),
                                        accountDTO.getUser().getDocumentId()
                                )))
                .collectList()
                .flatMap(accounts -> Mono.just(QueryResponse.ofMultiple(accounts)));
    }
}
