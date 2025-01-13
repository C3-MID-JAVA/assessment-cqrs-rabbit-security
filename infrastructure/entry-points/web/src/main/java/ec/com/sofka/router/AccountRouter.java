package ec.com.sofka.router;

import ec.com.sofka.ErrorResponse;
import ec.com.sofka.data.AccountReqByElementDTO;
import ec.com.sofka.data.AccountRequestDTO;
import ec.com.sofka.data.AccountResponseDTO;
import ec.com.sofka.globalexceptions.GlobalErrorHandler;
import ec.com.sofka.handler.AccountHandler;
import ec.com.sofka.service.ValidationService;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class AccountRouter {

    private final AccountHandler handler;
    private final ValidationService validationService;
    private final GlobalErrorHandler globalErrorHandler;

    public AccountRouter(AccountHandler handler, ValidationService validationService, GlobalErrorHandler globalErrorHandler) {
        this.handler = handler;
        this.validationService = validationService;
        this.globalErrorHandler = globalErrorHandler;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/accounts",
                    operation = @Operation(
                            tags = {"Accounts"},
                            operationId = "create",
                            summary = "Create a new account",
                            description = "This endpoint allows the creation of a new bank account for a user. It accepts user details in the request body and returns the created account's information.",
                            requestBody = @RequestBody(
                                    description = "Account creation details",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = AccountRequestDTO.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Account successfully created",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountResponseDTO.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Bad request, validation error or missing required fields",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/update",
                    operation = @Operation(
                            tags = {"Accounts"},
                            operationId = "update",
                            summary = "Create a new account",
                            description = "This endpoint allows the creation of a new bank account for a user. It accepts user details in the request body and returns the created account's information.",
                            requestBody = @RequestBody(
                                    description = "Account creation details",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = AccountRequestDTO.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Account successfully created",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountResponseDTO.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Bad request, validation error or missing required fields",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/accounts/accountId",
                    operation = @Operation(
                            tags = {"Accounts"},
                            operationId = "getAccountById",
                            summary = "Create a new account",
                            description = "This endpoint allows the creation of a new bank account for a user. It accepts user details in the request body and returns the created account's information.",
                            requestBody = @RequestBody(
                                    description = "Account creation details",
                                    required = false,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = AccountReqByElementDTO.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Account successfully created",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountResponseDTO.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Bad request, validation error or missing required fields",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/accounts/getAll",
                    operation = @Operation(
                            tags = {"Accounts"},
                            operationId = "listAccounts",
                            summary = "List all accounts",
                            description = "Fetches a list of all accounts available in the system.",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successfully retrieved the list of accounts",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountResponseDTO[].class))
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/accounts/accountNumber",
                    operation = @Operation(
                            tags = {"Accounts"},
                            operationId = "getAccountByAccountNumber",
                            summary = "Create a new account",
                            description = "This endpoint allows the creation of a new bank account for a user. It accepts user details in the request body and returns the created account's information.",
                            requestBody = @RequestBody(
                                    description = "Account creation details",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = AccountReqByElementDTO.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Account successfully created",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountResponseDTO.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Bad request, validation error or missing required fields",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> accountRoutes() {
        return RouterFunctions
                .route(POST("/api/v1/accounts").and(accept(MediaType.APPLICATION_JSON)), this::createAccount)
                .andRoute(POST("/api/v1/update").and(accept(MediaType.APPLICATION_JSON)), this::updateAccount)
                .andRoute(POST("/api/v1/accounts/accountNumber").and(accept(MediaType.APPLICATION_JSON)), this::getAccountByAccountNumber)
                .andRoute(GET("/api/v1/accounts/getAll"), this::listAccounts)
                .andRoute(POST("/api/v1/accounts/accountId").and(accept(MediaType.APPLICATION_JSON)), this::getAccountById)
        ;
    }

    public Mono<ServerResponse> createAccount(ServerRequest request) {
        return request.bodyToMono(AccountRequestDTO.class)
                .flatMap(dto -> validationService.validate(dto, AccountRequestDTO.class))
                .flatMap(handler::createAccount)
                .flatMap(accountResponseDTO -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(accountResponseDTO))
                .onErrorResume(ex -> globalErrorHandler.handleException(request.exchange(), ex));
    }

    public Mono<ServerResponse> updateAccount(ServerRequest request) {
        return request.bodyToMono(AccountRequestDTO.class)
                .flatMap(dto -> validationService.validate(dto, AccountRequestDTO.class))
                .flatMap(handler::updateAccount)
                .flatMap(accountResponseDTO -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(accountResponseDTO))
                .onErrorResume(ex -> globalErrorHandler.handleException(request.exchange(), ex));
    }


    public Mono<ServerResponse> getAccountByAccountNumber(ServerRequest request) {
        return request.bodyToMono(AccountReqByElementDTO.class)
                .flatMap(handler::getAccountByNumber)
                .flatMap(accountResponseDTO -> ServerResponse
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(accountResponseDTO))
                .onErrorResume(ex -> globalErrorHandler.handleException(request.exchange(), ex));
    }


    public Mono<ServerResponse> getAccountById(ServerRequest request) {
        return request.bodyToMono(AccountReqByElementDTO.class)
                .flatMap(handler::getAccountById)
                .flatMap(accountResponseDTO -> ServerResponse
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(accountResponseDTO))
                .onErrorResume(ex -> globalErrorHandler.handleException(request.exchange(), ex));
    }


    public Mono<ServerResponse> listAccounts(ServerRequest request) {
        return handler.getAllAccounts()
                .collectList()
                .flatMap(accounts -> ServerResponse
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(accounts))
                .onErrorResume(ex -> globalErrorHandler.handleException(request.exchange(), ex));
    }


}
