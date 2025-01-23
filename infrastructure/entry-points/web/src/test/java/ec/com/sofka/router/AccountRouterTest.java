package ec.com.sofka.router;

import ec.com.sofka.data.AccountReqByElementDTO;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.Matchers.is;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.mockito.Mockito.*;
class AccountRouterTest {

    private WebTestClient webTestClient;

    @Mock
    private AccountHandler accountHandler;

    @Mock
    private ValidationService validationService;

    @Mock
    private GlobalErrorHandler globalErrorHandler;

    @InjectMocks
    private AccountRouter accountRouter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        webTestClient = WebTestClient.bindToRouterFunction(accountRouter.accountRoutes()).build();
    }

    @Test
    void createAccount_ShouldReturnCreated() {
        // Arrange
        AccountRequestDTO requestDTO = new AccountRequestDTO("0123456789", BigDecimal.valueOf(500), "John Doe", "active");
        AccountResponseDTO responseDTO = new AccountResponseDTO("accountId123", "John Doe", "0123456789", BigDecimal.valueOf(500), "active");

        when(validationService.validate(any(), eq(AccountRequestDTO.class))).thenReturn(Mono.just(requestDTO));
        when(accountHandler.createAccount(any(AccountRequestDTO.class))).thenReturn(Mono.just(responseDTO));

        // Act and Assert
        webTestClient.post()
                .uri("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.accountId").isEqualTo("accountId123")
                .jsonPath("$.owner").isEqualTo("John Doe")
                .jsonPath("$.balance").isEqualTo(500);
    }


    @Test
    void updateAccount_ShouldReturnCreated() {
        // Arrange
        AccountRequestDTO requestDTO = new AccountRequestDTO("0123456789", BigDecimal.valueOf(500), "John Doe", "active");
        AccountResponseDTO responseDTO = new AccountResponseDTO("accountId123", "John Doe", "0123456789", BigDecimal.valueOf(500), "active");

        when(validationService.validate(any(), eq(AccountRequestDTO.class))).thenReturn(Mono.just(requestDTO));
        when(accountHandler.updateAccount(any(AccountRequestDTO.class))).thenReturn(Mono.just(responseDTO));

        // Act and Assert
        webTestClient.post()
                .uri("/api/v1/update")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.accountId").isEqualTo("accountId123")
                .jsonPath("$.owner").isEqualTo("John Doe")
                .jsonPath("$.balance").isEqualTo(500);
    }

    @Test
    void getAccountByAccountNumber_ShouldReturnOk() {
        // Arrange
        String accountNumber = "0123456789";
        AccountResponseDTO responseDTO = new AccountResponseDTO("123", "accountId123", "John Doe", "0123456789", BigDecimal.valueOf(500), "active");

        when(accountHandler.getAccountByNumber(any(AccountReqByElementDTO.class))).thenReturn(Mono.just(responseDTO));

        // Act and Assert
        webTestClient.post()
                .uri("/api/v1/accounts/accountNumber")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new AccountReqByElementDTO("123", accountNumber, null))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.accountId").isEqualTo("accountId123")
                .jsonPath("$.owner").isEqualTo("John Doe")
                .jsonPath("$.balance").isEqualTo(500);
    }

    @Test
    void listAccounts_ShouldReturnOk() {
        // Arrange
        AccountResponseDTO account1 = new AccountResponseDTO("123", "accountId123", "John Doe", "0123456789", BigDecimal.valueOf(500), "active");
        AccountResponseDTO account2 = new AccountResponseDTO("124", "accountId124", "Jane Doe", "0123456790", BigDecimal.valueOf(1000), "active");

        when(accountHandler.getAllAccounts()).thenReturn(Flux.fromIterable(List.of(account1, account2)));

        // Act and Assert
        webTestClient.get()
                .uri("/api/v1/accounts/getAll")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].accountId").isEqualTo("accountId123")
                .jsonPath("$[1].accountId").isEqualTo("accountId124");
    }

}
