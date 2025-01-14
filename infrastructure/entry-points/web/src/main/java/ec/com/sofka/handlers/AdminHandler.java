package ec.com.sofka.handlers;

import ec.com.sofka.dto.AccountRequestDTO;
import ec.com.sofka.dto.AdminRequestDTO;
import ec.com.sofka.dto.LoginRequestDTO;
import ec.com.sofka.mapper.AccountMapper;
import ec.com.sofka.mapper.AdminMapper;
import ec.com.sofka.usecases.LoginAdminUseCase;
import ec.com.sofka.usecases.RegisterAdminUseCase;
import ec.com.sofka.usecases.command.LoginAdminCommand;
import ec.com.sofka.usecases.command.RegisterAdminCommand;
import ec.com.sofka.validator.RequestValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class AdminHandler {

    private final RequestValidator requestValidator;
    private final RegisterAdminUseCase registerAdminUseCase;
    private final LoginAdminUseCase loginAdminUseCase;

    public AdminHandler(RequestValidator requestValidator, RegisterAdminUseCase registerAdminUseCase, LoginAdminUseCase loginAdminUseCase) {
        this.requestValidator = requestValidator;
        this.registerAdminUseCase = registerAdminUseCase;
        this.loginAdminUseCase = loginAdminUseCase;
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(AdminRequestDTO.class)
                .doOnNext(requestValidator::validate)
                .map(AdminMapper::toRegisterCommand)
                .flatMap(registerAdminUseCase::execute)
                .flatMap(adminResponseDTO -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(adminResponseDTO));
    }

    public Mono<ServerResponse> login(ServerRequest request) {
        return request.bodyToMono(LoginRequestDTO.class)
                .doOnNext(requestValidator::validate)
                .map(AdminMapper::toLoginCommand)
                .flatMap(loginAdminUseCase::execute)
                .flatMap(adminResponseDTO -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(adminResponseDTO));
    }
}
