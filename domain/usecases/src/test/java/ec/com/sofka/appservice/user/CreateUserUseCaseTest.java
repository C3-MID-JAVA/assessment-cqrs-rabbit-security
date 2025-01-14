package ec.com.sofka.appservice.user;

import ec.com.sofka.ConflictException;
import ec.com.sofka.appservice.commands.CreateUserCommand;
import ec.com.sofka.appservice.commands.usecases.CreateUserUseCase;
import ec.com.sofka.appservice.gateway.IUserRepository;
import ec.com.sofka.appservice.gateway.dto.UserDTO;
import ec.com.sofka.appservice.queries.responses.CreateUserResponse;
import ec.com.sofka.enums.ROLE;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class CreateUserUseCaseTest {

    @Mock
    private IUserRepository userRepository;

    private CreateUserUseCase createUserUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        createUserUseCase = new CreateUserUseCase(userRepository);
    }

    @Test
    void shouldCreateUserSuccessfully() {
        // Datos de prueba
        String username = "newuser";
        String password = "password123";
        String roles = ROLE.ADMIN.name();

        // Crear el comando
        CreateUserCommand command = new CreateUserCommand(username, password, roles);

        // Simulamos que el usuario no existe
        when(userRepository.findByUsername(username)).thenReturn(Mono.empty());

        // Simulamos la creación del usuario
        UserDTO userDTO = new UserDTO("1", username, password, roles);
        when(userRepository.save(any(UserDTO.class))).thenReturn(Mono.just(userDTO));

        // Ejecución y verificación
        StepVerifier.create(createUserUseCase.execute(command))
                .expectNextMatches(response ->
                        response.getUsername().equals(username) &&
                                response.getPassword().equals(password) &&
                                response.getRoles().equals(roles)
                )
                .verifyComplete();

        // Verificación de interacciones
        verify(userRepository, times(1)).findByUsername(username);
        verify(userRepository, times(1)).save(any(UserDTO.class));
    }

    @Test
    void shouldCreateUserSuccessfullyIfUsernameDoesNotExist() {
        // Datos de prueba
        String username = "newuser";
        String password = "password123";
        String roles = ROLE.ADMIN.name();

        // Crear el comando
        CreateUserCommand command = new CreateUserCommand(username, password, roles);

        // Simulamos que el usuario NO existe en la base de datos
        when(userRepository.findByUsername(username)).thenReturn(Mono.empty());  // No existe el usuario

        // Simulamos que el usuario se guarda correctamente
        when(userRepository.save(any(UserDTO.class))).thenReturn(Mono.just(new UserDTO("2", username, password, roles)));

        // Ejecución y verificación (esperamos la creación exitosa)
        StepVerifier.create(createUserUseCase.execute(command))
                .expectNextMatches(response ->
                        response.getUsername().equals(username) &&
                                response.getRoles().equals(roles) &&
                                response.getPassword().equals(password))
                .verifyComplete();

        // Verificación de interacciones
        verify(userRepository, times(1)).findByUsername(username);  // Se debe llamar a findByUsername
        verify(userRepository, times(1)).save(any(UserDTO.class));   // Se debe llamar a save para guardar al nuevo usuario
    }




    @Test
    void shouldThrowIllegalArgumentExceptionIfRoleIsInvalid() {
        // Datos de prueba con un rol inválido
        String username = "newuser";
        String password = "password123";
        String roles = "INVALID_ROLE"; // Rol no válido

        // Crear el comando
        CreateUserCommand command = new CreateUserCommand(username, password, roles);

        // Ejecución y verificación (se espera un error)
        StepVerifier.create(createUserUseCase.execute(command))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("The role is not valid"))
                .verify();

        // Verificación de interacciones
        verify(userRepository, times(0)).findByUsername(any());
        verify(userRepository, times(0)).save(any(UserDTO.class));
    }
}
