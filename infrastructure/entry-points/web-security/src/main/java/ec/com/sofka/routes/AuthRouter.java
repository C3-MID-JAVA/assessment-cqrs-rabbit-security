package ec.com.sofka.routes;

import ec.com.sofka.ErrorDetails;
import ec.com.sofka.data.AuthRequest;
import ec.com.sofka.data.AuthResponse;
import ec.com.sofka.data.CreateUserRequest;
import ec.com.sofka.exceptions.BodyRequestValidator;
import ec.com.sofka.exceptions.GlobalExceptionsHandler;
import ec.com.sofka.handlers.AuthHandler;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class AuthRouter {
    private final AuthHandler authHandler;
    private final BodyRequestValidator bodyRequestValidator;
    private final GlobalExceptionsHandler globalExceptionsHandler;

    public AuthRouter(AuthHandler authHandler, BodyRequestValidator bodyRequestValidator, GlobalExceptionsHandler globalExceptionsHandler) {
        this.authHandler = authHandler;
        this.bodyRequestValidator = bodyRequestValidator;
        this.globalExceptionsHandler = globalExceptionsHandler;
    }


    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/user/create",
                    method = RequestMethod.POST,
                    operation = @Operation(
                            tags = {"Users"},
                            operationId = "create",
                            summary = "Create a new user",
                            description = "Creates a new user with username and password",
                            requestBody = @RequestBody(
                                    description = "User creation details",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = CreateUserRequest.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "User successfully created",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateUserRequest.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Bad request, validation error or missing required fields",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Username could already exist",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/user/authenticate",
                    method = RequestMethod.POST,
                    operation = @Operation(
                            tags = {"Users"},
                            operationId = "login",
                            summary = "Authenticate an user",
                            description = "Authenticates an user with username and password",
                            requestBody = @RequestBody(
                                    description = "Authenticate credentials",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = AuthRequest.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "User successfully authenticate",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Bad request, validation error or missing required fields",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Invalid Credentials",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Invalid Credentials",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Invalid Credentials",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> userRoutes() {
        return RouterFunctions
                .route(RequestPredicates.POST("/api/v1/user/create")
                        .and(accept(MediaType.APPLICATION_JSON)), this::createUser)
                .andRoute(RequestPredicates.POST("/api/v1/user/authenticate")
                        .and(accept(MediaType.APPLICATION_JSON)), this::login);
    }


    public Mono<ServerResponse> createUser(ServerRequest request) {

        return request.bodyToMono(CreateUserRequest.class)
                .doOnNext(bodyRequestValidator::validate)
                .flatMap(authHandler::createUser)
                .flatMap(userDTO -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON).bodyValue(userDTO))
                .onErrorResume(globalExceptionsHandler::handleException);

    }

    public Mono<ServerResponse> login(ServerRequest request) {

        return request.bodyToMono(AuthRequest.class)
                .doOnNext(bodyRequestValidator::validate)
                .flatMap(authHandler::authenticate)
                .flatMap(authResponse -> ServerResponse.status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON).bodyValue(authResponse))
                .onErrorResume(globalExceptionsHandler::handleException);

    }
}
