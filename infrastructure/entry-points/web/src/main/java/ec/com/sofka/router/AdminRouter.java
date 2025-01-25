package ec.com.sofka.router;

import ec.com.sofka.dto.AdminRequestDTO;
import ec.com.sofka.exception.ErrorResponse;
import ec.com.sofka.handler.AdminHandler;
import ec.com.sofka.usecases.responses.AdminResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class AdminRouter {

    private final AdminHandler adminHandler;

    public AdminRouter(AdminHandler adminHandler) {
        this.adminHandler = adminHandler;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/admin/register",
                    operation = @Operation(
                            tags = {"Admin"},
                            operationId = "registerAdmin",
                            summary = "Register a new admin",
                            description = "This endpoint registers a new admin by providing a valid email and password.",
                            requestBody = @RequestBody(
                                    description = "Admin registration details",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = AdminRequestDTO.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Admin successfully registered",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = AdminResponse.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Bad request, invalid email or password format",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ErrorResponse.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "409",
                                            description = "Conflict, admin with the provided email already exists",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ErrorResponse.class)
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/admin/login",
                    operation = @Operation(
                            tags = {"Admin"},
                            operationId = "loginAdmin",
                            summary = "Login an admin",
                            description = "This endpoint allows an admin to log in using their email and password.",
                            requestBody = @RequestBody(
                                    description = "Admin login details",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = AdminRequestDTO.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Login successful, token returned",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = AdminResponse.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Bad request, invalid email or password format",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ErrorResponse.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "Admin not found, no admin exists with the provided email",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ErrorResponse.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "403",
                                            description = "Access denied, incorrect password",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ErrorResponse.class)
                                            )
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> adminRoutes() {
        return RouterFunctions
                .route(RequestPredicates.POST("/admin/register"), adminHandler::create)
                .andRoute(RequestPredicates.POST("/admin/login"), adminHandler::login);
    }
}
