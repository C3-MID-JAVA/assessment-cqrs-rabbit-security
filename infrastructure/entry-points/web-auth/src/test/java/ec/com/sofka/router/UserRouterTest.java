package ec.com.sofka.router;

import ec.com.sofka.UserRouter;
import ec.com.sofka.data.UserRequestDTO;
import ec.com.sofka.data.UserResponseDTO;
import ec.com.sofka.data.UserUpdateRequestDTO;
import ec.com.sofka.exceptions.RequestValidationException;
import ec.com.sofka.handler.UserAuthHandler;
import ec.com.sofka.utils.enums.RoleEnum;
import ec.com.sofka.validator.RequestValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserRouterTest {
    @Mock
    private RequestValidator requestValidator;

    @Mock
    private UserAuthHandler userAuthHandler;

    @InjectMocks
    private UserRouter userRouter;

    private WebTestClient webTestClient;

    private UserRequestDTO userRequestDTO;
    private UserUpdateRequestDTO userUpdateRequestDTO;
    private UserResponseDTO userResponseDTO;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToRouterFunction(userRouter.usersRouters()).build();

        userRequestDTO = new UserRequestDTO("user@gmail.com", "Customer123.", "Jhon", "Doe", RoleEnum.ROLE_EXECUTIVE.name());
        userUpdateRequestDTO = new UserUpdateRequestDTO("U1", "user@gmail.com", "Customer123.", "Jhon", "Doe", RoleEnum.ROLE_EXECUTIVE.name());
        userResponseDTO = new UserResponseDTO("Jhon", "Doe", "user@gmail.com", "ACTIVE");

    }

    @Test
    @DisplayName("Should create User when request passes to validation")
    void shouldCreateUserSuccessfully() {
        when(userAuthHandler.save(any())).thenReturn(Mono.just(userResponseDTO));

        webTestClient.post()
                .uri("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userRequestDTO)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.email").isEqualTo(userRequestDTO.getEmail())
                .jsonPath("$.firstName").isEqualTo(userRequestDTO.getFirstName());
    }

    @Test
    @DisplayName("Should update User when request passes to validation")
    void shouldUpdateUserSuccessfully() {
        when(userAuthHandler.update(any())).thenReturn(Mono.just(userResponseDTO));

        webTestClient.put()
                .uri("/api/auth/user-update")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userUpdateRequestDTO)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.email").isEqualTo(userUpdateRequestDTO.getEmail())
                .jsonPath("$.firstName").isEqualTo(userUpdateRequestDTO.getFirstName());
    }

    @Test
    @DisplayName("Should return validation errors when exist error in create")
    void shouldCreateReturnValidationErrors() {
        UserRequestDTO invalidRequest = new UserRequestDTO(null, "Customer123.", "Jhon", "Doe", RoleEnum.ROLE_EXECUTIVE.name());

        doThrow(new RequestValidationException(List.of("email cannot be null")))
                .when(requestValidator).validate(any(UserUpdateRequestDTO.class));


        webTestClient.post()
                .uri("/api/auth/register")
                .bodyValue(invalidRequest)
                .exchange()
                .expectStatus().isEqualTo(500);
    }

    @Test
    @DisplayName("Should return validation errors when exist error in update")
    void shouldUpdateReturnValidationErrors() {
        UserUpdateRequestDTO invalidRequest = new UserUpdateRequestDTO("u1", null, "Customer123.", "Jhon", "Doe", RoleEnum.ROLE_EXECUTIVE.name());

        doThrow(new RequestValidationException(List.of("email cannot be null")))
                .when(requestValidator).validate(any(UserRequestDTO.class));


        webTestClient.put()
                .uri("/api/auth/user-update")
                .bodyValue(invalidRequest)
                .exchange()
                .expectStatus().isEqualTo(500);
    }

}