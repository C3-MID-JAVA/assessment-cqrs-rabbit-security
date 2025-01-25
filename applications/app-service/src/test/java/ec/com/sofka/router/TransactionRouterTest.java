package ec.com.sofka.router;

import ec.com.sofka.JwtAuthFilter;
import ec.com.sofka.JwtServiceAdapter;
import ec.com.sofka.NotFoundException;
import ec.com.sofka.SecurityConfig;
import ec.com.sofka.dto.TransactionRequestDTO;
import ec.com.sofka.exception.GlobalExceptionHandler;
import ec.com.sofka.generics.utils.QueryResponse;
import ec.com.sofka.handler.TransactionHandler;
import ec.com.sofka.router.TransactionRouter;
import ec.com.sofka.transaction.TransactionType;
import ec.com.sofka.transaction.commands.CreateTransactionCommand;
import ec.com.sofka.transaction.commands.usecases.CreateTransactionUseCase;
import ec.com.sofka.transaction.queries.query.GetAllByAccountNumberQuery;
import ec.com.sofka.transaction.queries.responses.TransactionResponse;
import ec.com.sofka.transaction.queries.usecases.GetAllByAccountNumberViewUseCase;
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
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

@WebFluxTest
@ContextConfiguration(classes = {
        TransactionRouter.class,
        TransactionHandler.class,
        RequestValidator.class,
        GlobalExceptionHandler.class,
        SecurityConfig.class,
        JwtAuthFilter.class
})
public class TransactionRouterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private CreateTransactionUseCase createTransactionUseCase;

    @MockitoBean
    private GetAllByAccountNumberViewUseCase getAllByAccountNumber;

    @MockitoBean
    private JwtServiceAdapter jwtService;

    private TransactionRequestDTO validTransactionCommand;
    private TransactionResponse transactionResponse;

    private String authToken;

    @BeforeEach
    void setUp() {
        validTransactionCommand = new TransactionRequestDTO(BigDecimal.valueOf(100.0), TransactionType.ATM_DEPOSIT, "123456789", "675e0e1259d6de4eda5b29b7");
        transactionResponse = new TransactionResponse(
                "675e0ec661737976b43cca86",
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(2.0),
                BigDecimal.valueOf(98.0),
                TransactionType.ATM_DEPOSIT,
                LocalDateTime.now(),
                "675e0e1259d6de4eda5b29a8"
        );

        when(jwtService.generateToken(anyString(), anyString())).thenReturn("test-token");
        when(jwtService.isTokenValid(anyString())).thenReturn(true);
        when(jwtService.extractUsername(anyString())).thenReturn("test-user");
        when(jwtService.extractRole(anyString())).thenReturn("GOD");

        authToken = jwtService.generateToken("test-user", "GOD");
    }

    @Test
    void create_validTransaction_ReturnsCreatedResponse() {
        when(createTransactionUseCase.execute(any(CreateTransactionCommand.class))).thenReturn(Mono.just(transactionResponse));

        webTestClient.post()
                .uri("/transactions")
                .headers(headers -> headers.setBearerAuth(authToken))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validTransactionCommand)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.operationId").isEqualTo("675e0ec661737976b43cca86")
                .jsonPath("$.fee").isEqualTo(2.0)
                .jsonPath("$.netAmount").isEqualTo(98.0)
                .jsonPath("$.type").isEqualTo("ATM_DEPOSIT");

        verify(createTransactionUseCase, times(1)).execute(any(CreateTransactionCommand.class));
    }

    @Test
    void create_accountNotFound_ReturnsNotFound() {
        when(createTransactionUseCase.execute(any(CreateTransactionCommand.class)))
                .thenReturn(Mono.error(new NotFoundException("Account not found")));

        webTestClient.post()
                .uri("/transactions")
                .headers(headers -> headers.setBearerAuth(authToken))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validTransactionCommand)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.error").isEqualTo("Account not found");

        verify(createTransactionUseCase, times(1)).execute(any(CreateTransactionCommand.class));
    }

    @Test
    void create_invalidTransactionData_ReturnsBadRequest() {
        TransactionRequestDTO invalidTransactionRequest = new TransactionRequestDTO(BigDecimal.valueOf(-100.0), TransactionType.ATM_DEPOSIT, "123456789", "675e0e1259d6de4eda5b29b7");

        webTestClient.post()
                .uri("/transactions")
                .headers(headers -> headers.setBearerAuth(authToken))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidTransactionRequest)
                .exchange()
                .expectStatus().isBadRequest();

        verify(createTransactionUseCase, never()).execute(any(CreateTransactionCommand.class));
    }

    @Test
    void getAllByAccountNumber_validAccount_ReturnsTransactionList() {
        List<TransactionResponse> transactionList = List.of(transactionResponse);

        Mono<QueryResponse<TransactionResponse>> response = Mono.just(QueryResponse.ofMultiple(transactionList));

        when(getAllByAccountNumber.get(any(GetAllByAccountNumberQuery.class))).thenReturn(response);

        webTestClient.get()
                .uri("/transactions/{accountNumber}/account", "123456789")
                .headers(headers -> headers.setBearerAuth(authToken))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].operationId").isEqualTo("675e0ec661737976b43cca86")
                .jsonPath("$[0].fee").isEqualTo(2.0)
                .jsonPath("$[0].netAmount").isEqualTo(98.0)
                .jsonPath("$[0].type").isEqualTo("ATM_DEPOSIT");

        verify(getAllByAccountNumber, times(1)).get(any(GetAllByAccountNumberQuery.class));
    }

    @Test
    void getAllByAccountNumber_accountNotFound_ReturnsNotFound() {
        when(getAllByAccountNumber.get(any(GetAllByAccountNumberQuery.class)))
                .thenReturn(Mono.error(new NotFoundException("Account not found")));

        webTestClient.get()
                .uri("/transactions/{accountNumber}/account", "9999999")
                .headers(headers -> headers.setBearerAuth(authToken))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.error").isEqualTo("Account not found");

        verify(getAllByAccountNumber, times(1)).get(any(GetAllByAccountNumberQuery.class));
    }
}
