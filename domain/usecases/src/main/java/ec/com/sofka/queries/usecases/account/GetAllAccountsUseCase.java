package ec.com.sofka.queries.usecases.account;

import ec.com.sofka.aggregate.account.Customer;
import ec.com.sofka.gateway.IAccountRepository;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.generics.interfaces.IUseCaseGetEmpty;
import ec.com.sofka.generics.utils.QueryResponse;
import ec.com.sofka.queries.responses.account.CreateAccountResponse;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.stream.Collectors;

public class GetAllAccountsUseCase implements IUseCaseGetEmpty<CreateAccountResponse> {

    private final IAccountRepository accountRepository;

    public GetAllAccountsUseCase(IAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Flux<QueryResponse<CreateAccountResponse>> get() {
        return accountRepository.findAllAccounts()
                .map(accountDTO -> new CreateAccountResponse(
                               null,
                        accountDTO.getAccountNumber(),
                        accountDTO.getOwnerName(),
                        accountDTO.getAccountType(),
                        accountDTO.getBalance()
                        )
                )
                .collectList()
                .flatMapMany(createAccountResponse ->
                        Flux.just(QueryResponse.ofMultiple(createAccountResponse)));

    }
}
