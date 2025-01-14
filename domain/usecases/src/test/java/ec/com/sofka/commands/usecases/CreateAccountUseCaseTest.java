package ec.com.sofka.commands.usecases;

import ec.com.sofka.aggregate.account.events.AccountCreated;
import ec.com.sofka.commands.CreateAccountCommand;
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
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

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
    void execute() {
    }

    @Test
    void createAccountSuccessfully() {

        CreateAccountCommand command = new CreateAccountCommand(
                BigDecimal.valueOf(1000.0),"123456789", "John Doe", "Savings"
        );

        when(eventStore.findAllAggregate("customer")).thenReturn(Flux.empty());


        StepVerifier.create(useCase.execute(command))
                .expectNextMatches(response -> {
                    return response.getAccountNumber().equals("123456789")
                            && response.getOwnerName().equals("John Doe")
                            && response.getAccountType().equals("Savings")
                            && Objects.equals(response.getAccountBalance(), BigDecimal.valueOf(1000.0));
                })
                .verifyComplete();


        verify(eventStore, times(1)).findAllAggregate("customer");
        verify(busEvent, times(1)).sendEventAccountCreated(any());
    }

    @Test
    void createAccountFailsWhenAccountExists() {

        CreateAccountCommand command = new CreateAccountCommand(
                BigDecimal.valueOf(1000.0),"123456789", "John Doe", "Savings"
        );


        DomainEvent existingEvent = mockDomainEventWithAccountNumber();
        when(eventStore.findAllAggregate("customer"))
                .thenReturn(Flux.just(existingEvent));


        StepVerifier.create(useCase.execute(command))
                .expectErrorMessage("Account with number 123456789 already exists")
                .verify();



        verify(eventStore, never()).save(any());
        verify(busEvent, never()).sendEventAccountCreated(any());
    }


    private DomainEvent mockDomainEventWithAccountNumber() {
        AccountCreated event = new AccountCreated(
                null,
                "123456789",
                BigDecimal.valueOf(1000.00),
                "John Doe",
                "Savings" );
        event.setAggregateRootId("customerId");
        event.setAggregateRootName("customer");
        return event;
    }

}