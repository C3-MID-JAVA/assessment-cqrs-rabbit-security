package e.com.sofka.router;

import ec.com.sofka.account.CreateAccountUseCaseExecute;
import ec.com.sofka.account.GetAccountByNumberUseCaseExecute;
import ec.com.sofka.account.GetAllByUserIdUseCase;
import ec.com.sofka.account.request.CreateAccountRequest;
import ec.com.sofka.account.request.GetAccountByNumberRequest;
import ec.com.sofka.account.request.GetAllByUserIdRequest;
import ec.com.sofka.account.responses.AccountResponse;
import ec.com.sofka.dto.AccountRequestDTO;
import ec.com.sofka.exception.GlobalExceptionHandler;
import ec.com.sofka.exception.NotFoundException;
import ec.com.sofka.handler.AccountHandler;
import ec.com.sofka.router.AccountRouter;
import ec.com.sofka.validator.RequestValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;

@WebFluxTest
@ContextConfiguration(classes = {AccountRouter.class, AccountHandler.class, RequestValidator.class, GlobalExceptionHandler.class})
public class AccountRouterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private CreateAccountUseCaseExecute createAccountUseCase;

    @MockitoBean
    private GetAllByUserIdUseCase getAllByUserIdUseCase;

    @MockitoBean
    private GetAccountByNumberUseCaseExecute getAccountByNumberUseCase;

    private AccountResponse accountResponse;

    @BeforeEach
    void setUp() {
        accountResponse = new AccountResponse("675e0e1259d6de4eda5b29a8", "12345678", BigDecimal.valueOf(0.0), "675e0e1259d6de4eda5b29b7");
    }

    @Test
    void create_validAccount_ReturnsCreatedResponse() {
        CreateAccountRequest validAccountRequest = new CreateAccountRequest("675e0e1259d6de4eda5b29a8");
        when(createAccountUseCase.execute(any(CreateAccountRequest.class))).thenReturn(Mono.just(accountResponse));

        webTestClient.post()
                .uri("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validAccountRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.accountNumber").isEqualTo("12345678")
                .jsonPath("$.balance").isEqualTo(0.0)
                .jsonPath("$.userId").isEqualTo("675e0e1259d6de4eda5b29b7");

        verify(createAccountUseCase, times(1)).execute(any(CreateAccountRequest.class));
    }

    @Test
    void create_DuplicateUser_ReturnsBadRequest() {
        CreateAccountRequest validAccountRequest = new CreateAccountRequest("675e0e1259d6de4eda5b29a8");
        when(createAccountUseCase.execute(any(CreateAccountRequest.class)))
                .thenReturn(Mono.error(new NotFoundException("User not found")));

        webTestClient.post()
                .uri("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validAccountRequest)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.error").isEqualTo("User not found");

        verify(createAccountUseCase, times(1)).execute(any(CreateAccountRequest.class));
    }

    @Test
    void create_EmptyUserId_ReturnsBadRequest() {
        AccountRequestDTO invalidAccountRequest = new AccountRequestDTO();
        invalidAccountRequest.setAggregateId("");

        webTestClient.post()
                .uri("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidAccountRequest)
                .exchange()
                .expectStatus().isBadRequest();

        verify(createAccountUseCase, never()).execute(any(CreateAccountRequest.class));
    }

    @Test
    void getAllByUserId_validUser_ReturnsAccountsList() {
        List<AccountResponse> accountList = List.of(accountResponse);


        when(getAllByUserIdUseCase.execute(any(GetAllByUserIdRequest.class))).thenReturn(Flux.fromIterable(accountList));

        webTestClient.get()
                .uri("/accounts/{userId}/user", "675e0e1259d6de4eda5b29b7")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].accountNumber").isEqualTo("12345678")
                .jsonPath("$[0].balance").isEqualTo(0.0)
                .jsonPath("$[0].userId").isEqualTo("675e0e1259d6de4eda5b29b7");

        verify(getAllByUserIdUseCase, times(1)).execute(any(GetAllByUserIdRequest.class));
    }

    @Test
    void getByAccountNumber_validAccount_ReturnsAccount() {

        GetAccountByNumberRequest validRequest = new GetAccountByNumberRequest("675e0e1259d6de4eda5b29a8", "12345678");
        when(getAccountByNumberUseCase.execute(any(GetAccountByNumberRequest.class))).thenReturn(Mono.just(accountResponse));

        webTestClient.post()
                .uri("/accounts/number")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validRequest)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.accountNumber").isEqualTo("12345678")
                .jsonPath("$.balance").isEqualTo(0.0)
                .jsonPath("$.userId").isEqualTo("675e0e1259d6de4eda5b29b7");

        verify(getAccountByNumberUseCase, times(1)).execute(any(GetAccountByNumberRequest.class));
    }

    @Test
    void getByAccountNumber_accountNotFound_ReturnsNotFound() {
        GetAccountByNumberRequest validRequest = new GetAccountByNumberRequest("675e0e1259d6de4eda5b29a8", "9999999");

        when(getAccountByNumberUseCase.execute(any(GetAccountByNumberRequest.class)))
                .thenReturn(Mono.error(new NotFoundException("Account not found")));

        webTestClient.post()
                .uri("/accounts/number")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validRequest)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.error").isEqualTo("Account not found");

        verify(getAccountByNumberUseCase, times(1)).execute(any(GetAccountByNumberRequest.class));
    }
}
