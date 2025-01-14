package ec.com.sofka;

import ec.com.sofka.dto.UserRequestDTO;
import ec.com.sofka.exception.GlobalExceptionHandler;
import ec.com.sofka.handler.UserHandler;
import ec.com.sofka.router.UserRouter;
import ec.com.sofka.user.commands.CreateUserCommand;
import ec.com.sofka.user.commands.usecases.CreateUserUseCase;
import ec.com.sofka.user.queries.responses.UserResponse;
import ec.com.sofka.user.queries.usecases.GetAllUserViewUseCase;
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
        UserRouter.class,
        UserHandler.class,
        RequestValidator.class,
        GlobalExceptionHandler.class,
        SecurityConfig.class,
        JwtAuthFilter.class
})
public class SecurityTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private JwtServiceAdapter jwtService;

    @MockitoBean
    private CreateUserUseCase createUserUseCase;

    @MockitoBean
    private GetAllUserViewUseCase getAllUseCase;

    private UserRequestDTO validUserCommand;

    @BeforeEach
    void init() {
        validUserCommand = new UserRequestDTO("John Doe", "12345678");

        when(jwtService.isTokenValid("super-user-token")).thenReturn(true);
        when(jwtService.isTokenValid("user-token")).thenReturn(true);
        when(jwtService.isTokenValid("invalid-token")).thenReturn(false);

        when(jwtService.extractUsername("super-user-token")).thenReturn("super-user");
        when(jwtService.extractRole("super-user-token")).thenReturn("SUPER_USER");

        when(jwtService.extractUsername("user-token")).thenReturn("user");
        when(jwtService.extractRole("user-token")).thenReturn("USER");
    }

    @Test
    void shouldAllowPostForSuperUserRole() {
        when(createUserUseCase.execute(any(CreateUserCommand.class)))
                .thenReturn(Mono.just(new UserResponse("1", "John Doe", "12345678")));

        webTestClient.post()
                .uri("/users")
                .headers(headers -> headers.setBearerAuth("super-user-token"))  // Cambiado a super-user-token
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validUserCommand)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo("1");

        verify(createUserUseCase, times(1)).execute(any(CreateUserCommand.class));
    }

    @Test
    void shouldForbidPostForUserRole() {
        webTestClient.post()
                .uri("/accounts")
                .headers(headers -> headers.setBearerAuth("user-token"))  // Cambiado a user-token
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validUserCommand)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .jsonPath("$.error").isEqualTo("Unauthorized access");

        verify(createUserUseCase, never()).execute(any(CreateUserCommand.class));
    }

    @Test
    void shouldReturnUnauthorizedForInvalidToken() {
        webTestClient.post()
                .uri("/accounts")
                .headers(headers -> headers.setBearerAuth("invalid-token"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validUserCommand)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .jsonPath("$.error").isEqualTo("Unauthorized access");

        verify(createUserUseCase, never()).execute(any(CreateUserCommand.class));
    }

    @Test
    void shouldReturnUnauthorizedWhenNoTokenProvided() {
        webTestClient.post()
                .uri("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validUserCommand)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .jsonPath("$.error").isEqualTo("Unauthorized access");

        verify(createUserUseCase, never()).execute(any(CreateUserCommand.class));
    }
}
