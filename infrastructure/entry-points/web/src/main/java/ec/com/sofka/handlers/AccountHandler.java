/*package ec.com.sofka.handlers;

import ec.com.sofka.commands.CreateAccountCommand;
import ec.com.sofka.commands.usecases.CreateAccountUseCase;
import ec.com.sofka.commands.usecases.DeleteAccountUseCase;
import ec.com.sofka.commands.usecases.UpdateAccountUseCase;
import ec.com.sofka.data.RequestDTO;
import ec.com.sofka.data.ResponseDTO;
import ec.com.sofka.queries.query.GetAccountQuery;
import ec.com.sofka.commands.UpdateAccountCommand;
import ec.com.sofka.queries.usecases.GetAccountByNumberUseCase;
import ec.com.sofka.queries.usecases.GetAllAccountsUseCase;
import org.springframework.stereotype.Component;

import java.util.List;

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

    public ResponseDTO createAccount(RequestDTO request){
        var response = createAccountUseCase.execute(
                new CreateAccountCommand(
                        request.getAccountNum(),
                        request.getName(),
                        request.getBalance(),
                        request.getIdUser()

                ));
        return new ResponseDTO(response.getCustomerId(),
                response.getAccountId(),
                response.getName(),
                response.getAccountNumber(),
                response.getBalance(),
                response.getStatus());
    }

    public List<ResponseDTO> getAllAccounts(){
        var response = getAllAccountsUseCase.get(new GetAccountQuery());
        return response.getMultipleResults().stream()
                .map(accountResponse -> new ResponseDTO(
                        accountResponse.getCustomerId(),
                        accountResponse.getAccountId(),
                        accountResponse.getName(),
                        accountResponse.getAccountNumber(),
                        accountResponse.getBalance(),
                        accountResponse.getStatus()
                        )
                ).toList();
    }

    public ResponseDTO getAccountByNumber(RequestDTO request){
        var response = getAccountByNumberUseCase.get(
                new GetAccountQuery(
                        request.getCustomerId(),
                        request.getAccountNum()
                )).getSingleResult().get();

        return new ResponseDTO(
                response.getCustomerId(),
                response.getAccountId(),
                response.getName(),
                response.getAccountNumber(),
                response.getBalance(),
                response.getStatus());
    }

    public ResponseDTO updateAccount(RequestDTO request){
        var response = updateAccountUseCase.execute(
                new UpdateAccountCommand(
                        request.getCustomerId(),
                        request.getBalance(),
                        request.getAccountNum(),
                        request.getName(),
                        request.getStatus(),
                        request.getIdUser()
                ));

        return new ResponseDTO(
                response.getCustomerId(),
                response.getAccountId(),
                response.getName(),
                response.getAccountNumber(),
                response.getBalance(),
                response.getStatus()
        );
    }

    public ResponseDTO deleteAccount(RequestDTO request){
        var response = deleteAccountUseCase.execute(
                new UpdateAccountCommand(
                        request.getCustomerId(),
                        request.getBalance(),
                        request.getAccountNum(),
                        request.getName(),
                        request.getStatus(),
                        request.getIdUser()

                ));
        return new ResponseDTO(
                response.getCustomerId(),
                response.getAccountId(),
                response.getName(),
                response.getAccountNumber(),
                response.getBalance(),
                response.getStatus());
    }
}
*/
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
import org.springframework.stereotype.Component;
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
                ));
    }
/*
    public Mono<ResponseDTO> getAccountByNumber(RequestDTO request) {
        return getAccountByNumberUseCase.get(
                new GetAccountQuery(
                        request.getCustomerId(),
                        request.getAccountNum()
                )).map(response -> new ResponseDTO(
                response.getCustomerId(),
                response.getAccountId(),
                response.getName(),
                response.getAccountNumber(),
                response.getBalance(),
                response.getStatus()
        ));
    }
*/

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
    });
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
        ));
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
        ));
    }
}