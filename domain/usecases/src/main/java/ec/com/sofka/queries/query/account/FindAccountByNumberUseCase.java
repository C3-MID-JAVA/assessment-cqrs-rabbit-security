package ec.com.sofka.queries.query.account;

import ec.com.sofka.aggregates.account.entities.account.Account;
import ec.com.sofka.exceptions.RecordNotFoundException;
import ec.com.sofka.gateway.AccountRepository;
import ec.com.sofka.gateway.busMessage.ErrorBusMessage;
import ec.com.sofka.generics.interfaces.IUseCaseGetElement;
import ec.com.sofka.generics.shared.QueryResponse;
import ec.com.sofka.mapper.AccountMapper;
import ec.com.sofka.model.ErrorMessage;
import ec.com.sofka.generics.shared.PropertyQuery;
import ec.com.sofka.queries.responses.AccountResponse;
import reactor.core.publisher.Mono;

public class FindAccountByNumberUseCase implements IUseCaseGetElement<PropertyQuery, AccountResponse> {
    private final AccountRepository accountRepository;
    private final ErrorBusMessage errorBusMessage;

    public FindAccountByNumberUseCase(AccountRepository accountRepository, ErrorBusMessage errorBusMessage) {
        this.accountRepository = accountRepository;
        this.errorBusMessage = errorBusMessage;
    }

    @Override
    public Mono<QueryResponse<AccountResponse>> get(PropertyQuery request) {

        return getByNumberAccount(request.getProperty())
                .map(AccountMapper::mapToResponseFromModel)
                .flatMap(accountResponse -> Mono.just(QueryResponse.ofSingle(accountResponse)));

    }

    public Mono<Account> getByNumberAccount(String numberAccount) {
        return accountRepository.findByNumber(numberAccount)
                .switchIfEmpty(Mono.defer(() -> {
                    errorBusMessage.sendMsg(new ErrorMessage("Account not found", "Get Account by NumberAccount"));
                    return Mono.error(new RecordNotFoundException("Account not found."));
                }))
                .map(AccountMapper::mapToModelFromDTO);
    }

}