package ec.com.sofka.commands.account;

import ec.com.sofka.aggregate.events.AccountCreated;
import ec.com.sofka.commands.CreateAccountCommand;
import ec.com.sofka.commands.usecases.CreateAccountUseCase;
import ec.com.sofka.gateway.BusEvent;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.generics.domain.DomainEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateAccountUseCaseTest {

    @Mock
    private IEventStore eventStore;

    @Mock
    private BusEvent busEvent;

    @InjectMocks
    private CreateAccountUseCase useCase;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createAccountSuccessfully() {
        CreateAccountCommand command = new CreateAccountCommand(
                "123456789", "John Doe", BigDecimal.valueOf(1000.0), "userId"
        );

        when(eventStore.save(any(DomainEvent.class))).thenReturn(Mono.just(new AccountCreated(
                "accountId", "123456789", BigDecimal.valueOf(1000.0), "John Doe", "Savings", "userId"
        )));

        StepVerifier.create(useCase.execute(command))
                .expectNextMatches(response -> {
                    return response.getAccountNumber().equals("123456789")
                            && response.getName().equals("John Doe")
                            && Objects.equals(response.getBalance(), BigDecimal.valueOf(1000.0));
                })
                .verifyComplete();

        verify(eventStore, times(1)).save(any(DomainEvent.class));
        verify(busEvent, times(1)).sendEvent(any(DomainEvent.class));
    }

    private DomainEvent mockDomainEventWithAccountNumber() {
        AccountCreated event = new AccountCreated(
                "accountId", "123456789", BigDecimal.valueOf(1000.0), "John Doe", "Savings", "userId"
        );
        event.setAggregateRootId("customerId");
        return event;
    }
}