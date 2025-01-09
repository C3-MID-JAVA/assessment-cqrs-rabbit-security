package ec.com.sofka.handler;

import ec.com.sofka.appservice.commands.CreateAccountCommand;
import ec.com.sofka.appservice.queries.query.GetByElementQuery;
import ec.com.sofka.appservice.commands.UpdateAccountCommand;
import ec.com.sofka.appservice.commands.usecases.CreateAccountUseCase;
import ec.com.sofka.appservice.commands.usecases.DeleteAccountUseCase;
import ec.com.sofka.appservice.commands.usecases.UpdateAccountUseCase;
import ec.com.sofka.appservice.queries.usecases.GetAccountByAccountNumberUseCase;
import ec.com.sofka.appservice.queries.usecases.GetAccountByIdUseCase;
import ec.com.sofka.appservice.queries.usecases.GetAllAccountsUseCase;
import ec.com.sofka.data.AccountReqByIdDTO;
import ec.com.sofka.data.AccountRequestDTO;
import ec.com.sofka.data.AccountResponseDTO;
import ec.com.sofka.mapper.AccountDTOMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class AccountHandler {
    private final GetAccountByAccountNumberUseCase getAccountByAccountNumberUseCase;
    private final CreateAccountUseCase createAccountUseCase;
    private final GetAccountByIdUseCase getAccountByIdUseCase;
    private final GetAllAccountsUseCase getAccountsUseCase;
    private final DeleteAccountUseCase deleteAccountUseCase;
    private final UpdateAccountUseCase updateAccountUseCase;



    public AccountHandler(GetAccountByAccountNumberUseCase getAccountByAccountNumberUseCase, CreateAccountUseCase createAccountUseCase,
                          GetAllAccountsUseCase getAccountsUseCase, GetAccountByIdUseCase getAccountByIdUseCase, DeleteAccountUseCase deleteAccountUseCase,
                          UpdateAccountUseCase updateAccountUseCase) {
        this.getAccountByAccountNumberUseCase = getAccountByAccountNumberUseCase;
        this.createAccountUseCase = createAccountUseCase;
        this.getAccountsUseCase = getAccountsUseCase;
        this.getAccountByIdUseCase = getAccountByIdUseCase;
        this.deleteAccountUseCase = deleteAccountUseCase;
        this.updateAccountUseCase = updateAccountUseCase;
    }

    public Mono<AccountResponseDTO> createAccount(AccountRequestDTO request) {
        // Mapear la solicitud de AccountRequestDTO a CreateAccountRequest
        CreateAccountCommand createAccountRequest = AccountDTOMapper.toCreateAccountRequest(request);

        // Ejecutar el caso de uso y mapear la respuesta a un ResponseDTO
        return createAccountUseCase.execute(createAccountRequest)
                .map(response -> AccountDTOMapper.toAccountResponseDTO(response));
    }


    public Flux<AccountResponseDTO> getAllAccounts() {
        return getAccountsUseCase.get()
                .map(accountResponse -> new AccountResponseDTO(
                        accountResponse.getCustomerId(),
                        accountResponse.getAccountId(),
                        accountResponse.getName(),
                        accountResponse.getAccountNumber(),
                        accountResponse.getBalance(),
                        accountResponse.getStatus()
                ));
    }

    public Mono<AccountResponseDTO> getAccountByNumber(AccountReqByIdDTO request) {
        return getAccountByAccountNumberUseCase.execute(
                new GetByElementQuery(
                        request.getCustomerId(),
                        request.getAccountNumber()
                )).map(response -> new AccountResponseDTO(
                response.getCustomerId(),
                response.getAccountId(),
                response.getName(),
                response.getAccountNumber(),
                response.getBalance(),
                response.getStatus()
        ));
    }

    public Mono<AccountResponseDTO> getAccountById(AccountReqByIdDTO request) {
        return getAccountByIdUseCase.execute(
                new GetByElementQuery(
                        request.getCustomerId(),
                        request.getCustomerId()
                )).map(response -> new AccountResponseDTO(
                response.getCustomerId(),
                response.getAccountId(),
                response.getName(),
                response.getAccountNumber(),
                response.getBalance(),
                response.getStatus()
        ));
    }


    public Mono<AccountResponseDTO> updateAccount(AccountRequestDTO request) {
        return updateAccountUseCase.execute(
                new UpdateAccountCommand(
                        request.getCustomerId(),
                        request.getInitialBalance(),
                        request.getAccountNumber(),
                        request.getOwner(),
                        request.getStatus()
                )).map(response -> new AccountResponseDTO(
                response.getCustomerId(),
                response.getAccountId(),
                response.getName(),
                response.getAccountNumber(),
                response.getBalance(),
                response.getStatus()
        ));
    }

    public Mono<AccountResponseDTO> deleteAccount(AccountRequestDTO request) {
        return deleteAccountUseCase.execute(
                new UpdateAccountCommand(
                        request.getCustomerId(),
                        request.getInitialBalance(),
                        request.getAccountNumber(),
                        request.getOwner(),
                        request.getStatus()
                )).map(response -> new AccountResponseDTO(
                response.getCustomerId(),
                response.getAccountId(),
                response.getName(),
                response.getAccountNumber(),
                response.getBalance(),
                response.getStatus()
        ));
    }


}
