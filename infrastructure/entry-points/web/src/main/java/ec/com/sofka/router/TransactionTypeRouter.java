package ec.com.sofka.router;

import ec.com.sofka.data.TransactionTypeRequestDTO;
import ec.com.sofka.data.TransactionTypeResponseDTO;
import ec.com.sofka.exceptions.model.ErrorDetails;
import ec.com.sofka.handlers.transcationType.CreateTransactionTypeHandler;
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
public class TransactionTypeRouter {
    private final RequestValidator requestValidator;
    private final CreateTransactionTypeHandler createTransactionTypeHandler;

    public TransactionTypeRouter(RequestValidator requestValidator, CreateTransactionTypeHandler createTransactionTypeHandler) {
        this.requestValidator = requestValidator;
        this.createTransactionTypeHandler = createTransactionTypeHandler;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/transaction-types",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.POST,
                    beanClass = CreateTransactionTypeHandler.class,
                    beanMethod = "save",
                    operation = @Operation(
                            tags = {"TransactionTypes"},
                            operationId = "createTransactionType",
                            summary = "Create a new transaction type",
                            description = "Create a transaction type user from the request data.",
                            requestBody = @RequestBody(
                                    description = "Details of the required entity.",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = TransactionTypeRequestDTO.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Record created successfully.",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionTypeResponseDTO.class))
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
    public RouterFunction<ServerResponse> transactionTypesRouters() {
        return RouterFunctions
                .route(POST("/api/transaction-types").and(accept(APPLICATION_JSON)), this::saveTransactionType);
    }

    public Mono<ServerResponse> saveTransactionType(ServerRequest request) {
        return request.bodyToMono(TransactionTypeRequestDTO.class)
                .doOnNext(requestValidator::validate)
                .flatMap(createTransactionTypeHandler::save)
                .flatMap(response ->
                        ServerResponse.ok().contentType(APPLICATION_JSON).bodyValue(response));

    }

}