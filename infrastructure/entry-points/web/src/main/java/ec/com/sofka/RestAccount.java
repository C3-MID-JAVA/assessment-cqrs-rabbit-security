package ec.com.sofka;
import ec.com.sofka.data.RequestDTO;
import ec.com.sofka.data.ResponseDTO;
import ec.com.sofka.handlers.AccountHandler;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;


@Configuration
public class RestAccount {
    private final AccountHandler handler;
    private final RequestValidator requestValidator;

    public RestAccount(AccountHandler handler, RequestValidator requestValidator) {
        this.handler = handler;
        this.requestValidator = requestValidator;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/account",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.POST,
                    beanClass = AccountHandler.class,
                    beanMethod = "createAccount",
                    operation = @Operation(
                            operationId = "createAccount",
                            responses = {
                                    @ApiResponse(responseCode = "201", description = "Account created", content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
                                    @ApiResponse(responseCode = "400", description = "Invalid input")
                            },
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = RequestDTO.class)))
                    )
            ),
            @RouterOperation(
                    path = "/api/accounts",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET,
                    beanClass = AccountHandler.class,
                    beanMethod = "getAllAccounts",
                    operation = @Operation(
                            operationId = "getAllAccounts",
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Accounts retrieved", content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
                                    @ApiResponse(responseCode = "404", description = "No accounts found")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/account/number",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.POST,
                    beanClass = AccountHandler.class,
                    beanMethod = "getAccountByNumber",
                    operation = @Operation(
                            operationId = "getAccountByNumber",
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Account retrieved", content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
                                    @ApiResponse(responseCode = "404", description = "Account not found")
                            },
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = RequestDTO.class)))
                    )
            ),
            @RouterOperation(
                    path = "/api/account",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.PUT,
                    beanClass = AccountHandler.class,
                    beanMethod = "updateAccount",
                    operation = @Operation(
                            operationId = "updateAccount",
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Account updated", content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
                                    @ApiResponse(responseCode = "400", description = "Invalid input")
                            },
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = RequestDTO.class)))
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
                .doOnNext(requestValidator::validate)
                .flatMap(handler::createAccount)
                .flatMap(response -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response))
                .onErrorResume(e -> ServerResponse.status(HttpStatus.BAD_REQUEST)
                        .bodyValue(e.getMessage()));
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
                        .bodyValue("Error recuperando cuentas"));
    }

    public Mono<ServerResponse> getAccountByNumber(ServerRequest request) {
        return request.bodyToMono(RequestDTO.class)
                .flatMap(handler::getAccountByNumber)
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response))
                .onErrorResume(e -> ServerResponse.status(HttpStatus.NOT_FOUND)
                        .bodyValue("La cuenta  no existe"));
    }

    public Mono<ServerResponse> updateAccount(ServerRequest request) {
        return request.bodyToMono(RequestDTO.class)
                .doOnNext(requestValidator::validate)
                .flatMap(handler::updateAccount)
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response))
                .onErrorResume(e -> ServerResponse.status(HttpStatus.BAD_REQUEST)
                        .bodyValue(e.getMessage()));
    }
}


