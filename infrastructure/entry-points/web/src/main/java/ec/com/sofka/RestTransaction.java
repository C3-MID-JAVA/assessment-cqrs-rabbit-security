package ec.com.sofka;

import ec.com.sofka.data.transaction.TransactionRequestDTO;
import ec.com.sofka.data.transaction.TransactionResponseDTO;
import ec.com.sofka.handlers.TransactionHandler;
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
public class RestTransaction {
    private final TransactionHandler handler;

    public RestTransaction(TransactionHandler handler) {
        this.handler = handler;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/transaction",
                    operation = @Operation(
                            tags = {"Transactions"},
                            operationId = "saveTransaction",
                            summary = "Create a new transaction",
                            description = "This endpoint allows the creation of a new transaction.",
                            requestBody = @RequestBody(
                                    description = "Transaction creation details",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = TransactionRequestDTO.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Transaction successfully created",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionResponseDTO.class))
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
    public RouterFunction<ServerResponse> transactionRoutes() {
        return RouterFunctions
                .route(RequestPredicates.POST("/api/transaction"), this::saveTransaction);
    }

    public Mono<ServerResponse> saveTransaction(ServerRequest request) {
        String customerId = request.queryParam("customerId").orElseThrow(() -> new IllegalArgumentException("Customer ID is required"));
        return request.bodyToMono(TransactionRequestDTO.class)
                .flatMap(transactionRequestDTO -> handler.saveTransaction(transactionRequestDTO, customerId))
                .flatMap(response -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response))
                .onErrorResume(e -> ServerResponse.status(HttpStatus.BAD_REQUEST)
                        .bodyValue(e.getMessage()));
    }
}