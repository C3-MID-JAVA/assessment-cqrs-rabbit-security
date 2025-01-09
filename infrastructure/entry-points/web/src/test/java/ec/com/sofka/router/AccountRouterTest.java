package ec.com.sofka.router;

import ec.com.sofka.data.AccountRequestDTO;
import ec.com.sofka.data.AccountResponseDTO;
import ec.com.sofka.handler.AccountHandler;
import ec.com.sofka.service.ValidationService;
import ec.com.sofka.globalexceptions.GlobalErrorHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

import static java.util.EnumSet.allOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

class AccountRouterTest {

    @Mock
    private AccountHandler accountHandler;

    @Mock
    private ValidationService validationService;

    @Mock
    private GlobalErrorHandler globalErrorHandler;

    @InjectMocks
    private AccountRouter accountRouter;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        webTestClient = WebTestClient.bindToRouterFunction(accountRouter.accountRoutes()).build();
    }

    @Test
    void testCreateAccount() {
        // Arrange
        AccountRequestDTO accountRequestDTO = new AccountRequestDTO("0123456789", new BigDecimal("5000.00"), "Anderson Zambrano");
        AccountResponseDTO accountResponseDTO = new AccountResponseDTO("1", "0123456789", new BigDecimal("5000.00"), "Anderson Zambrano");

        when(validationService.validate(any(), eq(AccountRequestDTO.class))).thenReturn(Mono.just(accountRequestDTO));
        when(accountHandler.createAccount(accountRequestDTO)).thenReturn(Mono.just(accountResponseDTO));

        // Act & Assert
        webTestClient.post()
                .uri("/accounts")
                .contentType(APPLICATION_JSON)
                .bodyValue(accountRequestDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.accountNumber").isEqualTo("0123456789")
                .jsonPath("$.owner").isEqualTo("Anderson Zambrano")
                .jsonPath("$.balance").isEqualTo(5000.00);
    }

    @Test
    void testGetAccountByAccountNumber() {
        // Arrange
        String accountNumber = "0123456789";
        AccountResponseDTO accountResponseDTO = new AccountResponseDTO("1", accountNumber, new BigDecimal("5000.00"), "Anderson Zambrano");

        when(accountHandler.getAccountByAccountNumber(accountNumber)).thenReturn(Mono.just(accountResponseDTO));

        // Act & Assert
        webTestClient.get()
                .uri("/accounts/accountNumber/{accountNumber}", accountNumber)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.accountNumber").isEqualTo(accountNumber)
                .jsonPath("$.owner").isEqualTo("Anderson Zambrano")
                .jsonPath("$.balance").isEqualTo(5000.00);
    }

    @Test
    void testListAccounts() {
        // Arrange
        AccountResponseDTO accountResponseDTO = new AccountResponseDTO("1", "0123456789", new BigDecimal("5000.00"), "Anderson Zambrano");
        AccountResponseDTO accountResponseDTO2 = new AccountResponseDTO("1", "0123456789", new BigDecimal("5000.00"), "Anderson Zambrano");

        when(accountHandler.getAccounts()).thenReturn(Flux.just(accountResponseDTO,accountResponseDTO2));

        // Act & Assert
        webTestClient.get()
                .uri("/accounts/getAll")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBodyList(AccountResponseDTO.class)
                .hasSize(2)
                .consumeWith(response -> {
                    List<AccountResponseDTO> actualResponse = response.getResponseBody();
                    assert actualResponse != null;
                    assertEquals(accountResponseDTO.getAccountNumber(), actualResponse.get(0).getAccountNumber());
                    assertEquals(accountResponseDTO2.getAccountNumber(), actualResponse.get(1).getAccountNumber());
                });
    }


    @Test
    void testGetAccountById() {
        // Arrange
        String accountId = "1";
        AccountResponseDTO accountResponseDTO = new AccountResponseDTO(accountId, "0123456789", new BigDecimal("5000.00"), "Anderson Zambrano");
        when(accountHandler.getAccountByAccountId(accountId)).thenReturn(Mono.just(accountResponseDTO));

        // Act & Assert
        webTestClient.get()
                .uri("/accounts/{accountId}", accountId)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.accountNumber").isEqualTo("0123456789")
                .jsonPath("$.owner").isEqualTo("Anderson Zambrano")
                .jsonPath("$.balance").isEqualTo(5000.00);
    }

    @Test
    void testGetAccountBalance() {
        // Arrange
        String accountId = "1";
        BigDecimal balance = new BigDecimal("5000.00");
        when(accountHandler.getCheckBalance(accountId)).thenReturn(Mono.just(balance));

        // Act & Assert
        webTestClient.get()
                .uri("/accounts/{accountId}/balance", accountId)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$").isEqualTo(5000.00);
    }

}
