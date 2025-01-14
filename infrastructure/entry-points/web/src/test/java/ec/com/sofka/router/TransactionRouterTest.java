package ec.com.sofka.router;

import ec.com.sofka.data.TransactionRequestDTO;
import ec.com.sofka.data.TransactionResponseDTO;
import ec.com.sofka.exceptions.RequestValidationException;
import ec.com.sofka.handlers.transaction.CreateTransactionHandler;
import ec.com.sofka.handlers.transaction.GetAllTransactionsHandler;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionRouterTest {
    @Mock
    private RequestValidator requestValidator;

    @Mock
    private CreateTransactionHandler createTransactionHandler;

    @Mock
    private GetAllTransactionsHandler getAllTransactionsHandler;

    @InjectMocks
    private TransactionRouter transactionRouter;

    private WebTestClient webTestClient;

    private TransactionRequestDTO transactionRequest;
    private TransactionResponseDTO transactionResponse;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToRouterFunction(transactionRouter.transactionsRouters()).build();

        transactionRequest = new TransactionRequestDTO(new BigDecimal(100), new Date().toString(), "2222222222", "transaction", "ACTIVE", "u1", "u2");
        transactionResponse = new TransactionResponseDTO("2222222222", "transaction", new BigDecimal(100), new Date().toString(), null,null);

    }

    @Test
    @DisplayName("Should create transaction when request passes to validation")
    void shouldCreateTransactionSuccessfully() {
        when(createTransactionHandler.save(any())).thenReturn(Mono.just(transactionResponse));

        webTestClient.post()
                .uri("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(transactionRequest)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.accountNumber").isEqualTo(transactionRequest.getAccountNumber())
                .jsonPath("$.details").isEqualTo(transactionRequest.getDetails());
    }

    @Test
    @DisplayName("Should return validation errors when exist error")
    void shouldReturnValidationErrors() {
        TransactionRequestDTO invalidRequest = new TransactionRequestDTO(null, new Date().toString(), "2222222222", "transaction", "ACTIVE", "u1", "u2");

        doThrow(new RequestValidationException(List.of("amount cannot be null")))
                .when(requestValidator).validate(any(TransactionRequestDTO.class));

        webTestClient.post()
                .uri("/api/transactions")
                .bodyValue(invalidRequest)
                .exchange()
                .expectStatus().isEqualTo(500);
    }

    @Test
    @DisplayName("Should get all transaction records")
    void shouldGetAllTransactionSuccessfully() {
        when(getAllTransactionsHandler.getAll()).thenReturn(Flux.just(transactionResponse));

        webTestClient.get()
                .uri("/api/transactions")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$[0].accountNumber").isEqualTo(transactionRequest.getAccountNumber())
                .jsonPath("$[0].details").isEqualTo(transactionRequest.getDetails());
    }

}