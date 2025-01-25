package ec.com.sofka.user.queries;

import ec.com.sofka.gateway.UserRepository;
import ec.com.sofka.gateway.dto.UserDTO;
import ec.com.sofka.user.queries.usecases.GetAllUserViewUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Objects;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GetAllUserViewUseCase useCase;

    @Test
    void shouldReturnAllUsersSuccessfully() {
        UserDTO user1Created = new UserDTO("user1", "John Doe", "DOC001");
        UserDTO user2Created = new UserDTO("user2", "Jane Doe", "DOC002");

        when(userRepository.getAll()).thenReturn(Flux.just(user1Created, user2Created));

        StepVerifier.create(useCase.get())
                .expectNextMatches(response ->
                        response.getMultipleResults().get(0).getId().equals("user1") &&
                                response.getMultipleResults().get(0).getName().equals("John Doe") &&
                                response.getMultipleResults().get(0).getDocumentId().equals("DOC001") &&
                                response.getMultipleResults().get(1).getId().equals("user2") &&
                                response.getMultipleResults().get(1).getName().equals("Jane Doe") &&
                                response.getMultipleResults().get(1).getDocumentId().equals("DOC002")
                )
                .verifyComplete();

        verify(userRepository, times(1)).getAll();
    }

    @Test
    void shouldReturnEmptyFluxWhenNoUsersExist() {
        when(userRepository.getAll()).thenReturn(Flux.empty());

        StepVerifier.create(useCase.get())
                .expectNextMatches(Objects::nonNull)
                .verifyComplete();

        verify(userRepository, times(1)).getAll();
    }
}
