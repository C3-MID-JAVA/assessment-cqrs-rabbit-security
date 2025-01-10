package e.com.sofka.router;

import ec.com.sofka.dto.UserRequestDTO;
import ec.com.sofka.exception.ConflictException;
import ec.com.sofka.exception.GlobalExceptionHandler;
import ec.com.sofka.handler.UserHandler;
import ec.com.sofka.router.UserRouter;
import ec.com.sofka.user.CreateUserUseCase;
import ec.com.sofka.user.GetAllUserUseCase;
import ec.com.sofka.user.request.CreateUserRequest;
import ec.com.sofka.user.responses.UserResponse;
import ec.com.sofka.validator.RequestValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebFluxTest
@ContextConfiguration(classes = {UserRouter.class, UserHandler.class, RequestValidator.class, GlobalExceptionHandler.class})
public class UserRouterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private CreateUserUseCase createUserUseCase;

    @MockitoBean
    private GetAllUserUseCase getAllUseCase;

    private UserRequestDTO validUserRequest;

    @BeforeEach
    void init() {
        validUserRequest = new UserRequestDTO("John Doe", "12345678");
    }

    @Test
    void register_ValidUser_ReturnsCreatedResponse() {
        UserResponse userResponse = new UserResponse("675e0e1259d6de4eda5b29b7", "John Doe", "12345678");
        when(createUserUseCase.execute(any(CreateUserRequest.class))).thenReturn(Mono.just(userResponse));

        webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validUserRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.customerId").isEqualTo("675e0e1259d6de4eda5b29b7")
                .jsonPath("$.name").isEqualTo("John Doe")
                .jsonPath("$.documentId").isEqualTo("12345678");

        verify(createUserUseCase, times(1)).execute(any(CreateUserRequest.class));
    }

    @Test
    void register_DuplicateUser_ReturnsBadRequest() {
        when(createUserUseCase.execute(any(CreateUserRequest.class)))
                .thenReturn(Mono.error(new ConflictException("Document ID already exists.")));

        webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validUserRequest)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                .expectBody()
                .jsonPath("$.error").isEqualTo("Document ID already exists.");

        verify(createUserUseCase, times(1)).execute(any(CreateUserRequest.class));
    }

    @Test
    void register_EmptyDocumentId_ReturnsBadRequest() {
        UserRequestDTO invalidUserRequest = new UserRequestDTO();
        invalidUserRequest.setDocumentId("");
        invalidUserRequest.setName("John Doe");

        webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidUserRequest)
                .exchange()
                .expectStatus().isBadRequest();
    }
}
