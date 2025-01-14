package ec.com.sofka.router;

import ec.com.sofka.data.AccountRequestDTO;
import ec.com.sofka.data.TransactionTypeRequestDTO;
import ec.com.sofka.data.TransactionTypeResponseDTO;
import ec.com.sofka.exceptions.RequestValidationException;
import ec.com.sofka.handlers.transcationType.CreateTransactionTypeHandler;
import ec.com.sofka.validator.RequestValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionTypeRouterTest {
    @Mock
    private RequestValidator requestValidator;

    @Mock
    private CreateTransactionTypeHandler createTransactionTypeHandler;

    @InjectMocks
    private TransactionTypeRouter transactionTypeRouter;

    private WebTestClient webTestClient;

    private TransactionTypeRequestDTO transactionTypeRequest;
    private TransactionTypeResponseDTO transactionTypeResponse;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToRouterFunction(transactionTypeRouter.transactionTypesRouters()).build();

        transactionTypeRequest = new TransactionTypeRequestDTO("Deposit from branch", "Deposit from branch", new BigDecimal(10), true, false, "ACTIVE");
        transactionTypeResponse = new TransactionTypeResponseDTO("Deposit from branch", "Deposit from branch", new BigDecimal(10), true, false, "ACTIVE");

    }

    @Test
    @DisplayName("Should create TransactionType when request passes to validation")
    void shouldCreateTransactionTypeSuccessfully() {
        when(createTransactionTypeHandler.save(ArgumentMatchers.any())).thenReturn(Mono.just(transactionTypeResponse));

        webTestClient.post()
                .uri("/api/transaction-types")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(transactionTypeRequest)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.type").isEqualTo(transactionTypeRequest.getType())
                .jsonPath("$.value").isEqualTo(transactionTypeRequest.getValue());
    }

    @Test
    @DisplayName("Should return validation errors when exist error")
    void shouldReturnValidationErrors() {
        TransactionTypeRequestDTO invalidRequest = new TransactionTypeRequestDTO(null, "Deposit from branch", new BigDecimal(10), true, false, "ATIVE");

        doThrow(new RequestValidationException(List.of("type cannot be null")))
                .when(requestValidator).validate(ArgumentMatchers.any(AccountRequestDTO.class));

        webTestClient.post()
                .uri("/api/transaction-types")
                .bodyValue(invalidRequest)
                .exchange()
                .expectStatus().isEqualTo(500);
    }

}