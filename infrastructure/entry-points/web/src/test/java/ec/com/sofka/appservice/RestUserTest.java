package ec.com.sofka.appservice;

import ec.com.sofka.RestUser;
import ec.com.sofka.data.user.AuthResponseDTO;
import ec.com.sofka.data.user.UserRequestDTO;
import ec.com.sofka.data.user.UserResponseDTO;
import ec.com.sofka.handlers.UserHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public class RestUserTest {

    private WebTestClient webTestClient;

    @Mock
    private UserHandler handler;

    @InjectMocks
    private RestUser restUser;

    private UserRequestDTO requestDTO;
    private UserResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        webTestClient = WebTestClient.bindToRouterFunction(restUser.userRoutes()).build();
        requestDTO = new UserRequestDTO("test@example.com", "Password1!");
        responseDTO = new UserResponseDTO("test@example.com");
    }

    @Test
    void testRegisterUser() {
        when(handler.create(any(UserRequestDTO.class))).thenReturn(Mono.just(responseDTO));

        webTestClient.post()
                .uri("/user/register")
                .contentType(APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserResponseDTO.class);
    }

    @Test
    void testRegisterUserInvalidRequest() {
        UserRequestDTO invalidRequestDTO = new UserRequestDTO(null, null);

        webTestClient.post()
                .uri("/user/register")
                .contentType(APPLICATION_JSON)
                .bodyValue(invalidRequestDTO)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testLoginUser() {
        when(handler.login(any(UserRequestDTO.class))).thenReturn(Mono.just(new AuthResponseDTO("token")));

        webTestClient.post()
                .uri("/user/login")
                .contentType(APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AuthResponseDTO.class);
    }
/*
    @Test
    void testLoginUserInvalidRequest() {
        UserRequestDTO invalidRequestDTO = new UserRequestDTO(null, null);

        webTestClient.post()
                .uri("/user/login")
                .contentType(APPLICATION_JSON)
                .bodyValue(invalidRequestDTO)
                .exchange()
                .expectStatus().isBadRequest();
    }
*/
    @Test
    void testLoginUserUnauthorized() {
        when(handler.login(any(UserRequestDTO.class))).thenReturn(Mono.error(new RuntimeException("Unauthorized")));

        webTestClient.post()
                .uri("/user/login")
                .contentType(APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody(String.class)
                .isEqualTo("Error logging in: Unauthorized");
    }
}