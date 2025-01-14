package ec.com.sofka.queries.usecases;

import ec.com.sofka.gateway.TransactionTypeRepository;
import ec.com.sofka.gateway.dto.TransactionTypeDTO;
import ec.com.sofka.utils.enums.StatusEnum;
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
public class TransactionTypeSavedViewUseCaseTest {
    @Mock
    private TransactionTypeRepository transactionTypeRepository;

    @InjectMocks
    private TransactionTypeSavedViewUseCase transactionTypeSavedViewUseCase;

    private TransactionTypeDTO transactionTypeDTO;

    @BeforeEach
    void setUp() {
        transactionTypeDTO = new TransactionTypeDTO("deposit", "deposit", new BigDecimal(10), true, true, StatusEnum.ACTIVE);
    }

    @Test
    @DisplayName("should save transaction type when accept is called")
    void shouldSaveTransactionType_WhenAcceptIsCalled() {
        when(transactionTypeRepository.save(transactionTypeDTO)).thenReturn(Mono.just(transactionTypeDTO));

        transactionTypeSavedViewUseCase.accept(transactionTypeDTO);

        verify(transactionTypeRepository, times(1)).save(transactionTypeDTO);
    }

}