package ec.com.sofka.handler;

import ec.com.sofka.dto.TransactionRequestDTO;
import ec.com.sofka.mapper.TransactionMapper;
import ec.com.sofka.transaction.CreateTransactionUseCase;
import ec.com.sofka.transaction.GetAllByAccountNumberUseCase;
import ec.com.sofka.transaction.request.GetAllByAccountNumberRequest;
import ec.com.sofka.validator.RequestValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class TransactionHandler {

    private final RequestValidator requestValidator;
    private final CreateTransactionUseCase createTransactionUseCase;
    private final GetAllByAccountNumberUseCase getAllByAccountNumberUseCase;

    public TransactionHandler(RequestValidator requestValidator, CreateTransactionUseCase createTransactionUseCase, GetAllByAccountNumberUseCase getAllByAccountNumberUseCase) {
        this.requestValidator = requestValidator;
        this.createTransactionUseCase = createTransactionUseCase;
        this.getAllByAccountNumberUseCase = getAllByAccountNumberUseCase;
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(TransactionRequestDTO.class)
                .doOnNext(requestValidator::validate)
                .map(TransactionMapper::toEntity)
                .flatMap(createTransactionUseCase::execute)
                .map(TransactionMapper::fromEntity)
                .flatMap(transactionResponseDTO -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(transactionResponseDTO));
    }

    public Mono<ServerResponse> getAllByAccountNumber(ServerRequest request) {
        return request.bodyToMono(GetAllByAccountNumberRequest.class)
                .flatMapMany(getAllByAccountNumberUseCase::execute)
                .collectList()
                .flatMap(transactionResponseDTOs ->
                        ServerResponse
                                .ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(transactionResponseDTOs)
                );
    }
}
