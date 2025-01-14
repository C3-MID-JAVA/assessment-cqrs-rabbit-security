package ec.com.sofka.queries.usecases;

import ec.com.sofka.gateway.TransactionRepository;
import ec.com.sofka.gateway.dto.TransactionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionSavedViewUseCaseTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionSavedViewUseCase transactionSavedViewUseCase;

    private TransactionDTO transactionDTO;

    @BeforeEach
    void setUp() {
        transactionDTO = new TransactionDTO("1234567890", "deposit", new BigDecimal(100), "12-01-2025", null, null);
    }

    @Test
    @DisplayName("should save transaction when accept is called")
    void shouldSaveTransaction_WhenAcceptIsCalled() {
        when(transactionRepository.save(transactionDTO)).thenReturn(Mono.just(transactionDTO));

        transactionSavedViewUseCase.accept(transactionDTO);

        verify(transactionRepository, times(1)).save(transactionDTO);
    }

}