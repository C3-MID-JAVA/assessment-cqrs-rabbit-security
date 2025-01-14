package ec.com.sofka.router;

import ec.com.sofka.data.AccountReqByElementDTO;
import ec.com.sofka.data.TransactionRequestDTO;
import ec.com.sofka.data.TransactionResponseDTO;
import ec.com.sofka.enums.TransactionType;
import ec.com.sofka.handler.TransactionHandler;
import ec.com.sofka.service.ValidationService;
import ec.com.sofka.globalexceptions.GlobalErrorHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.is;

class TransactionRouterTest {

    private WebTestClient webTestClient;

    @Mock
    private TransactionHandler transactionHandler;

    @Mock
    private ValidationService validationService;

    @Mock
    private GlobalErrorHandler globalErrorHandler;

    @InjectMocks
    private TransactionRouter transactionRouter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        webTestClient = WebTestClient.bindToRouterFunction(transactionRouter.transactionRoutes()).build();
    }

    @Test
    void createWithDrawal_ShouldReturnCreated() {
        // Arrange
        TransactionRequestDTO requestDTO = new TransactionRequestDTO(
                "customer123",
                "0123456789",
                BigDecimal.valueOf(500.75),
                TransactionType.BRANCH_DEPOSIT // O cualquier tipo de transacción válido
        );
        TransactionResponseDTO responseDTO = new TransactionResponseDTO(
                "txn123",
                "accountId123",
                BigDecimal.valueOf(1.25), // Costo de transacción
                BigDecimal.valueOf(500.75),
                LocalDateTime.now(),
                TransactionType.BRANCH_DEPOSIT
        );

        when(validationService.validate(any(), eq(TransactionRequestDTO.class))).thenReturn(Mono.just(requestDTO));
        when(transactionHandler.createWithDrawal(any(TransactionRequestDTO.class))).thenReturn(Mono.just(responseDTO));

        // Act and Assert
        webTestClient.post()
                .uri("/api/v1/transactions/withdrawal")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.transactionId").isEqualTo("txn123")
                .jsonPath("$.accountId").isEqualTo("accountId123")
                .jsonPath("$.amount").isEqualTo(500.75)
                .jsonPath("$.transactionType").isEqualTo("BRANCH_DEPOSIT");
    }


    @Test
    void createDeposit_ShouldReturnCreated() {
        // Arrange
        TransactionRequestDTO requestDTO = new TransactionRequestDTO(
                "customer456",
                "0123456780",
                BigDecimal.valueOf(2000.50),
                TransactionType.PHYSICAL_PURCHASE // Otro tipo de transacción
        );
        TransactionResponseDTO responseDTO = new TransactionResponseDTO(
                "txn456",
                "accountId456",
                BigDecimal.valueOf(2.50), // Costo de transacción
                BigDecimal.valueOf(2000.50),
                LocalDateTime.now(),
                TransactionType.PHYSICAL_PURCHASE
        );

        when(validationService.validate(any(), eq(TransactionRequestDTO.class))).thenReturn(Mono.just(requestDTO));
        when(transactionHandler.createDeposit(any(TransactionRequestDTO.class))).thenReturn(Mono.just(responseDTO));

        // Act and Assert
        webTestClient.post()
                .uri("/api/v1/transactions/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.transactionId").isEqualTo("txn456")
                .jsonPath("$.accountId").isEqualTo("accountId456")
                .jsonPath("$.amount").isEqualTo(2000.50)
                .jsonPath("$.transactionType").isEqualTo("PHYSICAL_PURCHASE");
    }


}
