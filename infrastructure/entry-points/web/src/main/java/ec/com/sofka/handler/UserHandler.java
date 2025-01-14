package ec.com.sofka.handler;

import ec.com.sofka.dto.UserRequestDTO;
import ec.com.sofka.mapper.UserMapper;
import ec.com.sofka.user.commands.usecases.CreateUserUseCase;
import ec.com.sofka.user.queries.usecases.GetAllUserViewUseCase;
import ec.com.sofka.validator.RequestValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class UserHandler {

    private final RequestValidator requestValidator;
    private final GetAllUserViewUseCase getAllUserViewUseCase;
    private final CreateUserUseCase createUserUseCase;

    public UserHandler(RequestValidator requestValidator, GetAllUserViewUseCase getAllUserViewUseCase, CreateUserUseCase createUserUseCase) {
        this.requestValidator = requestValidator;
        this.getAllUserViewUseCase = getAllUserViewUseCase;
        this.createUserUseCase = createUserUseCase;
    }

    public Mono<ServerResponse> getAll(ServerRequest request) {
        return getAllUserViewUseCase.get()
                .map(queryResponse -> queryResponse.getMultipleResults()
                        .stream()
                        .map(UserMapper::fromQueryEntity)
                        .toList())
                .flatMap(userResponseDTOs ->
                        ServerResponse
                                .ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(userResponseDTOs));
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(UserRequestDTO.class)
                .doOnNext(requestValidator::validate)
                .map(UserMapper::toEntity)
                .flatMap(createUserUseCase::execute)
                .map(UserMapper::fromEntity)
                .flatMap(userResponseDTO -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(userResponseDTO));
    }
}
