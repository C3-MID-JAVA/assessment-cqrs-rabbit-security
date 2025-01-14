// Este archivo define las rutas (endpoints) del módulo de administración utilizando programación funcional en Spring WebFlux.
// También incluye anotaciones de OpenAPI para documentar los endpoints automáticamente con Swagger.

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

    // Inyección del handler encargado de implementar la lógica de negocio para los endpoints.
    public AdminRouter(AdminHandler adminHandler) {
        this.adminHandler = adminHandler;
    }

    @Bean
    @RouterOperations({
            // Anotación de OpenAPI para el endpoint "/admin/register".
            @RouterOperation(
                    path = "/admin/register",
                    operation = @Operation(
                            tags = {"Admin"}, // Etiqueta para agrupar endpoints en Swagger.
                            operationId = "registerAdmin", // Identificador único para este endpoint en la documentación.
                            summary = "Register a new admin", // Breve descripción de la funcionalidad.
                            description = "This endpoint registers a new admin by providing a valid email and password.", // Detalles del propósito del endpoint.
                            requestBody = @RequestBody(
                                    description = "Admin registration details", // Explicación de los datos esperados en el cuerpo de la solicitud.
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json", // Tipo de contenido esperado.
                                            schema = @Schema(implementation = AdminRequestDTO.class) // Clase que define la estructura del cuerpo.
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201", // Respuesta esperada en caso de éxito.
                                            description = "Admin successfully registered",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = AdminResponse.class) // Clase que representa la respuesta de éxito.
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400", // Respuesta para un error de solicitud mal formada.
                                            description = "Bad request, invalid email or password format",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ErrorResponse.class) // Clase que describe errores genéricos.
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "409", // Error por conflicto (email ya registrado).
                                            description = "Conflict, admin with the provided email already exists",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ErrorResponse.class)
                                            )
                                    )
                            }
                    )
            ),
            // Anotación de OpenAPI para el endpoint "/admin/login".
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
                                            responseCode = "201", // Éxito: inicio de sesión correcto.
                                            description = "Login successful, token returned",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = AdminResponse.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400", // Error por solicitud mal formada.
                                            description = "Bad request, invalid email or password format",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ErrorResponse.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "404", // Error: admin no encontrado.
                                            description = "Admin not found, no admin exists with the provided email",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ErrorResponse.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "403", // Error: contraseña incorrecta.
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
        // Configuración de las rutas para los endpoints de administración.
        return RouterFunctions
                .route(RequestPredicates.POST("/admin/register"), adminHandler::create) // Ruta para el registro.
                .andRoute(RequestPredicates.POST("/admin/login"), adminHandler::login); // Ruta para el login.
    }
}
