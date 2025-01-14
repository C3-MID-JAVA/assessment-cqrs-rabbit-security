package ec.com.sofka.command;

import ec.com.sofka.UserDTO;
import ec.com.sofka.gateway.AuthenticationRepository;
import ec.com.sofka.gateway.JwtService;
import ec.com.sofka.gateway.PasswordHasher;
import ec.com.sofka.usecases.RegisterAuthenticationUseCase;
import ec.com.sofka.usecases.commands.RegisterAuthenticationCommand;
import ec.com.sofka.usecases.queries.RegisterResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class RegisterAuthenticationUseCaseTest {

    @Mock
    private AuthenticationRepository authenticationRepository;

    @Mock
    private PasswordHasher passwordHasher;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private RegisterAuthenticationUseCase registerAuthenticationUseCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testExecute_AdminAlreadyExists() {
        RegisterAuthenticationCommand command = new RegisterAuthenticationCommand("test@example.com", "password");
        when(authenticationRepository.findByEmail(command.getEmail())).thenReturn(Mono.just(new UserDTO("1", command.getEmail(), "hashedPassword")));

        Mono<RegisterResponse> result = registerAuthenticationUseCase.execute(command);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Admin already exists"))
                .verify();
    }

    @Test
    public void testExecute_SuccessfulRegistration() {
        RegisterAuthenticationCommand command = new RegisterAuthenticationCommand("test@example.com", "password");
        when(authenticationRepository.findByEmail(command.getEmail())).thenReturn(Mono.empty());
        when(passwordHasher.hashPassword(command.getPassword())).thenReturn("hashedPassword");
        when(authenticationRepository.save(any(UserDTO.class))).thenReturn(Mono.just(new UserDTO("1", command.getEmail(), "hashedPassword")));

        Mono<RegisterResponse> result = registerAuthenticationUseCase.execute(command);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getEmail().equals(command.getEmail()))
                .verifyComplete();
    }

    @Test
    public void testExecute_HashingPasswordFails() {
        RegisterAuthenticationCommand command = new RegisterAuthenticationCommand("test@example.com", "password");
        when(authenticationRepository.findByEmail(command.getEmail())).thenReturn(Mono.empty());
        when(passwordHasher.hashPassword(command.getPassword())).thenThrow(new RuntimeException("Hashing failed"));

        Mono<RegisterResponse> result = registerAuthenticationUseCase.execute(command);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Hashing failed"))
                .verify();
    }

    @Test
    public void testExecute_SaveUserFails() {
        RegisterAuthenticationCommand command = new RegisterAuthenticationCommand("test@example.com", "password");
        when(authenticationRepository.findByEmail(command.getEmail())).thenReturn(Mono.empty());
        when(passwordHasher.hashPassword(command.getPassword())).thenReturn("hashedPassword");
        when(authenticationRepository.save(any(UserDTO.class))).thenReturn(Mono.error(new RuntimeException("Save failed")));

        Mono<RegisterResponse> result = registerAuthenticationUseCase.execute(command);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Save failed"))
                .verify();
    }
}