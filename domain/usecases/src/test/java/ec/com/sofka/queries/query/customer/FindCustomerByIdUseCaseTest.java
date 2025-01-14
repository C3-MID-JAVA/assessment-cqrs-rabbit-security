package ec.com.sofka.queries.query.customer;

import ec.com.sofka.aggregates.account.entities.customer.Customer;
import ec.com.sofka.aggregates.account.entities.customer.values.CustomerId;
import ec.com.sofka.aggregates.account.entities.customer.values.objects.IdentityCard;
import ec.com.sofka.exceptions.RecordNotFoundException;
import ec.com.sofka.gateway.CustomerRepository;
import ec.com.sofka.gateway.busMessage.ErrorBusMessage;
import ec.com.sofka.generics.shared.PropertyQuery;
import ec.com.sofka.generics.shared.QueryResponse;
import ec.com.sofka.mapper.CustomerMapper;
import ec.com.sofka.queries.responses.CustomerResponse;
import ec.com.sofka.utils.enums.StatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class FindCustomerByIdUseCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private FindCustomerByIdUseCase findCustomerByIdUseCase;

    @Mock
    private ErrorBusMessage errorBusMessage;

    private Customer customer;
    private PropertyQuery propertyQuery;

    @BeforeEach
    void setUp() {
        customer = new Customer(CustomerId.of("1"), "John", "Doe", IdentityCard.of("1234567890"), StatusEnum.ACTIVE);
        propertyQuery = new PropertyQuery(customer.getId().getValue());
    }

    @Test
    @DisplayName("When finding customer with valid ID should return customer response")
    void findExistingCustomerTest() {
        when(customerRepository.findById(eq(propertyQuery.getProperty())))
                .thenReturn(Mono.just(CustomerMapper.mapToDTOFromModel(customer)));

        Mono<QueryResponse<CustomerResponse>> result = findCustomerByIdUseCase.get(propertyQuery);

        StepVerifier.create(result)
                .expectNextMatches(response -> {
                    CustomerResponse customerResponse = response.getSingleResult()
                            .orElseThrow(() -> new RecordNotFoundException("User not found"));
                    return customerResponse.getIdentityCard().equals(customer.getIdentityCard().getValue())
                            && customerResponse.getStatus().name().equals(customer.getStatus().name());
                })
                .verifyComplete();

        verify(customerRepository).findById(eq(propertyQuery.getProperty()));
        verifyNoInteractions(errorBusMessage);
    }

    @Test
    @DisplayName("When finding non-existing customer should send error message and throw exception")
    void findNonExistingCustomerTest() {
        when(customerRepository.findById(propertyQuery.getProperty())).thenReturn(Mono.empty());

        Mono<QueryResponse<CustomerResponse>> result = findCustomerByIdUseCase.get(propertyQuery);

        StepVerifier.create(result)
                .expectError(RecordNotFoundException.class)
                .verify();

        verify(customerRepository).findById(customer.getId().getValue());
        verify(errorBusMessage).sendMsg(argThat(errorMessage ->
                errorMessage.getError().equals("User not found") &&
                        errorMessage.getProcess().equals("Get User by Id")));
    }

}