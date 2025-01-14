package ec.com.sofka.router;

import ec.com.sofka.ConflictException;
import ec.com.sofka.JwtAuthFilter;
import ec.com.sofka.JwtServiceAdapter;
import ec.com.sofka.SecurityConfig;
import ec.com.sofka.dto.UserRequestDTO;
import ec.com.sofka.exception.GlobalExceptionHandler;
import ec.com.sofka.generics.utils.QueryResponse;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

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
public class UserRouterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private CreateUserUseCase createUserUseCase;

    @MockitoBean
    private GetAllUserViewUseCase getAllUseCase;

    @MockitoBean
    private JwtServiceAdapter jwtService;

    private UserRequestDTO validUserCommand;

    private String authToken;

    @BeforeEach
    void init() {
        validUserCommand = new UserRequestDTO("John Doe", "12345678");
        when(jwtService.generateToken(anyString(), anyString())).thenReturn("test-token");
        when(jwtService.isTokenValid(anyString())).thenReturn(true);
        when(jwtService.extractUsername(anyString())).thenReturn("test-user");
        when(jwtService.extractRole(anyString())).thenReturn("SUPER_USER");

        authToken = jwtService.generateToken("test-user", "SUPER_USER");
    }

    @Test
    void register_ValidUser_ReturnsCreatedResponse() {
        UserResponse userResponse = new UserResponse("b99a7817bbec44f0a0459a8d", "John Doe", "12345678");
        when(createUserUseCase.execute(any(CreateUserCommand.class))).thenReturn(Mono.just(userResponse));

        webTestClient.post()
                .uri("/users")
                .headers(headers -> headers.setBearerAuth(authToken))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validUserCommand)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo("b99a7817bbec44f0a0459a8d")
                .jsonPath("$.name").isEqualTo("John Doe")
                .jsonPath("$.documentId").isEqualTo("12345678");

        verify(createUserUseCase, times(1)).execute(any(CreateUserCommand.class));
    }

    @Test
    void register_DuplicateUser_ReturnsBadCommand() {
        when(createUserUseCase.execute(any(CreateUserCommand.class)))
                .thenReturn(Mono.error(new ConflictException("Document ID already exists.")));

        webTestClient.post()
                .uri("/users")
                .headers(headers -> headers.setBearerAuth(authToken))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validUserCommand)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                .expectBody()
                .jsonPath("$.error").isEqualTo("Document ID already exists.");

        verify(createUserUseCase, times(1)).execute(any(CreateUserCommand.class));
    }

    @Test
    void register_EmptyDocumentId_ReturnsBadRequest() {
        UserRequestDTO invalidUserRequest = new UserRequestDTO();
        invalidUserRequest.setDocumentId("");
        invalidUserRequest.setName("John Doe");

        webTestClient.post()
                .uri("/users")
                .headers(headers -> headers.setBearerAuth(authToken))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidUserRequest)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void getAll_ReturnsUserListSuccessfully() {
        List<UserResponse> mockUsers = List.of(
                new UserResponse("1", "John Doe", "12345678"),
                new UserResponse("2", "Jane Doe", "87654321")
        );

        QueryResponse<UserResponse> mockQueryResponse = QueryResponse.ofMultiple(mockUsers);
        when(getAllUseCase.get()).thenReturn(Mono.just(mockQueryResponse));

        webTestClient.get()
                .uri("/users")
                .headers(headers -> headers.setBearerAuth(authToken))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$[0].id").isEqualTo("1")
                .jsonPath("$[0].name").isEqualTo("John Doe")
                .jsonPath("$[0].documentId").isEqualTo("12345678")
                .jsonPath("$[1].id").isEqualTo("2")
                .jsonPath("$[1].name").isEqualTo("Jane Doe")
                .jsonPath("$[1].documentId").isEqualTo("87654321");

        verify(getAllUseCase, times(1)).get();
    }
}
