package e.com.sofka.router;

import ec.com.sofka.dto.TransactionRequestDTO;
import ec.com.sofka.exception.GlobalExceptionHandler;
import ec.com.sofka.exception.NotFoundException;
import ec.com.sofka.handler.TransactionHandler;
import ec.com.sofka.router.TransactionRouter;
import ec.com.sofka.transaction.CreateTransactionUseCase;
import ec.com.sofka.transaction.GetAllByAccountNumberUseCase;
import ec.com.sofka.transaction.TransactionType;
import ec.com.sofka.transaction.request.CreateTransactionRequest;
import ec.com.sofka.transaction.request.GetAllByAccountNumberRequest;
import ec.com.sofka.transaction.responses.TransactionResponse;
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
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

@WebFluxTest
@ContextConfiguration(classes = {TransactionRouter.class, TransactionHandler.class, RequestValidator.class, GlobalExceptionHandler.class})
public class TransactionRouterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private CreateTransactionUseCase createTransactionUseCase;

    @MockitoBean
    private GetAllByAccountNumberUseCase getAllByAccountNumber;

    private TransactionRequestDTO validTransactionRequest;
    private TransactionResponse transactionResponse;

    @BeforeEach
    void setUp() {
        validTransactionRequest = new TransactionRequestDTO(BigDecimal.valueOf(100.0), TransactionType.ATM_DEPOSIT, "123456789", "675e0e1259d6de4eda5b29b7");
        transactionResponse = new TransactionResponse(
                "675e0ec661737976b43cca86",
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(2.0),
                BigDecimal.valueOf(98.0),
                TransactionType.ATM_DEPOSIT,
                LocalDateTime.now(),
                "675e0e1259d6de4eda5b29a8",
                "675e0e1259d6de4eda5b29b7"
        );
    }

    @Test
    void create_validTransaction_ReturnsCreatedResponse() {
        when(createTransactionUseCase.execute(any(CreateTransactionRequest.class))).thenReturn(Mono.just(transactionResponse));

        webTestClient.post().uri("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validTransactionRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.operationId").isEqualTo("675e0ec661737976b43cca86")
                .jsonPath("$.fee").isEqualTo(2.0)
                .jsonPath("$.netAmount").isEqualTo(98.0)
                .jsonPath("$.type").isEqualTo("ATM_DEPOSIT");

        verify(createTransactionUseCase, times(1)).execute(any(CreateTransactionRequest.class));
    }

    @Test
    void create_accountNotFound_ReturnsNotFound() {
        when(createTransactionUseCase.execute(any(CreateTransactionRequest.class)))
                .thenReturn(Mono.error(new NotFoundException("Account not found")));

        webTestClient.post().uri("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validTransactionRequest)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.error").isEqualTo("Account not found");

        verify(createTransactionUseCase, times(1)).execute(any(CreateTransactionRequest.class));
    }

    @Test
    void create_invalidTransactionData_ReturnsBadRequest() {
        TransactionRequestDTO invalidTransactionRequest = new TransactionRequestDTO(BigDecimal.valueOf(-100.0), TransactionType.ATM_DEPOSIT, "123456789", "675e0e1259d6de4eda5b29b7");

        webTestClient.post().uri("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidTransactionRequest)
                .exchange()
                .expectStatus().isBadRequest();

        verify(createTransactionUseCase, never()).execute(any(CreateTransactionRequest.class));
    }

    @Test
    void getAllByAccountNumber_validAccount_ReturnsTransactionList() {
        List<TransactionResponse> transactionList = List.of(transactionResponse);

        GetAllByAccountNumberRequest getAllByAccountNumberRequest = new GetAllByAccountNumberRequest("675e0e1259d6de4eda5b29b7", "123456789");
        when(getAllByAccountNumber.execute(any(GetAllByAccountNumberRequest.class))).thenReturn(Flux.fromIterable(transactionList));

        webTestClient.post().uri("/transactions/account")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(getAllByAccountNumberRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].operationId").isEqualTo("675e0ec661737976b43cca86")
                .jsonPath("$[0].fee").isEqualTo(2.0)
                .jsonPath("$[0].netAmount").isEqualTo(98.0)
                .jsonPath("$[0].type").isEqualTo("ATM_DEPOSIT");

        verify(getAllByAccountNumber, times(1)).execute(any(GetAllByAccountNumberRequest.class));
    }

    @Test
    void getAllByAccountNumber_accountNotFound_ReturnsNotFound() {
        GetAllByAccountNumberRequest getAllByAccountNumberRequest = new GetAllByAccountNumberRequest("675e0e1259d6de4eda5b29b7", "9999999");

        when(getAllByAccountNumber.execute(any(GetAllByAccountNumberRequest.class)))
                .thenReturn(Flux.error(new NotFoundException("Account not found")));

        webTestClient.post().uri("/transactions/account")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(getAllByAccountNumberRequest)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.error").isEqualTo("Account not found");

        verify(getAllByAccountNumber, times(1)).execute(any(GetAllByAccountNumberRequest.class));
    }
}
