package ec.com.sofka.user.queries;

import ec.com.sofka.gateway.UserRepository;
import ec.com.sofka.gateway.dto.UserDTO;
import ec.com.sofka.user.queries.usecases.UserSavedViewUseCase;
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

    @Test
    void shouldSaveUserSuccessfully() {
        UserDTO userDTO = new UserDTO("user1", "John Doe", "DOC001");

        when(userRepository.save(userDTO)).thenReturn(Mono.just(userDTO));

        userSavedViewUseCase.accept(userDTO);

        verify(userRepository, times(1)).save(userDTO);
    }
}
