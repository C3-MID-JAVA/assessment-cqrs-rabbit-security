package ec.com.sofka.routes;


import ec.com.sofka.ErrorDetails;
import ec.com.sofka.data.AccountReqDTO;
import ec.com.sofka.exceptions.BodyRequestValidator;
import ec.com.sofka.exceptions.GlobalExceptionsHandler;
import ec.com.sofka.handlers.AccountHandler;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class AccountRouterTest {

    @Mock
    private AccountHandler accountHandler;

    @Mock
    private BodyRequestValidator bodyRequestValidator;

    @Mock
    private GlobalExceptionsHandler globalExceptionsHandler;

    @InjectMocks
    private AccountRouter accountRouter;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        webTestClient = WebTestClient.bindToRouterFunction(accountRouter.accountRoutes()).build();
    }


    @Test
    @DisplayName("Should retrieve accounts")
    void getAllAccounts() {
        AccountReqDTO accountDTO = new AccountReqDTO();
        AccountReqDTO accountDTO2 = new AccountReqDTO();
        accountDTO2.setAccountNumber("12368");
        accountDTO.setAccountNumber("12368");
        when(accountHandler.getAllAccounts()).thenReturn(Flux.just(accountDTO2, accountDTO));

        webTestClient.get()
                .uri("/api/v1/account")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AccountReqDTO.class)
                .hasSize(2)
                .consumeWith(response -> {
                    List<AccountReqDTO> actualResponse = response.getResponseBody();
                    assert actualResponse != null;
                    assertEquals(accountDTO.getAccountNumber(), actualResponse.get(0).getAccountNumber());
                    assertEquals(accountDTO2.getAccountNumber(), actualResponse.get(1).getAccountNumber());
                });

        verify(accountHandler, times(1)).getAllAccounts();


    }

    @Test
    @DisplayName("Should retrieve zero accounts")
    void getAllAccounts_zero() {
        when(accountHandler.getAllAccounts()).thenReturn(Flux.empty());

        webTestClient.get()
                .uri("/api/v1/account")
                .exchange()
                .expectStatus().isNotFound();

        verify(accountHandler, times(1)).getAllAccounts();


    }

    @Test
    @DisplayName("Should call create account endpoint successfully")
    void createAccount() {
        AccountReqDTO accountDTOResponse = new AccountReqDTO("12345",
                BigDecimal.valueOf(100), "DEBIT", "My cas",
                "customerId");
        when(accountHandler.createAccount(any(AccountReqDTO.class))).thenReturn(Mono.just(accountDTOResponse));


        webTestClient
                .post()
                .uri("/api/v1/account/create")
                .bodyValue(accountDTOResponse)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(AccountReqDTO.class)
                .consumeWith(response -> {
                    AccountReqDTO actualResponse = response.getResponseBody();
                    assert actualResponse != null;
                    assertEquals(accountDTOResponse.getAccountNumber(), actualResponse.getAccountNumber());
                });


        verify(accountHandler, times(1)).createAccount(any(AccountReqDTO.class));
    }

    @Test
    @DisplayName("Should call create account endpoint but return an error")
    void createAccount_error() {
        AccountReqDTO accountDTOrequest = new AccountReqDTO();
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "SOME FIELD(s) IN THE REQUEST HAS ERROR",
                List.of("accountOwner: accountOwner cannot be empty"));


        doThrow(new ValidationException("accountOwner: accountOwner cannot be empty"))
                .when(bodyRequestValidator).validate(any(AccountReqDTO.class));


        when(globalExceptionsHandler.handleException(any(ValidationException.class)))
                .thenReturn(
                        ServerResponse.badRequest()
                                .bodyValue(
                                        errorDetails)
                );


        webTestClient
                .post()
                .uri("/api/v1/account/create")
                .bodyValue(accountDTOrequest)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorDetails.class)
                .consumeWith(response -> {
                    ErrorDetails actualResponse = response.getResponseBody();
                    System.out.println(actualResponse);
                    assert actualResponse != null;
                    assertEquals("SOME FIELD(s) IN THE REQUEST HAS ERROR", actualResponse.getMessage());
                    assertTrue(String.valueOf(actualResponse.getDetails()).contains("accountOwner: accountOwner cannot be empty"));
                });

        verify(accountHandler, times(0)).createAccount(any(AccountReqDTO.class));
        verify(globalExceptionsHandler, times(1)).handleException(any(ValidationException.class));

    }
}