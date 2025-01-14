package ec.com.sofka.commandas.usecases.account;

import ec.com.sofka.aggregates.account.entities.customer.Customer;
import ec.com.sofka.aggregates.account.entities.customer.values.CustomerId;
import ec.com.sofka.aggregates.account.entities.customer.values.objects.IdentityCard;
import ec.com.sofka.commands.AccountCommand;
import ec.com.sofka.commands.usecases.account.CreateAccountUseCase;
import ec.com.sofka.exceptions.RecordNotFoundException;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.gateway.busMessage.EventBusMessage;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.queries.query.customer.FindCustomerByIdUseCase;
import ec.com.sofka.utils.enums.StatusEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateAccountUseCaseTest {

    @Mock
    private IEventStore repository;

    @Mock
    private FindCustomerByIdUseCase findCustomerByIdUseCase;

    @Mock
    private EventBusMessage eventBusMessage;

    @InjectMocks
    private CreateAccountUseCase useCase;

    @Test
    void createAccountSuccessfully() {
        AccountCommand command = new AccountCommand(
                "2222222222",
                BigDecimal.valueOf(5000),
                StatusEnum.ACTIVE,
                "123"
        );

        Customer customer = new Customer(CustomerId.of("123"), "John", "Doe", IdentityCard.of("1234567890"), StatusEnum.ACTIVE);

        when(findCustomerByIdUseCase.getById(command.getCustomerId()))
                .thenReturn(Mono.just(customer));

        when(repository.save(any(DomainEvent.class)))
                .thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute(command))
                .expectNextMatches(response ->
                        response.getNumber().equals("2222222222") &&
                                response.getBalance().equals(BigDecimal.valueOf(5000)) &&
                                response.getStatus().name().equals("ACTIVE"))
                .verifyComplete();

        verify(findCustomerByIdUseCase).getById("123");
        verify(repository, times(1)).save(any(DomainEvent.class));
    }

    @Test
    void createAccountFailsWhenCustomerNotFound() {
        AccountCommand command = new AccountCommand(
                "2222222222",
                BigDecimal.valueOf(5000),
                StatusEnum.ACTIVE,
                "123"
        );

        when(findCustomerByIdUseCase.getById(command.getCustomerId()))
                .thenReturn(Mono.error(new RecordNotFoundException("User not found.")));

        StepVerifier.create(useCase.execute(command))
                .expectError(RecordNotFoundException.class)
                .verify();

        verify(findCustomerByIdUseCase).getById("123");
        verify(repository, never()).save(any());
        verify(eventBusMessage, never()).sendEvent(any());
    }

}
