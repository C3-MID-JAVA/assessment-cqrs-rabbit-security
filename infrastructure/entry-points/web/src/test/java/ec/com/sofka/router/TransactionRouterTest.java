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
        RouterFunction<?> routerFunction = transactionRouter.transactionRoutes();
        this.webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
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
    void createWithDrawal_ShouldReturnBadRequest_WhenValidationFails() {
        // Arrange
        TransactionRequestDTO requestDTO = new TransactionRequestDTO(
                "customerId123", // customerId
                "0123456789", // accountNumber
                BigDecimal.valueOf(-500), // Invalid amount
                TransactionType.BRANCH_DEPOSIT // Tipo de transacción
        );

        // Simula el fallo de validación
        when(validationService.validate(any(), eq(TransactionRequestDTO.class)))
                .thenReturn(Mono.error(new IllegalArgumentException("Invalid data")));

        // Act and Assert
        webTestClient.post()
                .uri("/api/v1/transactions/withdrawal")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isBadRequest() // 400
                .expectBody()
                .jsonPath("$.message").value(is("Invalid data")); // Mensaje de error esperado
    }


    @Test
    void createWithDrawal_ShouldReturnInternalServerError_WhenHandlerFails() {
        // Arrange
        TransactionRequestDTO requestDTO = new TransactionRequestDTO(
                "customerId123", // customerId
                "0123456789", // accountNumber
                BigDecimal.valueOf(500), // Amount válido
                TransactionType.BRANCH_DEPOSIT // Tipo de transacción
        );

        // Simula el éxito en la validación
        when(validationService.validate(any(), eq(TransactionRequestDTO.class)))
                .thenReturn(Mono.just(requestDTO));

        // Simula el fallo en el manejador
        when(transactionHandler.createWithDrawal(any(TransactionRequestDTO.class)))
                .thenReturn(Mono.error(new RuntimeException("Internal server error")));

        // Act and Assert
        webTestClient.post()
                .uri("/api/v1/transactions/withdrawal")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().is5xxServerError() // 500
                .expectBody()
                .jsonPath("$.message").value(is("Internal server error")); // Mensaje de error esperado
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


    @Test
    void getTransactionByAccountNumber_ShouldReturnOk() {
        // Arrange
        String accountNumber = "0123456789";
        TransactionResponseDTO responseDTO = new TransactionResponseDTO(
                "txn123",
                "accountId123",
                BigDecimal.valueOf(1.25),
                BigDecimal.valueOf(500.75),
                LocalDateTime.now(),
                TransactionType.BRANCH_DEPOSIT
        );

        // Simula la respuesta del handler
        when(transactionHandler.getTransactionByAccountNumber(any(AccountReqByElementDTO.class)))
                .thenReturn(Mono.just(List.of(responseDTO)));  // Asegúrate de envolver la respuesta en una lista

        // Act and Assert
        webTestClient.post()
                .uri("/api/v1/transactions/accountNumber")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new AccountReqByElementDTO("customer123", accountNumber, null))
                .exchange()
                .expectStatus().isOk() // 200
                .expectBody()
                .jsonPath("$[0].transactionId").isEqualTo("txn123")
                .jsonPath("$[0].accountId").isEqualTo("accountId123")
                .jsonPath("$[0].amount").isEqualTo(500.75);  // Verifica el valor esperado
    }


    @Test
    void getTransactionByAccountNumber_ShouldReturnNotFound_WhenNoTransactionsExist() {
        // Arrange
        String accountNumber = "0123456789"; // Cuenta sin transacciones
        when(transactionHandler.getTransactionByAccountNumber(any(AccountReqByElementDTO.class)))
                .thenReturn(Mono.just(List.of()));  // Retorna una lista vacía

        // Act and Assert
        webTestClient.post()
                .uri("/api/v1/transactions/accountNumber")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new AccountReqByElementDTO("customer123", accountNumber, null))
                .exchange()
                .expectStatus().isNotFound()  // 404
                .expectBody()
                .jsonPath("$.message").value(is("No transactions found for the account"));
    }

    @Test
    void getTransactionByAccountNumber_ShouldReturnNotFound_WhenAccountDoesNotExist() {
        // Arrange
        String accountNumber = "9999999999"; // Cuenta no existente
        when(transactionHandler.getTransactionByAccountNumber(any(AccountReqByElementDTO.class)))
                .thenReturn(Mono.just(List.of()));  // Retorna lista vacía cuando no se encuentran transacciones

        // Act and Assert
        webTestClient.post()
                .uri("/api/v1/transactions/accountNumber")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new AccountReqByElementDTO("customer123", accountNumber, null))
                .exchange()
                .expectStatus().isNotFound()  // 404
                .expectBody()
                .jsonPath("$.message").value(is("Transactions not found for this account"));
    }


}
