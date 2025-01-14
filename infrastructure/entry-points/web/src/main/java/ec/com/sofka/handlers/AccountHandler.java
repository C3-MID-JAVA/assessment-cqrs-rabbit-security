
package ec.com.sofka.handlers;

import ec.com.sofka.commands.CreateAccountCommand;
import ec.com.sofka.commands.usecases.CreateAccountUseCase;
import ec.com.sofka.commands.usecases.DeleteAccountUseCase;
import ec.com.sofka.commands.usecases.UpdateAccountUseCase;
import ec.com.sofka.data.RequestDTO;
import ec.com.sofka.data.ResponseDTO;
import ec.com.sofka.queries.query.GetAccountQuery;
import ec.com.sofka.commands.UpdateAccountCommand;
import ec.com.sofka.queries.responses.GetAccountResponse;
import ec.com.sofka.queries.usecases.GetAccountByNumberUseCase;
import ec.com.sofka.queries.usecases.GetAllAccountsUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class AccountHandler {
    private final CreateAccountUseCase createAccountUseCase;
    private final GetAllAccountsUseCase getAllAccountsUseCase;
    private final GetAccountByNumberUseCase getAccountByNumberUseCase;
    private final UpdateAccountUseCase updateAccountUseCase;
    private final DeleteAccountUseCase deleteAccountUseCase;

    public AccountHandler(CreateAccountUseCase createAccountUseCase, GetAllAccountsUseCase getAllAccountsUseCase, GetAccountByNumberUseCase getAccountByNumberUseCase, UpdateAccountUseCase updateAccountUseCase, DeleteAccountUseCase deleteAccountUseCase) {
        this.createAccountUseCase = createAccountUseCase;
        this.getAllAccountsUseCase = getAllAccountsUseCase;
        this.getAccountByNumberUseCase = getAccountByNumberUseCase;
        this.updateAccountUseCase = updateAccountUseCase;
        this.deleteAccountUseCase = deleteAccountUseCase;
    }

    public Mono<ResponseDTO> createAccount(RequestDTO request) {
        return createAccountUseCase.execute(
                new CreateAccountCommand(
                        request.getAccountNum(),
                        request.getName(),
                        request.getBalance(),
                        request.getIdUser()
                )).map(response -> new ResponseDTO(
                response.getCustomerId(),
                response.getAccountId(),
                response.getName(),
                response.getAccountNumber(),
                response.getBalance(),
                response.getStatus()
        ));
    }

    public Flux<ResponseDTO> getAllAccounts() {
        return getAllAccountsUseCase.get(new GetAccountQuery())
                .flatMapMany(response -> Flux.fromIterable(response.getMultipleResults()))
                .map(accountResponse -> new ResponseDTO(
                        accountResponse.getCustomerId(),
                        accountResponse.getAccountId(),
                        accountResponse.getName(),
                        accountResponse.getAccountNumber(),
                        accountResponse.getBalance(),
                        accountResponse.getStatus()
                )).switchIfEmpty(Mono.empty());
    }

public Mono<ResponseDTO> getAccountByNumber(RequestDTO request) {
    return getAccountByNumberUseCase.get(
            new GetAccountQuery(
                    request.getCustomerId(),
                    request.getAccountNum()
            )).map(queryResponse -> {
        GetAccountResponse response = queryResponse.getSingleResult().get();
        return new ResponseDTO(
                response.getCustomerId(),
                response.getAccountId(),
                response.getName(),
                response.getAccountNumber(),
                response.getBalance(),
                response.getStatus()
        );
    }).switchIfEmpty(Mono.empty());
}
    public Mono<ResponseDTO> updateAccount(RequestDTO request) {
        return updateAccountUseCase.execute(
                new UpdateAccountCommand(
                        request.getCustomerId(),
                        request.getBalance(),
                        request.getAccountNum(),
                        request.getName(),
                        request.getStatus(),
                        request.getIdUser()
                )).map(response -> new ResponseDTO(
                response.getCustomerId(),
                response.getAccountId(),
                response.getName(),
                response.getAccountNumber(),
                response.getBalance(),
                response.getStatus()
        )).switchIfEmpty(Mono.empty());
    }

    public Mono<ResponseDTO> deleteAccount(RequestDTO request) {
        return deleteAccountUseCase.execute(
                new UpdateAccountCommand(
                        request.getCustomerId(),
                        request.getBalance(),
                        request.getAccountNum(),
                        request.getName(),
                        request.getStatus(),
                        request.getIdUser()
                )).map(response -> new ResponseDTO(
                response.getCustomerId(),
                response.getAccountId(),
                response.getName(),
                response.getAccountNumber(),
                response.getBalance(),
                response.getStatus()
        )).switchIfEmpty(Mono.empty());
    }
}
