package ec.com.sofka.handler;

import ec.com.sofka.account.commands.usecases.CreateAccountUseCase;
import ec.com.sofka.account.queries.query.GetAccountByNumberQuery;
import ec.com.sofka.account.queries.query.GetAllByUserIdQuery;
import ec.com.sofka.account.queries.usecases.GetAccountByNumberViewUseCase;
import ec.com.sofka.account.queries.usecases.GetAllAccountsViewUseCase;
import ec.com.sofka.account.queries.usecases.GetAllByUserIdViewUseCase;
import ec.com.sofka.dto.AccountRequestDTO;
import ec.com.sofka.mapper.AccountMapper;
import ec.com.sofka.validator.RequestValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class AccountHandler {

    private final RequestValidator requestValidator;
    private final GetAccountByNumberViewUseCase getAccountByNumberViewUseCase;
    private final CreateAccountUseCase createAccountUseCase;
    private final GetAllByUserIdViewUseCase getAllByUserIdViewUseCase;
    private final GetAllAccountsViewUseCase getAllAccountsViewUseCase;

    public AccountHandler(
            RequestValidator requestValidator,
            GetAccountByNumberViewUseCase getAccountByNumberViewUseCase,
            CreateAccountUseCase createAccountUseCase,
            GetAllByUserIdViewUseCase getAllByUserIdViewUseCase,
            GetAllAccountsViewUseCase getAllAccountsViewUseCase
    ) {
        this.requestValidator = requestValidator;
        this.getAccountByNumberViewUseCase = getAccountByNumberViewUseCase;
        this.createAccountUseCase = createAccountUseCase;
        this.getAllByUserIdViewUseCase = getAllByUserIdViewUseCase;
        this.getAllAccountsViewUseCase = getAllAccountsViewUseCase;
    }

    public Mono<ServerResponse> getByAccountNumber(ServerRequest request) {
        String accountNumber = request.pathVariable("id");
        return getAccountByNumberViewUseCase.get(new GetAccountByNumberQuery(accountNumber))
                .map(queryResponse -> AccountMapper.fromEntity(queryResponse.getSingleResult().get()))
                .flatMap(accountResponseDTO ->
                        ServerResponse
                                .ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(accountResponseDTO));
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(AccountRequestDTO.class)
                .doOnNext(requestValidator::validate)
                .map(AccountMapper::toEntity)
                .flatMap(createAccountUseCase::execute)
                .map(AccountMapper::fromEntity)
                .flatMap(accountResponseDTO -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(accountResponseDTO));
    }

    public Mono<ServerResponse> getAllByUserId(ServerRequest request) {
        String userId = request.pathVariable("userId");

        return getAllByUserIdViewUseCase.get(new GetAllByUserIdQuery(userId))
                .map(queryResponse -> queryResponse.getMultipleResults()
                        .stream()
                        .map(AccountMapper::fromEntity)
                        .toList())
                .flatMap(accountResponseDTOs ->
                        ServerResponse
                                .ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(accountResponseDTOs));
    }

    public Mono<ServerResponse> getAll(ServerRequest request) {
        return getAllAccountsViewUseCase.get()
                .map(queryResponse -> queryResponse.getMultipleResults()
                        .stream()
                        .map(AccountMapper::fromEntityWithUser)
                        .toList())
                .flatMap(accountResponseDTOs ->
                        ServerResponse
                                .ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(accountResponseDTOs));
    }
}
