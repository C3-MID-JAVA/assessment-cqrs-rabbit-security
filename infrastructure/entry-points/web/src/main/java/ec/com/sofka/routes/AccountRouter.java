package ec.com.sofka.routes;

import ec.com.sofka.ErrorDetails;
import ec.com.sofka.data.AccountReqDTO;
import ec.com.sofka.exceptions.BodyRequestValidator;
import ec.com.sofka.exceptions.GlobalExceptionsHandler;
import ec.com.sofka.handlers.AccountHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class AccountRouter {

    private final AccountHandler accountHandler;
    private final BodyRequestValidator bodyRequestValidator;
    private final GlobalExceptionsHandler globalExceptionsHandler;

    public AccountRouter(AccountHandler accountHandler, BodyRequestValidator bodyRequestValidator, GlobalExceptionsHandler globalExceptionsHandler) {
        this.accountHandler = accountHandler;
        this.bodyRequestValidator = bodyRequestValidator;
        this.globalExceptionsHandler = globalExceptionsHandler;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/account/create",
                    method = RequestMethod.POST,
                    operation = @Operation(
                            tags = {"Accounts"},
                            operationId = "create",
                            summary = "Create a new account",
                            description = "Creates a new account with the provided details.",
                            requestBody = @RequestBody(
                                    description = "Account creation details",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = AccountReqDTO.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Account successfully created",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountReqDTO.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Bad request, validation error or missing required fields",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Account could already exist",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))
                                    )
                            }
                    )
            ),
            @RouterOperation(
            path = "/api/v1/account",
            method = RequestMethod.GET,
            operation = @Operation(
                    tags = {"Accounts"},
                    operationId = "get",
                    summary = "Retrieve all accounts",
                    description = "Retrieve all accounts in database",
                    responses = {
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "Successfully retrieved all accounts",
                                    content = @Content(mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = AccountReqDTO.class))

                                    )
                            ),
                    }
            )
    ),
    })
    public RouterFunction<ServerResponse> accountRoutes() {
        return RouterFunctions
                .route(RequestPredicates.POST("/api/v1/account/create").and(accept(MediaType.APPLICATION_JSON)), this::createAccount)
                .andRoute(RequestPredicates.GET("/api/v1/account"),this::getAllAccounts)
                ;
    }

    public Mono<ServerResponse> createAccount(ServerRequest request) {

        return request.bodyToMono(AccountReqDTO.class)
                .doOnNext(bodyRequestValidator::validate)
                .flatMap(accountHandler::createAccount)
                .flatMap(accountDTO -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON).bodyValue(accountDTO))
                .onErrorResume(globalExceptionsHandler::handleException);

    }

    public Mono<ServerResponse> getAllAccounts(ServerRequest request) {

        return accountHandler.getAllAccounts()
                .collectList()
                .flatMap(elements ->
                        elements.isEmpty() ?
                                ServerResponse.status(HttpStatus.NOT_FOUND).build()
                                : ServerResponse.ok().body(Flux.fromIterable(elements), AccountReqDTO.class)

                );
    }


}
