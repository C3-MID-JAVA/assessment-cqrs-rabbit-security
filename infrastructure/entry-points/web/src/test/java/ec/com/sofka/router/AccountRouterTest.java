package ec.com.sofka.router;

import ec.com.sofka.data.AccountRequestDTO;
import ec.com.sofka.data.AccountResponseDTO;
import ec.com.sofka.exceptions.RequestValidationException;
import ec.com.sofka.handlers.account.CreateAccountHandler;
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

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountRouterTest {
    @Mock
    private RequestValidator requestValidator;

    @Mock
    private CreateAccountHandler createAccountHandler;

    @InjectMocks
    private AccountRouter accountRouter;

    private WebTestClient webTestClient;

    private AccountRequestDTO accountRequest;
    private AccountResponseDTO accountResponse;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToRouterFunction(accountRouter.accountsRouters()).build();

        accountRequest = new AccountRequestDTO("2200000000", new BigDecimal(100), "ACTIVE", "u1");
        accountResponse = new AccountResponseDTO("2200000000", new BigDecimal(100), "ACTIVE", null);

    }

    @Test
    @DisplayName("Should create account when request passes to validation")
    void shouldCreateAccountSuccessfully() {
        when(createAccountHandler.save(any())).thenReturn(Mono.just(accountResponse));

        webTestClient.post()
                .uri("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(accountRequest)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.accountNumber").isEqualTo(accountRequest.getAccountNumber())
                .jsonPath("$.balance").isEqualTo(accountRequest.getBalance());
    }

    @Test
    @DisplayName("Should return validation errors when exist error")
    void shouldReturnValidationErrors() {
        AccountRequestDTO invalidRequest = new AccountRequestDTO(null , new BigDecimal(100), "ACTIVE", "u1");

        doThrow(new RequestValidationException(List.of("number cannot be null")))
                .when(requestValidator).validate(any(AccountRequestDTO.class));

        webTestClient.post()
                .uri("/api/accounts")
                .bodyValue(invalidRequest)
                .exchange()
                .expectStatus().isEqualTo(500);
    }

}