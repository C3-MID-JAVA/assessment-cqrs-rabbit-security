package ec.com.sofka.handlers;

import ec.com.sofka.commands.usecases.CreateAccountUseCase;
import ec.com.sofka.queries.usecases.account.GetAllAccountsUseCase;
import ec.com.sofka.data.AccountReqDTO;
import ec.com.sofka.commands.CreateAccountCommand;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class AccountHandler {
    private final CreateAccountUseCase createAccountUseCase;
    private final GetAllAccountsUseCase getAllAccountsUseCase;

    public AccountHandler(CreateAccountUseCase createAccountUseCase, GetAllAccountsUseCase getAllAccountsUseCase) {
        this.createAccountUseCase = createAccountUseCase;
        this.getAllAccountsUseCase = getAllAccountsUseCase;
    }

    public Mono<AccountReqDTO> createAccount(AccountReqDTO request) {

        return createAccountUseCase.execute(
                new CreateAccountCommand(
                        request.getAccountBalance(),
                        request.getAccountNumber(),
                        request.getAccountOwner(),
                        request.getAccountType()
                )
        ).map(res -> new AccountReqDTO(res.getAccountNumber(),
                res.getAccountBalance(), res.getAccountType(), res.getOwnerName(), res.getCustomerId()));
    }

    public Flux<AccountReqDTO> getAllAccounts() {

        return getAllAccountsUseCase.get()
                .flatMap(res ->
                        Flux.fromIterable(res.getMultipleResults()
                                .stream()
                                .map(createAccountResponse ->
                                        new AccountReqDTO(
                                                createAccountResponse.getAccountNumber(),
                                                createAccountResponse.getAccountBalance(),
                                                createAccountResponse.getAccountType(),
                                                createAccountResponse.getOwnerName(),
                                                createAccountResponse.getCustomerId()
                                        )).toList()));
    }
}
