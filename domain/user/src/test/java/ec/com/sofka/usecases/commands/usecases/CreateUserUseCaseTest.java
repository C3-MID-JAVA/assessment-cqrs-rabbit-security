package ec.com.sofka.usecases.commands.usecases;

import ec.com.sofka.usecases.commands.CreateUserCommand;
import ec.com.sofka.usecases.gateway.IUserRepository;
import ec.com.sofka.usecases.gateway.UserDTO;
import ec.com.sofka.usecases.queries.responses.CreateUserResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private CreateUserUseCase createUserUseCase;



    @Test
    void execute_userDoesNotExist_createsUser() {

        CreateUserCommand command = new CreateUserCommand("newUser", "password123", "ROLE_USER");
        UserDTO savedUser = new UserDTO("1", "newUser", "password123", "ROLE_USER");

        when(userRepository.findByUsername(eq("newUser"))).thenReturn(Mono.empty());
        when(userRepository.save(any(UserDTO.class))).thenReturn(Mono.just(savedUser));


        Mono<CreateUserResponse> result = createUserUseCase.execute(command);

        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getUsername().equals("newUser") &&
                                response.getPassword().equals("password123") &&
                                response.getRoles().equals("ROLE_USER"))
                .verifyComplete();
    }

    @Test
    void execute_userAlreadyExists_throwsException() {

        CreateUserCommand command = new CreateUserCommand("existingUser", "password123", "ROLE_USER");
        UserDTO existingUser = new UserDTO("1", "existingUser", "password123", "ROLE_USER");

        when(userRepository.findByUsername(eq("existingUser"))).thenReturn(Mono.just(existingUser));


        Mono<CreateUserResponse> result = createUserUseCase.execute(command);

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof RuntimeException &&
                                throwable.getMessage().equals("User already exists"))
                .verify();
    }
}