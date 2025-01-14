package ec.com.sofka.commandas.usecases.customer;

import ec.com.sofka.aggregates.account.entities.customer.Customer;
import ec.com.sofka.aggregates.account.entities.customer.values.CustomerId;
import ec.com.sofka.aggregates.account.entities.customer.values.objects.IdentityCard;
import ec.com.sofka.commands.CustomerCommand;
import ec.com.sofka.commands.usecases.customer.CreateCustomerUseCase;
import ec.com.sofka.exceptions.ConflictException;
import ec.com.sofka.gateway.CustomerRepository;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.gateway.busMessage.ErrorBusMessage;
import ec.com.sofka.gateway.busMessage.EventBusMessage;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.mapper.CustomerMapper;
import ec.com.sofka.utils.enums.StatusEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCustomerUseCaseTest {

    @Mock
    private IEventStore repository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ErrorBusMessage errorBusMessage;

    @Mock
    private EventBusMessage eventBusMessage;

    @InjectMocks
    private CreateCustomerUseCase useCase;

    @Test
    void createCustomerSuccessfully() {
        CustomerCommand command = new CustomerCommand(
                "John",
                "Doe",
                "1234567890",
                    StatusEnum.ACTIVE
        );

        when(customerRepository.findByIdentityCard(command.getIdentityCard()))
                .thenReturn(Mono.empty());

        when(repository.save(any(DomainEvent.class)))
                .thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute(command))
                .expectNextMatches(response ->
                        response.getFirstName().equals("John") &&
                                response.getLastName().equals("Doe") &&
                                response.getIdentityCard().equals("1234567890") &&
                                response.getStatus().name().equals("ACTIVE"))
                .verifyComplete();

        verify(customerRepository).findByIdentityCard("1234567890");
        verify(repository, times(1)).save(any(DomainEvent.class));
        verify(errorBusMessage, never()).sendMsg(any());
    }

    @Test
    void createCustomerFailsWhenCustomerAlreadyExists() {
        CustomerCommand command = new CustomerCommand(
                "John",
                "Doe",
                "0987654321",
                StatusEnum.ACTIVE
        );

        Customer existingCustomer = new Customer(CustomerId.of("123"), "Jane", "Doe", IdentityCard.of("0987654321"), StatusEnum.ACTIVE);

        when(customerRepository.findByIdentityCard(command.getIdentityCard()))
                .thenReturn(Mono.just(CustomerMapper.mapToDTOFromModel(existingCustomer)));

        StepVerifier.create(useCase.execute(command))
                .expectErrorMatches(throwable -> throwable instanceof ConflictException &&
                        throwable.getMessage().equals("The customer is already registered."))
                .verify();

        verify(customerRepository).findByIdentityCard("0987654321");
        verify(repository, never()).save(any());
        verify(eventBusMessage, never()).sendEvent(any());
    }

}