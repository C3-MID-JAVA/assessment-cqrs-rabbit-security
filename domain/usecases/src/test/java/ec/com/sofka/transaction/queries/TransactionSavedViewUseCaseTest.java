package ec.com.sofka.transaction.queries;

import ec.com.sofka.gateway.TransactionRepository;
import ec.com.sofka.gateway.dto.TransactionDTO;
import ec.com.sofka.transaction.TransactionType;
import ec.com.sofka.transaction.queries.usecases.TransactionSavedViewUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionSavedViewUseCaseTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionSavedViewUseCase transactionSavedViewUseCase;

    @Test
    void shouldSaveTransactionSuccessfully() {
        TransactionDTO transactionDTO = new TransactionDTO(
                "txn1",
                BigDecimal.valueOf(1000),
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(990),
                TransactionType.ATM_DEPOSIT,
                LocalDateTime.now(),
                "acc1"
        );

        when(transactionRepository.save(transactionDTO)).thenReturn(Mono.just(transactionDTO));

        transactionSavedViewUseCase.accept(transactionDTO);

        verify(transactionRepository, times(1)).save(transactionDTO);
    }
}
