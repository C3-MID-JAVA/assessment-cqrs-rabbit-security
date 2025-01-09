package ec.com.sofka.router;

import ec.com.sofka.data.TransactionRequestDTO;
import ec.com.sofka.data.TransactionResponseDTO;
import ec.com.sofka.handler.TransactionHandler;
import ec.com.sofka.service.ValidationService;
import ec.com.sofka.globalexceptions.GlobalErrorHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

public class TransactionRouterTest {

    @Autowired
    private WebTestClient webTestClient;

    @Mock
    private TransactionHandler transactionHandler;

    @Mock
    private ValidationService validationService;

    @Mock
    private GlobalErrorHandler globalErrorHandler;

    private RouterFunction<?> routerFunction;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        // Crear una instancia de TransactionRouter
        TransactionRouter transactionRouter = new TransactionRouter(transactionHandler, validationService, globalErrorHandler);
        this.routerFunction = transactionRouter.transactionRoutes();

        // Inicializar WebTestClient
        this.webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }

    @Test
    public void testCreateDepositSuccess() {
        TransactionRequestDTO requestDTO = new TransactionRequestDTO();
        // Configurar la simulación del handler y validación
        TransactionResponseDTO responseDTO = new TransactionResponseDTO();
        when(validationService.validate(any(), any())).thenReturn(Mono.just(requestDTO));
        when(transactionHandler.createDeposit(any())).thenReturn(Mono.just(responseDTO));

        webTestClient.post()
                .uri("/transactions/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(TransactionResponseDTO.class);

        verify(validationService, times(1)).validate(any(), any());
        verify(transactionHandler, times(1)).createDeposit(any());
    }



    @Test
    public void testGetTransactionByIdSuccess() {
        String transactionId = "123";
        TransactionResponseDTO responseDTO = new TransactionResponseDTO();
        when(transactionHandler.getTransactionById(transactionId)).thenReturn(Mono.just(responseDTO));

        webTestClient.get()
                .uri("/transactions/{transactionId}", transactionId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TransactionResponseDTO.class);

        verify(transactionHandler, times(1)).getTransactionById(transactionId);
    }

    @Test
    public void testGetAllTransactionsSuccess() {
        TransactionResponseDTO responseDTO = new TransactionResponseDTO();
        when(transactionHandler.getTransactions()).thenReturn(Flux.just(responseDTO));

        webTestClient.get()
                .uri("/transactions")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TransactionResponseDTO.class);

        verify(transactionHandler, times(1)).getTransactions();
    }
}


