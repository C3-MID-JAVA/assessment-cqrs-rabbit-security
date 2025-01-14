package ec.com.sofka.router;

import ec.com.sofka.data.CustomerRequestDTO;
import ec.com.sofka.data.CustomerResponseDTO;
import ec.com.sofka.exceptions.RequestValidationException;
import ec.com.sofka.handlers.customer.CreateCustomerHandler;
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
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConsumerRouterTest {
    @Mock
    private RequestValidator requestValidator;

    @Mock
    private CreateCustomerHandler createCustomerHandler;

    @InjectMocks
    private CustomerRouter customerRouter;

    private WebTestClient webTestClient;

    private CustomerRequestDTO customerRequestDTO;
    private CustomerResponseDTO customerResponseDTO;

    @BeforeEach
    void setUp() {

        webTestClient = WebTestClient.bindToRouterFunction(customerRouter.customersRouters()).build();

        customerRequestDTO = new CustomerRequestDTO("Juan", "Zambrano", "1000000000", "user@gmail.com", "Customer123.", "ACTIVE");
        customerResponseDTO = new CustomerResponseDTO("Juan", "Zambrano", "1000000000", "ACTIVE");

    }

    @Test
    @DisplayName("Should create account when request passes to validation")
    void shouldCreateCustomerSuccessfully() {
        when(createCustomerHandler.save(any())).thenReturn(Mono.just(customerResponseDTO));

        webTestClient.post()
                .uri("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(customerRequestDTO)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.firstName").isEqualTo(customerRequestDTO.getFirstName())
                .jsonPath("$.identityCard").isEqualTo(customerRequestDTO.getIdentityCard());
    }

    @Test
    @DisplayName("Should return validation errors when exist error")
    void shouldReturnValidationErrors() {
        CustomerRequestDTO invalidRequest = new CustomerRequestDTO(null, "Zambrano", "1000000000", "invalid-email", "pass", "ACTIVE");

        doThrow(new RequestValidationException(List.of("firstName cannot be null")))
                .when(requestValidator).validate(any(CustomerRequestDTO.class));


        webTestClient.post()
                .uri("/api/customers")
                .bodyValue(invalidRequest)
                .exchange()
                .expectStatus().isEqualTo(500);
    }

}