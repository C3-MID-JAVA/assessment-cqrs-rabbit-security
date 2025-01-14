package ec.com.sofka.router;

import ec.com.sofka.data.AccountRequestDTO;
import ec.com.sofka.data.AccountResponseDTO;
import ec.com.sofka.exceptions.model.ErrorDetails;
import ec.com.sofka.handlers.account.CreateAccountHandler;
import ec.com.sofka.validator.RequestValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class AccountRouter {
    private final RequestValidator requestValidator;
    private final CreateAccountHandler createAccountHandler;

    public AccountRouter(RequestValidator requestValidator, CreateAccountHandler createAccountHandler) {
        this.requestValidator = requestValidator;
        this.createAccountHandler = createAccountHandler;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/accounts",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.POST,
                    beanClass = CreateAccountHandler.class,
                    beanMethod = "save",
                    operation = @Operation(
                            tags = {"Accounts"},
                            operationId = "createAccount",
                            summary = "Create a new account",
                            description = "Create a new account from the request data.",
                            requestBody = @RequestBody(
                                    description = "Details of the required entity.",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = AccountRequestDTO.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Record created successfully.",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountResponseDTO.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Bad request.",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "422",
                                            description = "The entity has a conflict.",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Internal application problems.",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> accountsRouters() {
        return RouterFunctions
                .route(POST("/api/accounts").and(accept(APPLICATION_JSON)), this::saveAccount);
    }

    public Mono<ServerResponse> saveAccount(ServerRequest request) {
        return request.bodyToMono(AccountRequestDTO.class)
                .doOnNext(requestValidator::validate)
                .flatMap(createAccountHandler::save)
                .flatMap(response ->
                        ServerResponse.ok().contentType(APPLICATION_JSON).bodyValue(response));

    }

}