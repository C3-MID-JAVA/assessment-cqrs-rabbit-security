package ec.com.sofka.routes;


import ec.com.sofka.data.AccountReqDTO;
import ec.com.sofka.data.AccountSimpleRequestDTO;
import ec.com.sofka.data.CardReqDTO;
import ec.com.sofka.exceptions.BodyRequestValidator;
import ec.com.sofka.exceptions.GlobalExceptionsHandler;
import ec.com.sofka.handlers.CardHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CardRouterTest {

    @Mock
    private CardHandler cardHandler;

    @Mock
    private BodyRequestValidator bodyRequestValidator;

    @Mock
    private GlobalExceptionsHandler globalExceptionsHandler;

    @InjectMocks
    CardRouter cardRouter;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        webTestClient = WebTestClient.bindToRouterFunction(cardRouter.cardRoutes()).build();
    }



    @Test
    @DisplayName("Should call create card endpoint successfully")
    void createCard() {
        AccountReqDTO accountDTO = new AccountReqDTO();
        accountDTO.setAccountNumber("123456");
        CardReqDTO cardDTORequest = new CardReqDTO("cus","CARD TEST", "123456789",
                "TDEBIT","ACTIVE", "12-12-2024",
                BigDecimal.valueOf(1000), "TEST HOLDER",
                accountDTO
        );
        CardReqDTO cardDTOResponse = new CardReqDTO("cus","CARD TEST", "123456789",
                "TDEBIT","ACTIVE", "12-12-2024",
                BigDecimal.valueOf(1000), "TEST HOLDER",
                accountDTO
        );


        when(cardHandler.createCard(any(CardReqDTO.class))).thenReturn(Mono.just(cardDTOResponse));


        webTestClient
                .post()
                .uri("/api/v1/card/create")
                .bodyValue(cardDTORequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CardReqDTO.class)
                .consumeWith(response -> {
                    CardReqDTO actualResponse = response.getResponseBody();
                    assert actualResponse != null;
                    assertEquals(cardDTORequest.getCardNumber(), actualResponse.getCardNumber());
                    assertEquals(cardDTORequest.getAccount().getAccountNumber(), actualResponse.getAccount().getAccountNumber());
                });

        verify(cardHandler, times(1)).createCard(any(CardReqDTO.class));
    }


    @Test
    @DisplayName("Should retrieve all cards by accountNumber when exists")
    void getCardsByAccount() {
        AccountReqDTO accountDTO = new AccountReqDTO();
        accountDTO.setAccountNumber("123456");
        CardReqDTO cardDTO = new CardReqDTO("cus","CARD TEST", "123456789",
                "TDEBIT","ACTIVE", "12-12-2024",
                BigDecimal.valueOf(1000), "TEST HOLDER",
                accountDTO
        );
        CardReqDTO cardDTO2 = new CardReqDTO("cus","CARD TEST", "123456789",
                "TDEBIT","ACTIVE", "12-12-2024",
                BigDecimal.valueOf(1000), "TEST HOLDER",
                accountDTO
        );
        AccountSimpleRequestDTO accountDTO2 = new AccountSimpleRequestDTO("123456789");



        when(cardHandler.getCardsByAccountNumber(anyString())).thenReturn(Flux.just(cardDTO, cardDTO2));


        webTestClient
                .post()
                .uri("/api/v1/card/byAccount")
                .bodyValue(accountDTO2)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CardReqDTO.class)
                .consumeWith(response -> {
                    List<CardReqDTO> actualResponse = response.getResponseBody();
                    assert actualResponse != null;
                    assertEquals(cardDTO.getCardNumber(), actualResponse.get(0).getCardNumber());
                    assertEquals(cardDTO2.getCardNumber(), actualResponse.get(1).getCardNumber());
                });

        verify(cardHandler, times(1)).getCardsByAccountNumber(anyString());

    }
}