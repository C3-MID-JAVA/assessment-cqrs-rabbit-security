package ec.com.sofka.queries.usecases;

import ec.com.sofka.gateway.ErrorLogRepository;
import ec.com.sofka.model.ErrorMessage;
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
public class ErrorLogSavedViewUseCaseTest {

    @Mock
    private ErrorLogRepository errorLogRepository;

    @InjectMocks
    private ErrorLogSavedViewUseCase errorLogSavedViewUseCase;

    private ErrorMessage errorMessage;

    @BeforeEach
    void setUp() {
        errorMessage = new ErrorMessage("Error message", "Error source");
    }

    @Test
    @DisplayName("should save error message when accept is called")
    void shouldSaveErrorMessage_WhenAcceptIsCalled() {
        when(errorLogRepository.save(errorMessage)).thenReturn(Mono.just(errorMessage));

        errorLogSavedViewUseCase.accept(errorMessage);

        verify(errorLogRepository, times(1)).save(errorMessage);
    }

}