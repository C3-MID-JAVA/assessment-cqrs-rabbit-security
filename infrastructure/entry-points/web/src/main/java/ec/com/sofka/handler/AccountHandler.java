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
import ec.com.sofka.generics.utils.QueryResponse;
import ec.com.sofka.mapper.AccountDTOMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class AccountHandler {
    private final GetAccountByAccountNumberUseCase getAccountByAccountNumberUseCase;
    private final CreateAccountUseCase createAccountUseCase;
    private final GetAccountByIdUseCase getAccountByIdUseCase;
    private final GetAllAccountsUseCase getAllAccountsUseCase;
    private final DeleteAccountUseCase deleteAccountUseCase;
    private final UpdateAccountUseCase updateAccountUseCase;



    public AccountHandler(GetAccountByAccountNumberUseCase getAccountByAccountNumberUseCase, CreateAccountUseCase createAccountUseCase,
                          GetAllAccountsUseCase getAllAccountsUseCase, GetAccountByIdUseCase getAccountByIdUseCase, DeleteAccountUseCase deleteAccountUseCase,
                          UpdateAccountUseCase updateAccountUseCase) {
        this.getAccountByAccountNumberUseCase = getAccountByAccountNumberUseCase;
        this.createAccountUseCase = createAccountUseCase;
        this.getAllAccountsUseCase = getAllAccountsUseCase;
        this.getAccountByIdUseCase = getAccountByIdUseCase;
        this.deleteAccountUseCase = deleteAccountUseCase;
        this.updateAccountUseCase = updateAccountUseCase;
    }

    public Mono<AccountResponseDTO> createAccount(AccountRequestDTO request) {
        CreateAccountCommand createAccountRequest = AccountDTOMapper.accountRequestDTOtoCreateAccountCommand(request);
        return createAccountUseCase.execute(createAccountRequest)
                .map(AccountDTOMapper::accountResponsetoAccountResponseDTO);
    }


    public Flux<AccountResponseDTO> getAllAccounts() {
        return getAllAccountsUseCase.get()
                .flatMapMany(queryResponse -> Flux.fromIterable(
                        queryResponse.getMultipleResults()
                ))
                .map(AccountDTOMapper::accountResponsetoAccountResponseDTO);
    }

    public Mono<AccountResponseDTO> getAccountByNumber(AccountReqByIdDTO request) {
        return getAccountByAccountNumberUseCase.get(
                new GetByElementQuery(
                        request.getCustomerId(),
                        request.getAccountNumber()
                ))
                .flatMap(queryResponse -> Mono.justOrEmpty(
                        queryResponse.getSingleResult()
                ))
                .map(AccountDTOMapper::accountResponsetoAccountResponseDTO);
    }

    public Mono<AccountResponseDTO> getAccountById(AccountReqByIdDTO request) {
        return getAccountByIdUseCase.get(
                new GetByElementQuery(
                        request.getCustomerId(),
                        request.getCustomerId()
                ))
                .flatMap(queryResponse -> Mono.justOrEmpty(
                        queryResponse.getSingleResult()
                ))
                .map(AccountDTOMapper::accountResponsetoAccountResponseDTO);
    }


    public Mono<AccountResponseDTO> updateAccount(AccountRequestDTO request) {
        return updateAccountUseCase.execute(AccountDTOMapper.accountRequestDTOtoUpdateAccountCommand(request))
                .map(AccountDTOMapper::updateAccountResponseToAccountResponse)
                .map(AccountDTOMapper::accountResponsetoAccountResponseDTO);
    }

    public Mono<AccountResponseDTO> deleteAccount(AccountRequestDTO request) {
        return deleteAccountUseCase.execute(AccountDTOMapper.accountRequestDTOtoUpdateAccountCommand(request))
                .map(AccountDTOMapper::updateAccountResponseToAccountResponse)
                .map(AccountDTOMapper::accountResponsetoAccountResponseDTO);
    }


}
