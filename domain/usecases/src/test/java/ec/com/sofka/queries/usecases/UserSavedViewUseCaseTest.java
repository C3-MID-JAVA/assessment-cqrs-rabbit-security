package ec.com.sofka.queries.usecases;

import ec.com.sofka.gateway.UserRepository;
import ec.com.sofka.gateway.dto.UserDTO;
import ec.com.sofka.utils.enums.RoleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserSavedViewUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserSavedViewUseCase userSavedViewUseCase;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO("Jhon", "Doe", "user@example.com", "Test123.", RoleEnum.ROLE_BANK_TELLER);
    }

    @Test
    @DisplayName("should save user when accept is called")
    void shouldSaveUser_WhenAcceptIsCalled() {
        when(userRepository.save(userDTO)).thenReturn(Mono.just(userDTO));

        userSavedViewUseCase.accept(userDTO);

        verify(userRepository, times(1)).save(userDTO);
    }

}