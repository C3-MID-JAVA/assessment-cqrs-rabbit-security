package ec.com.sofka.appservice;

import ec.com.sofka.RestAccount;
import ec.com.sofka.data.RequestDTO;
import ec.com.sofka.data.ResponseDTO;
import ec.com.sofka.handlers.AccountHandler;
import ec.com.sofka.validator.RequestValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public class RestAccountTest {

    private WebTestClient webTestClient;

    @Mock
    private RequestValidator requestValidator;

    @Mock
    private AccountHandler handler;

    @InjectMocks
    private RestAccount restAccount;

    private RequestDTO requestDTO;
    private ResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        webTestClient = WebTestClient.bindToRouterFunction(restAccount.accountRoutes()).build();
        requestDTO = new RequestDTO("customerId", "name", "123456789", new BigDecimal("1000.0"), "status", "idUser");
        responseDTO = new ResponseDTO("customerId", "accountId", "accountName", "123456789", new BigDecimal("1000.0"), "status");
    }

    @Test
    void testCreateAccount() {
        when(handler.createAccount(any(RequestDTO.class))).thenReturn(Mono.just(responseDTO));

        webTestClient.post()
                .uri("/api/account")
                .contentType(APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ResponseDTO.class);
    }

    @Test
    void testCreateAccountInvalidRequest() {
        RequestDTO invalidRequestDTO = new RequestDTO(null, null, null, null, null, null);

        webTestClient.post()
                .uri("/api/account")
                .contentType(APPLICATION_JSON)
                .bodyValue(invalidRequestDTO)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testGetAllAccounts() {
        when(handler.getAllAccounts()).thenReturn(Flux.just(responseDTO));

        webTestClient.get()
                .uri("/api/accounts")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ResponseDTO.class)
                .hasSize(1);
    }

    @Test
    void testGetAccountByNumber() {
        when(handler.getAccountByNumber(any(RequestDTO.class))).thenReturn(Mono.just(responseDTO));

        webTestClient.post()
                .uri("/api/account/number")
                .contentType(APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResponseDTO.class);
    }

    @Test
    void testUpdateAccount() {
        when(handler.updateAccount(any(RequestDTO.class))).thenReturn(Mono.just(responseDTO));

        webTestClient.put()
                .uri("/api/account")
                .contentType(APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResponseDTO.class);
    }

    @Test
    void testUpdateAccountInvalidRequest() {
        RequestDTO invalidRequestDTO = new RequestDTO(null, null, null, null, null, null);

        webTestClient.put()
                .uri("/api/account")
                .contentType(APPLICATION_JSON)
                .bodyValue(invalidRequestDTO)
                .exchange()
                .expectStatus().isBadRequest();
    }
}