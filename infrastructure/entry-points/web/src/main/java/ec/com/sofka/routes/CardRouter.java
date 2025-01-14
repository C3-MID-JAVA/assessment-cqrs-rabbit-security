package ec.com.sofka.routes;


import ec.com.sofka.ErrorDetails;
import ec.com.sofka.data.AccountSimpleRequestDTO;
import ec.com.sofka.data.CardReqDTO;
import ec.com.sofka.exceptions.BodyRequestValidator;
import ec.com.sofka.exceptions.GlobalExceptionsHandler;
import ec.com.sofka.handlers.CardHandler;
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
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class CardRouter {
    private final CardHandler cardHandler;

    private final BodyRequestValidator bodyRequestValidator;
    private final GlobalExceptionsHandler globalExceptionsHandler;


    public CardRouter(CardHandler cardHandler, BodyRequestValidator bodyRequestValidator, GlobalExceptionsHandler globalExceptionsHandler) {
        this.cardHandler = cardHandler;
        this.bodyRequestValidator = bodyRequestValidator;
        this.globalExceptionsHandler = globalExceptionsHandler;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/card/create",
                    method = RequestMethod.POST,
                    operation = @Operation(
                            tags = {"Card"},
                            operationId = "create",
                            summary = "Create a new card",
                            description = "Creates a new card with the provided details.",
                            requestBody = @RequestBody(
                                    description = "Card creation details",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = CardReqDTO.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Card successfully created",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CardReqDTO.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Bad request, validation error or missing required fields",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Card could already exist",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/card/byAccount",
                    method = RequestMethod.POST,
                    operation = @Operation(
                            tags = {"Card"},
                            operationId = "getCards",
                            summary = "Retrieve all cards  by account",
                            description = "Retrieve all card in database by account",
                            requestBody = @RequestBody(
                                    description = "Account ",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = AccountSimpleRequestDTO.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successfully retrieved all cards",
                                            content = @Content(mediaType = "application/json",
                                                    array = @ArraySchema(schema = @Schema(implementation = CardReqDTO.class))

                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Bad request, validation error or missing required fields",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))
                                    )
                            }
                    )
            ),
    })
    public RouterFunction<ServerResponse> cardRoutes() {
        return RouterFunctions
                .route(RequestPredicates.POST("/api/v1/card/create").and(accept(MediaType.APPLICATION_JSON)), this::createCard)
                .andRoute(RequestPredicates.POST("/api/v1/card/byAccount").and(accept(MediaType.APPLICATION_JSON)), this::getCardsByAccount);
    }

    public Mono<ServerResponse> createCard(ServerRequest request) {

        return request.bodyToMono(CardReqDTO.class)
                .doOnNext(bodyRequestValidator::validate)
                .flatMap(cardHandler::createCard)
                .flatMap(card -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON).bodyValue(card))
                .onErrorResume(globalExceptionsHandler::handleException);
    }

    public Mono<ServerResponse> getCardsByAccount(ServerRequest request) {

        return request.bodyToMono(AccountSimpleRequestDTO.class)
                .doOnNext(bodyRequestValidator::validate)
                .flatMap(re  -> cardHandler.getCardsByAccountNumber(re.getAccountNumber())
                        .collectList()
                        .flatMap(cards -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(cards)))
                .onErrorResume(globalExceptionsHandler::handleException);
    }

}
