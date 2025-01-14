package ec.com.sofka.routes;

import ec.com.sofka.ErrorDetails;
import ec.com.sofka.data.AuthRequest;
import ec.com.sofka.data.AuthResponse;
import ec.com.sofka.data.CreateUserRequest;
import ec.com.sofka.exceptions.BodyRequestValidator;
import ec.com.sofka.exceptions.GlobalExceptionsHandler;

import ec.com.sofka.handlers.AuthHandler;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class AuthRouterTest {

    @Mock
    private AuthHandler authHandler;

    @Mock
    private BodyRequestValidator bodyRequestValidator;

    @Mock
    private GlobalExceptionsHandler globalExceptionsHandler;

    @InjectMocks
    private AuthRouter authRouter;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        webTestClient = WebTestClient.bindToRouterFunction(authRouter.userRoutes()).build();
    }

    @Test
    void testCreateUser_Success() {

        CreateUserRequest request = new CreateUserRequest("username", "password", "admin");
        when(authHandler.createUser(any())).thenReturn(Mono.just(request));


        webTestClient.post()
                .uri("/api/v1/user/create")
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CreateUserRequest.class)
                .value(response -> {
                    assert response.getUsername().equals(request.getUsername());
                });
    }

    @Test
    void testLogin_Success() {

        AuthRequest request = new AuthRequest("username", "password");
        AuthResponse mockToken = new AuthResponse("token");
        when(authHandler.authenticate(any())).thenReturn(Mono.just(mockToken));


        webTestClient.post()
                .uri("/api/v1/user/authenticate")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AuthResponse.class)
                .value(response -> assertEquals(mockToken.getToken(), response.getToken()));
    }

    @Test
    void testCreateUser_BadRequest() {
        CreateUserRequest request = new CreateUserRequest("", "123", "123");


        ErrorDetails errorDetails = new ErrorDetails(new Date(), "SOME FIELD(s) IN THE REQUEST HAS ERROR",
                List.of("username: username can not be null"));

        doThrow(new ValidationException("username can not be null"))
                .when(bodyRequestValidator).validate(any(CreateUserRequest.class));

        when(globalExceptionsHandler.handleException(any(ValidationException.class)))
                .thenReturn(
                        ServerResponse.badRequest()
                                .bodyValue(
                                        errorDetails)
                );

        webTestClient
                .post()
                .uri("/api/v1/user/create")
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorDetails.class);

        verify(authHandler, times(0)).createUser(any(CreateUserRequest.class));
        verify(globalExceptionsHandler, times(1)).handleException(any(ValidationException.class));
    }
}