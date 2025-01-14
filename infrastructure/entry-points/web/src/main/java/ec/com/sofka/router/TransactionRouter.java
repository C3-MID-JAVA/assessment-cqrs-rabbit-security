package ec.com.sofka.router;

import ec.com.sofka.data.TransactionRequestDTO;
import ec.com.sofka.data.TransactionResponseDTO;
import ec.com.sofka.exceptions.model.ErrorDetails;
import ec.com.sofka.handlers.transaction.CreateTransactionHandler;
import ec.com.sofka.handlers.transaction.GetAllTransactionsHandler;
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
public class TransactionRouter {
    private final RequestValidator requestValidator;
    private final CreateTransactionHandler createTransactionHandler;
    private final GetAllTransactionsHandler getAllTransactionsHandler;

    public TransactionRouter(RequestValidator requestValidator, CreateTransactionHandler createTransactionHandler, GetAllTransactionsHandler getAllTransactionsHandler) {
        this.requestValidator = requestValidator;
        this.createTransactionHandler = createTransactionHandler;
        this.getAllTransactionsHandler = getAllTransactionsHandler;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/transactions",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.POST,
                    beanClass = CreateTransactionHandler.class,
                    beanMethod = "save",
                    operation = @Operation(
                            tags = {"Transactions"},
                            operationId = "createTransaction",
                            summary = "Create a new transaction",
                            description = "Create a new transaction from the request data.",
                            requestBody = @RequestBody(
                                    description = "Details of the required entity.",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = TransactionRequestDTO.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Record created successfully.",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionResponseDTO.class))
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
            ),
            @RouterOperation(
                    path = "/api/transactions",
                    method = RequestMethod.GET,
                    beanClass = GetAllTransactionsHandler.class,
                    beanMethod = "getAllTransactions",
                    operation = @Operation(
                            tags = {"Transactions"},
                            operationId = "getAllTransactions",
                            summary = "Get all transactions",
                            description = "Get all registered transactions.",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successfully obtained all registered transactions.",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionResponseDTO.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Bad request.",
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
    public RouterFunction<ServerResponse> transactionsRouters() {
        return RouterFunctions
                .route(POST("/api/transactions").and(accept(APPLICATION_JSON)), this::saveTransaction)
                .andRoute(GET("/api/transactions"), this::allTransactions);
    }

    public Mono<ServerResponse> saveTransaction(ServerRequest request) {
        return request.bodyToMono(TransactionRequestDTO.class)
                .doOnNext(requestValidator::validate)
                .flatMap(createTransactionHandler::save)
                .flatMap(response ->
                        ServerResponse.ok().contentType(APPLICATION_JSON).bodyValue(response));

    }

    public Mono<ServerResponse> allTransactions(ServerRequest request) {
        return getAllTransactionsHandler.getAll()
                .collectList()
                .flatMap(list -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(list)
                );
    }

}