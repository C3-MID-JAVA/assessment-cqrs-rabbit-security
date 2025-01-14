package ec.com.sofka.command;

import ec.com.sofka.UserDTO;
import ec.com.sofka.gateway.AuthenticationRepository;
import ec.com.sofka.gateway.JwtService;
import ec.com.sofka.gateway.PasswordHasher;
import ec.com.sofka.usecases.LoginAuthenticationUseCase;
import ec.com.sofka.usecases.commands.LoginAuthenticationCommand;
import ec.com.sofka.usecases.queries.AuthenticationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.nio.file.AccessDeniedException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class LoginAuthenticationUseCaseTest {

    @Mock
    private AuthenticationRepository authenticationRepository;

    @Mock
    private PasswordHasher passwordHasher;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private LoginAuthenticationUseCase loginAuthenticationUseCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testExecute_AdminNotFound() {
        LoginAuthenticationCommand command = new LoginAuthenticationCommand("test@example.com", "password");
        when(authenticationRepository.findByEmail(command.getEmail())).thenReturn(Mono.empty());

        Mono<AuthenticationResponse> result = loginAuthenticationUseCase.execute(command);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Admin not found"))
                .verify();
    }

    @Test
    public void testExecute_BadCredentials() {
        LoginAuthenticationCommand command = new LoginAuthenticationCommand("test@example.com", "password");
        UserDTO userDTO = new UserDTO("1", command.getEmail(), "hashedPassword");
        when(authenticationRepository.findByEmail(command.getEmail())).thenReturn(Mono.just(userDTO));
        when(passwordHasher.verifyPassword(command.getPassword(), userDTO.getPassword())).thenReturn(false);

        Mono<AuthenticationResponse> result = loginAuthenticationUseCase.execute(command);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getCause() instanceof AccessDeniedException &&
                        throwable.getCause().getMessage().equals("Bad credentials"))
                .verify();
    }

    @Test
    public void testExecute_SuccessfulLogin() {
        LoginAuthenticationCommand command = new LoginAuthenticationCommand("test@example.com", "password");
        UserDTO userDTO = new UserDTO("1", command.getEmail(), "hashedPassword");
        when(authenticationRepository.findByEmail(command.getEmail())).thenReturn(Mono.just(userDTO));
        when(passwordHasher.verifyPassword(command.getPassword(), userDTO.getPassword())).thenReturn(true);
        when(jwtService.generateToken(userDTO.getEmail())).thenReturn("token");

        Mono<AuthenticationResponse> result = loginAuthenticationUseCase.execute(command);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getToken().equals("token"))
                .verifyComplete();
    }
}