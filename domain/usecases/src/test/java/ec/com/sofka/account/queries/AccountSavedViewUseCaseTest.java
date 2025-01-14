package ec.com.sofka.account.queries;

import ec.com.sofka.account.queries.usecases.AccountSavedViewUseCase;
import ec.com.sofka.gateway.AccountRepository;
import ec.com.sofka.gateway.dto.AccountDTO;
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

    @Test
    void shouldSaveAccountSuccessfully() {
        AccountDTO accountDTO = new AccountDTO("1", "12345678",BigDecimal.valueOf(100), "1000");
        when(accountRepository.save(accountDTO)).thenReturn(Mono.just(accountDTO));

        accountSavedViewUseCase.accept(accountDTO);

        verify(accountRepository, times(1)).save(accountDTO);
    }


}
