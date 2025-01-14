package ec.com.sofka.appservice;

import ec.com.sofka.RestTransaction;
import ec.com.sofka.data.transaction.TransactionRequestDTO;
import ec.com.sofka.data.transaction.TransactionResponseDTO;
import ec.com.sofka.handlers.TransactionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public class RestTransactionTest {

    private WebTestClient webTestClient;

    @Mock
    private TransactionHandler handler;

    @InjectMocks
    private RestTransaction restTransaction;

    private TransactionRequestDTO requestDTO;
    private TransactionResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        webTestClient = WebTestClient.bindToRouterFunction(restTransaction.transactionRoutes()).build();
        requestDTO = new TransactionRequestDTO("operationId", new BigDecimal("100.0"), "type", new BigDecimal("10.0"), "idAccount", "status");
        responseDTO = new TransactionResponseDTO("operationId", "transactionId", new BigDecimal("100.0"), "type", new BigDecimal("10.0"), "idAccount", "status");
    }

    @Test
    void testSaveTransaction() {
        when(handler.saveTransaction(any(TransactionRequestDTO.class), any(String.class))).thenReturn(Mono.just(responseDTO));

        webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("/api/transaction").queryParam("customerId", "customerId").build())
                .contentType(APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(TransactionResponseDTO.class);

    }


    @Test
    void testSaveTransactionInvalidRequest() {
        TransactionRequestDTO invalidRequestDTO = new TransactionRequestDTO(null, null, null, null, null, null);

        webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("/api/transaction").queryParam("customerId", "customerId").build())
                .contentType(APPLICATION_JSON)
                .bodyValue(invalidRequestDTO)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testSaveTransactionHandlerError() {
        when(handler.saveTransaction(any(TransactionRequestDTO.class), any(String.class))).thenReturn(Mono.error(new RuntimeException("Handler error")));

        webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("/api/transaction").queryParam("customerId", "customerId").build())
                .contentType(APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .isEqualTo("Handler error");
    }
}