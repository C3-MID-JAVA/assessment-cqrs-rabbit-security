package ec.com.sofka;

import ec.com.sofka.aggregates.auth.handlers.UserHandler;
import ec.com.sofka.data.UserRequestDTO;
import ec.com.sofka.data.UserResponseDTO;
import ec.com.sofka.data.UserUpdateRequestDTO;
import ec.com.sofka.exceptions.model.ErrorDetails;
import ec.com.sofka.handler.UserAuthHandler;
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
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class UserRouter {
    private final RequestValidator requestValidator;
    private final UserAuthHandler userHandler;

    public UserRouter(RequestValidator requestValidator, UserAuthHandler userHandler) {
        this.requestValidator = requestValidator;
        this.userHandler = userHandler;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/auth/register",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.POST,
                    beanClass = UserAuthHandler.class,
                    beanMethod = "save",
                    operation = @Operation(
                            tags = {"Users"},
                            operationId = "createUser",
                            summary = "Create a new user",
                            description = "Create a new user from the request data.",
                            requestBody = @RequestBody(
                                    description = "Details of the required entity.",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = UserRequestDTO.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Record created successfully.",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))
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
                    path = "/api/auth/user-update",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.PUT,
                    beanClass = UserAuthHandler.class,
                    beanMethod = "update",
                    operation = @Operation(
                            tags = {"Users"},
                            operationId = "updateUser",
                            summary = "Update a user",
                            description = "Update a user from the request data.",
                            requestBody = @RequestBody(
                                    description = "Details of the required entity.",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = UserUpdateRequestDTO.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Record updated successfully.",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))
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
    public RouterFunction<ServerResponse> usersRouters() {
        return RouterFunctions
                .route(POST("/api/auth/register").and(accept(APPLICATION_JSON)), this::saveUser)
                .andRoute(PUT("/api/auth/user-update").and(accept(APPLICATION_JSON)), this::updateUser);
    }

    public Mono<ServerResponse> saveUser(ServerRequest request) {
        return request.bodyToMono(UserRequestDTO.class)
                .doOnNext(requestValidator::validate)
                .flatMap(userHandler::save)
                .flatMap(response ->
                        ServerResponse.ok().contentType(APPLICATION_JSON).bodyValue(response));

    }

    public Mono<ServerResponse> updateUser(ServerRequest request) {
        return request.bodyToMono(UserUpdateRequestDTO.class)
                .doOnNext(requestValidator::validate)
                .flatMap(userHandler::update)
                .flatMap(response ->
                        ServerResponse.ok().contentType(APPLICATION_JSON).bodyValue(response));

    }

}
