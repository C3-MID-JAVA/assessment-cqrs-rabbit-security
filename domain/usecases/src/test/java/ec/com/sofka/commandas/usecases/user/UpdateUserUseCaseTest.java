package ec.com.sofka.commandas.usecases.user;

import ec.com.sofka.aggregates.auth.events.UserUpdated;
import ec.com.sofka.commands.UserCommand;
import ec.com.sofka.commands.usecases.user.UpdateUserUseCase;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.gateway.UserRepository;
import ec.com.sofka.gateway.busMessage.ErrorBusMessage;
import ec.com.sofka.gateway.busMessage.EventBusMessage;
import ec.com.sofka.gateway.dto.UserDTO;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.model.ErrorMessage;
import ec.com.sofka.utils.enums.RoleEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class UpdateUserUseCaseTest {

    @Mock
    private IEventStore eventStore;

    @Mock
    private ErrorBusMessage errorBusMessage;

    @Mock
    private EventBusMessage eventBusMessage;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UpdateUserUseCase useCase;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void updateUserSuccessfully() {
        UserCommand command = new UserCommand("Jhon", "Doe", "test@gmail.com", "Test123.", RoleEnum.ROLE_BANK_TELLER);

        DomainEvent existingEvent = mockDomainEvent();
        when(userRepository.findByEmail(any())).thenReturn(Mono.empty());
        when(eventStore.findAggregate(any())).thenReturn(Flux.just(existingEvent));
        when(eventStore.save(any(DomainEvent.class))).thenReturn(Mono.just(new UserUpdated("id", "Jhon", "Doe", "test@gmail.com", "Test123.", RoleEnum.ROLE_BANK_TELLER)));

        StepVerifier.create(useCase.execute(command))
                .expectNextMatches(response -> {
                    return response.getEmail().equals("test@gmail.com")
                            && response.getFirstName().equals("Jhon")
                            && response.getRole().name().equals(RoleEnum.ROLE_BANK_TELLER.name());
                })
                .verifyComplete();

        verify(eventStore, times(1)).findAggregate(any());
        verify(eventStore, times(1)).save(any(DomainEvent.class));
        verify(eventBusMessage, times(1)).sendEvent(any(DomainEvent.class));
    }

    @Test
    void updateUserFailsWhenUserDoesNotExist() {
        UserCommand command = new UserCommand("Jhon", "Doe", "test@gmail.com", "Test123.", RoleEnum.ROLE_BANK_TELLER);

        when(userRepository.findByEmail(any())).thenReturn(Mono.just(new UserDTO("Jhon", "Doe", "test@gmail.com", "Test123.", RoleEnum.ROLE_BANK_TELLER)));
        doNothing().when(errorBusMessage).sendMsg(any(ErrorMessage.class));

        StepVerifier.create(useCase.execute(command))
                .expectErrorMessage("The user is already registered.")
                .verify();

        verify(eventStore, times(1)).findAggregate(any());
        verify(eventStore, never()).save(any(DomainEvent.class));
    }

    private DomainEvent mockDomainEvent() {
        UserUpdated event = new UserUpdated("userId", "Jhon", "Doe", "test@gmail.com", "Test123.", RoleEnum.ROLE_BANK_TELLER);
        event.setAggregateRootId("aggregateId");
        return event;
    }

}