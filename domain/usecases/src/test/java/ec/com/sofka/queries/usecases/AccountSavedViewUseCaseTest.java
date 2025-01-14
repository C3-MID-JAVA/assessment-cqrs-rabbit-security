package ec.com.sofka.queries.usecases;

import ec.com.sofka.gateway.AccountRepository;
import ec.com.sofka.gateway.dto.AccountDTO;
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
public class AccountSavedViewUseCaseTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountSavedViewUseCase accountSavedViewUseCase;

    private AccountDTO accountDTO;

    @BeforeEach
    void setUp() {
        accountDTO = new AccountDTO("1234567890", new BigDecimal("1000.00"), StatusEnum.ACTIVE, null);
    }

    @Test
    @DisplayName("should save account when accept is called")
    void shouldSaveAccount_WhenAcceptIsCalled() {
        when(accountRepository.save(accountDTO)).thenReturn(Mono.just(accountDTO));

        accountSavedViewUseCase.accept(accountDTO);

        verify(accountRepository, times(1)).save(accountDTO);
    }

}