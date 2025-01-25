package ec.com.sofka.router;

import ec.com.sofka.JwtAuthFilter;
import ec.com.sofka.JwtServiceAdapter;
import ec.com.sofka.ROLE;
import ec.com.sofka.SecurityConfig;
import ec.com.sofka.dto.AdminRequestDTO;
import ec.com.sofka.exception.GlobalExceptionHandler;
import ec.com.sofka.handler.AdminHandler;
import ec.com.sofka.usecases.LoginAdminUseCase;
import ec.com.sofka.usecases.RegisterAdminUseCase;
import ec.com.sofka.usecases.command.LoginAdminCommand;
import ec.com.sofka.usecases.command.RegisterAdminCommand;
import ec.com.sofka.usecases.responses.AdminResponse;
import ec.com.sofka.validator.RequestValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebFluxTest
@ContextConfiguration(classes = {
        AdminRouter.class,
        AdminHandler.class,
        RequestValidator.class,
        GlobalExceptionHandler.class,
        SecurityConfig.class,
        JwtAuthFilter.class,
        JwtServiceAdapter.class
})
public class AdminRouterTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private RegisterAdminUseCase registerAdminUseCase;

    @MockitoBean
    private LoginAdminUseCase loginAdminUseCase;

    private AdminRequestDTO validAdminCommand;

    @BeforeEach
    void init() {
        validAdminCommand = new AdminRequestDTO("admin@test.com", "securePassword123*", "GOD");
    }

    @Test
    void create_ValidAdmin_ReturnsCreatedResponse() {
        AdminResponse adminResponse = new AdminResponse("123456", "admin@test.com", "generated-jwt-token");
        when(registerAdminUseCase.execute(any(RegisterAdminCommand.class))).thenReturn(Mono.just(adminResponse));

        webTestClient.post()
                .uri("/admin/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validAdminCommand)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo("123456")
                .jsonPath("$.email").isEqualTo("admin@test.com")
                .jsonPath("$.token").isEqualTo("generated-jwt-token");

        verify(registerAdminUseCase, times(1)).execute(any(RegisterAdminCommand.class));
    }

    @Test
    void register_InvalidPassword_ReturnsBadRequestWithValidationMessages() {
        RegisterAdminCommand invalidPasswordCommand = new RegisterAdminCommand("bad_email", "weak", ROLE.ADMIN);

        webTestClient.post()
                .uri("/admin/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidPasswordCommand)
                .exchange()
                .expectStatus().isBadRequest();

        verify(registerAdminUseCase, never()).execute(any(RegisterAdminCommand.class));
    }

    @Test
    void login_ValidAdmin_ReturnsCreatedResponse() {
        AdminResponse adminResponse = new AdminResponse("123456", "admin@test.com", "generated-jwt-token");
        when(loginAdminUseCase.execute(any(LoginAdminCommand.class))).thenReturn(Mono.just(adminResponse));

        webTestClient.post()
                .uri("/admin/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validAdminCommand)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo("123456")
                .jsonPath("$.email").isEqualTo("admin@test.com")
                .jsonPath("$.token").isEqualTo("generated-jwt-token");

        verify(loginAdminUseCase, times(1)).execute(any(LoginAdminCommand.class));
    }

    @Test
    void login_InvalidPassword_ReturnsBadRequestWithValidationMessages() {
        LoginAdminCommand invalidPasswordCommand = new LoginAdminCommand("bad_email", "weak");

        webTestClient.post()
                .uri("/admin/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidPasswordCommand)
                .exchange()
                .expectStatus().isBadRequest();

        verify(loginAdminUseCase, never()).execute(any(LoginAdminCommand.class));
    }
}
