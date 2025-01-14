package ec.com.sofka.routes;


import ec.com.sofka.data.AccountReqDTO;
import ec.com.sofka.data.CardReqDTO;
import ec.com.sofka.data.TransactionReqDTO;
import ec.com.sofka.exceptions.BodyRequestValidator;
import ec.com.sofka.exceptions.GlobalExceptionsHandler;
import ec.com.sofka.handlers.TransactionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionRouterTest {

    @Mock
    private TransactionHandler transactionHandler;

    @InjectMocks
    private TransactionRouter transactionRouter;

    @Mock
    private BodyRequestValidator bodyRequestValidator;

    @Mock
    private GlobalExceptionsHandler globalExceptionsHandler;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        webTestClient = WebTestClient.bindToRouterFunction(transactionRouter.transactionsRoutes()).build();
    }

    @Test
    @DisplayName("Should create a new transaction and return the complete body response")
    void crateTransaction() {
        CardReqDTO cardDTO = new CardReqDTO();
        cardDTO.setCardNumber("123456789");

        AccountReqDTO accountDTO = new AccountReqDTO();
        accountDTO.setAccountNumber("123456");
        TransactionReqDTO transactionDTOReq = new TransactionReqDTO("cus","Test Transaction", BigDecimal.valueOf(10),
                "ATM", BigDecimal.valueOf(0), accountDTO, cardDTO,null);
        TransactionReqDTO transactionDTORes = new TransactionReqDTO("cus","Test Transaction", BigDecimal.valueOf(10),
                "ATM", BigDecimal.valueOf(0), accountDTO, cardDTO,null);

        when(transactionHandler.createTransaction(any(TransactionReqDTO.class))).thenReturn(Mono.just(transactionDTORes));


        webTestClient
                .post()
                .uri("/api/v1/transaction/make")
                .bodyValue(transactionDTOReq)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(TransactionReqDTO.class)
                .consumeWith(response -> {
                    TransactionReqDTO actualResponse = response.getResponseBody();
                    assert actualResponse != null;
                    assertEquals(transactionDTORes.getDescription(), actualResponse.getDescription());
                    assertEquals(transactionDTORes.getAccount().getAccountNumber(), actualResponse.getAccount().getAccountNumber());
                });

        verify(transactionHandler, times(1)).createTransaction(any(TransactionReqDTO.class));

    }
}