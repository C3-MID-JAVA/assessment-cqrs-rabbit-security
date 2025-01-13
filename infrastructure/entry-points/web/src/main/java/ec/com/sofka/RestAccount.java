package ec.com.sofka;
import ec.com.sofka.data.RequestDTO;
import ec.com.sofka.data.ResponseDTO;
import ec.com.sofka.handlers.AccountHandler;
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
import org.springframework.web.ErrorResponse;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;


@Configuration
public class RestAccount {
    private final AccountHandler handler;

    public RestAccount(AccountHandler handler) {
        this.handler = handler;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/account",
                    operation = @Operation(
                            tags = {"Accounts"},
                            operationId = "createAccount",
                            summary = "Create a new account",
                            description = "This endpoint allows the creation of a new bank account.",
                            requestBody = @RequestBody(
                                    description = "Account creation details",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = RequestDTO.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Account successfully created",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))
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
                    path = "/api/accounts",
                    operation = @Operation(
                            tags = {"Accounts"},
                            operationId = "getAllAccounts",
                            summary = "Get all accounts",
                            description = "This endpoint retrieves all bank accounts.",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successfully retrieved all accounts",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Bad request",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/account/number",
                    operation = @Operation(
                            tags = {"Accounts"},
                            operationId = "getAccountByNumber",
                            summary = "Get account by number",
                            description = "This endpoint retrieves a bank account by its number.",
                            requestBody = @RequestBody(
                                    description = "Account number details",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = RequestDTO.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successfully retrieved the account",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Bad request",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "Account not found",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/account",
                    operation = @Operation(
                            tags = {"Accounts"},
                            operationId = "updateAccount",
                            summary = "Update an existing account",
                            description = "This endpoint allows updating an existing bank account.",
                            requestBody = @RequestBody(
                                    description = "Account update details",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = RequestDTO.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Account successfully updated",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Bad request, validation error or missing required fields",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "Account not found",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> accountRoutes() {
        return RouterFunctions
                .route(RequestPredicates.POST("/api/account"), this::createAccount)
                .andRoute(RequestPredicates.GET("/api/accounts"), this::getAllAccounts)
                .andRoute(RequestPredicates.POST("/api/account/number"), this::getAccountByNumber)
                .andRoute(RequestPredicates.PUT("/api/account"), this::updateAccount);
    }

    public Mono<ServerResponse> createAccount(ServerRequest request) {
        return request.bodyToMono(RequestDTO.class)
                .flatMap(handler::createAccount)
                .flatMap(response -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response))
                .onErrorResume(e -> ServerResponse.status(HttpStatus.BAD_REQUEST)
                        .bodyValue("Error creando la cuenta"));
    }

    public Mono<ServerResponse> getAllAccounts(ServerRequest request) {
        return handler.getAllAccounts()
                .collectList()
                .flatMap(accounts -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(accounts))
                .switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND)
                        .bodyValue("No existen cuentas"))
                .onErrorResume(e -> ServerResponse.status(HttpStatus.BAD_REQUEST)
                        .bodyValue("Error recuperando ciuentas"));
    }

    public Mono<ServerResponse> getAccountByNumber(ServerRequest request) {
        return request.bodyToMono(RequestDTO.class)
                .flatMap(handler::getAccountByNumber)
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response))
                .onErrorResume(e -> ServerResponse.status(HttpStatus.NOT_FOUND)
                        .bodyValue("La cuenta con el ID  no existe"));
    }

    public Mono<ServerResponse> updateAccount(ServerRequest request) {
        return request.bodyToMono(RequestDTO.class)
                .flatMap(handler::updateAccount)
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response))
                .onErrorResume(e -> ServerResponse.status(HttpStatus.BAD_REQUEST)
                        .bodyValue("Error la  cuenta no fue actualizada"));
    }
}

