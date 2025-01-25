package ec.com.sofka.router;

import ec.com.sofka.JwtAuthFilter;
import ec.com.sofka.JwtServiceAdapter;
import ec.com.sofka.NotFoundException;
import ec.com.sofka.SecurityConfig;
import ec.com.sofka.account.commands.CreateAccountCommand;
import ec.com.sofka.account.commands.usecases.CreateAccountUseCase;
import ec.com.sofka.account.queries.query.GetAccountByNumberQuery;
import ec.com.sofka.account.queries.query.GetAllByUserIdQuery;
import ec.com.sofka.account.queries.responses.AccountResponse;
import ec.com.sofka.account.queries.usecases.GetAccountByNumberViewUseCase;
import ec.com.sofka.account.queries.usecases.GetAllByUserIdViewUseCase;
import ec.com.sofka.dto.AccountRequestDTO;
import ec.com.sofka.exception.GlobalExceptionHandler;
import ec.com.sofka.generics.utils.QueryResponse;
import ec.com.sofka.handler.AccountHandler;
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

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;

@WebFluxTest
@ContextConfiguration(classes = {
        AccountRouter.class,
        AccountHandler.class,
        RequestValidator.class,
        GlobalExceptionHandler.class,
        SecurityConfig.class,
        JwtAuthFilter.class
})
public class AccountRouterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private CreateAccountUseCase createAccountUseCase;

    @MockitoBean
    private GetAllByUserIdViewUseCase getAllByUserIdUseCase;

    @MockitoBean
    private GetAccountByNumberViewUseCase getAccountByNumberUseCase;

    @MockitoBean
    private JwtServiceAdapter jwtService;

    private AccountResponse accountResponse;

    private String authToken;

    @BeforeEach
    void setUp() {
        accountResponse = new AccountResponse("675e0e1259d6de4eda5b29a8", "12345678", BigDecimal.valueOf(0.0), "675e0e1259d6de4eda5b29b7");
        when(jwtService.generateToken(anyString(), anyString())).thenReturn("test-token");
        when(jwtService.isTokenValid(anyString())).thenReturn(true);
        when(jwtService.extractUsername(anyString())).thenReturn("test-user");
        when(jwtService.extractRole(anyString())).thenReturn("GOD");

        authToken = jwtService.generateToken("test-user", "GOD");
    }

    @Test
    void create_validAccount_ReturnsCreatedResponse() {
        CreateAccountCommand validAccountCommand = new CreateAccountCommand("675e0e1259d6de4eda5b29a8");
        when(createAccountUseCase.execute(any(CreateAccountCommand.class))).thenReturn(Mono.just(accountResponse));

        webTestClient.post()
                .uri("/accounts")
                .headers(headers -> headers.setBearerAuth(authToken))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validAccountCommand)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.accountNumber").isEqualTo("12345678")
                .jsonPath("$.balance").isEqualTo(0.0)
                .jsonPath("$.userId").isEqualTo("675e0e1259d6de4eda5b29b7");

        verify(createAccountUseCase, times(1)).execute(any(CreateAccountCommand.class));
    }

    @Test
    void create_DuplicateUser_ReturnsBadRequest() {
        CreateAccountCommand validAccountCommand = new CreateAccountCommand("675e0e1259d6de4eda5b29a8");
        when(createAccountUseCase.execute(any(CreateAccountCommand.class)))
                .thenReturn(Mono.error(new NotFoundException("User not found")));

        webTestClient.post()
                .uri("/accounts")
                .headers(headers -> headers.setBearerAuth(authToken))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validAccountCommand)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.error").isEqualTo("User not found");

        verify(createAccountUseCase, times(1)).execute(any(CreateAccountCommand.class));
    }

    @Test
    void create_EmptyUserId_ReturnsBadRequest() {
        AccountRequestDTO invalidAccountRequest = new AccountRequestDTO();
        invalidAccountRequest.setAggregateId("");

        webTestClient.post()
                .uri("/accounts")
                .headers(headers -> headers.setBearerAuth(authToken))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidAccountRequest)
                .exchange()
                .expectStatus().isBadRequest();

        verify(createAccountUseCase, never()).execute(any(CreateAccountCommand.class));
    }

    @Test
    void getAllByUserId_validUser_ReturnsAccountsList() {
        List<AccountResponse> accountList = List.of(accountResponse);

        Mono<QueryResponse<AccountResponse>> response = Mono.just(QueryResponse.ofMultiple(accountList));

        when(getAllByUserIdUseCase.get(any(GetAllByUserIdQuery.class))).thenReturn(response);

        webTestClient.get()
                .uri("/accounts/{userId}/user", "675e0e1259d6de4eda5b29b7")
                .headers(headers -> headers.setBearerAuth(authToken))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].accountNumber").isEqualTo("12345678")
                .jsonPath("$[0].balance").isEqualTo(0.0)
                .jsonPath("$[0].userId").isEqualTo("675e0e1259d6de4eda5b29b7");

        verify(getAllByUserIdUseCase, times(1)).get(any(GetAllByUserIdQuery.class));
    }

    @Test
    void getByAccountNumber_validAccount_ReturnsAccount() {
        Mono<QueryResponse<AccountResponse>> response = Mono.just(QueryResponse.ofSingle(accountResponse));
        when(getAccountByNumberUseCase.get(any(GetAccountByNumberQuery.class))).thenReturn(response);


        webTestClient.get()
                .uri("/accounts/{id}", "12345678")
                .headers(headers -> headers.setBearerAuth(authToken))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.accountNumber").isEqualTo("12345678")
                .jsonPath("$.balance").isEqualTo(0.0)
                .jsonPath("$.userId").isEqualTo("675e0e1259d6de4eda5b29b7");

        verify(getAccountByNumberUseCase, times(1)).get(any(GetAccountByNumberQuery.class));
    }

    @Test
    void getByAccountNumber_accountNotFound_ReturnsNotFound() {
        when(getAccountByNumberUseCase.get(any(GetAccountByNumberQuery.class)))
                .thenReturn(Mono.error(new NotFoundException("Account not found")));

        webTestClient.get()
                .uri("/accounts/{id}", "9999999")
                .headers(headers -> headers.setBearerAuth(authToken))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.error").isEqualTo("Account not found");

        verify(getAccountByNumberUseCase, times(1)).get(any(GetAccountByNumberQuery.class));
    }
}
