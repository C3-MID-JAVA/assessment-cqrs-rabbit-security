package ec.com.sofka.commandas.usecases.user;

import ec.com.sofka.aggregates.auth.entities.user.User;
import ec.com.sofka.aggregates.auth.entities.user.values.UserId;
import ec.com.sofka.aggregates.auth.entities.user.values.objects.Email;
import ec.com.sofka.aggregates.auth.entities.user.values.objects.Password;
import ec.com.sofka.commands.UserCommand;
import ec.com.sofka.commands.usecases.user.CreateUserUseCase;
import ec.com.sofka.exceptions.ConflictException;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.gateway.UserRepository;
import ec.com.sofka.gateway.busMessage.ErrorBusMessage;
import ec.com.sofka.gateway.busMessage.EventBusMessage;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.mapper.UserMapper;
import ec.com.sofka.utils.enums.RoleEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @Mock
    private IEventStore repository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ErrorBusMessage errorBusMessage;

    @Mock
    private EventBusMessage eventBusMessage;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CreateUserUseCase useCase;

    @Test
    void createUserSuccessfully() {
        UserCommand command = new UserCommand(
                "John",
                "Doe",
                "john.doe@example.com",
                "Pass1234.",
                RoleEnum.ROLE_BANK_TELLER
        );

        when(userRepository.findByEmail(command.getEmail()))
                .thenReturn(Mono.empty());
        when(passwordEncoder.encode(command.getPassword()))
                .thenReturn("encodedPassword");

        StepVerifier.create(useCase.execute(command))
                .expectNextMatches(response ->
                        response.getFirstName().equals("John") &&
                                response.getLastName().equals("Doe") &&
                                response.getEmail().equals("john.doe@example.com") &&
                                response.getRole().name().equals("USER")
                )
                .verifyComplete();

        verify(userRepository).findByEmail("john.doe@example.com");
        verify(passwordEncoder).encode("password123");
        verify(eventBusMessage, times(1)).sendEvent(any(DomainEvent.class));
        verify(errorBusMessage, never()).sendMsg(any());
    }

    @Test
    void createUserFailsWhenEmailAlreadyExists() {
        UserCommand command = new UserCommand(
                "John",
                "Doe",
                "john.doe@example.com",
                "Pass1234.",
                RoleEnum.ROLE_BANK_TELLER
        );

        User existingUser = new User(
                UserId.of("123"),
                "John",
                "Doe",
                Email.of("john.doe@example.com"),
                Password.of("Pass1234."),
                RoleEnum.ROLE_BANK_TELLER
        );

        when(userRepository.findByEmail(command.getEmail()))
                .thenReturn(Mono.just(UserMapper.mapToDTOFromModel(existingUser)));

        StepVerifier.create(useCase.execute(command))
                .expectErrorMatches(throwable -> throwable instanceof ConflictException &&
                        throwable.getMessage().equals("The user is already registered."))
                .verify();

        verify(userRepository).findByEmail("john.doe@example.com");
        verify(passwordEncoder, never()).encode(anyString());
        verify(repository, never()).save(any());
        verify(eventBusMessage, never()).sendEvent(any());
    }

}