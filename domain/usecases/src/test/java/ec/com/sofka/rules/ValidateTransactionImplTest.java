package ec.com.sofka.rules;

import ec.com.sofka.exceptions.TransactionRejectedException;
import ec.com.sofka.gateway.busMessage.ErrorBusMessage;
import ec.com.sofka.gateway.dto.AccountDTO;
import ec.com.sofka.gateway.dto.TransactionDTO;
import ec.com.sofka.gateway.dto.TransactionTypeDTO;
import ec.com.sofka.mapper.AccountMapper;
import ec.com.sofka.queries.query.account.FindAccountByNumberUseCase;
import ec.com.sofka.rules.impl.ValidateTransactionImpl;
import ec.com.sofka.utils.enums.StatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ValidateTransactionImplTest {

    @Mock
    private FindAccountByNumberUseCase findAccountByNumberUseCase;

    @Mock
    private ErrorBusMessage errorBusMessage;

    @InjectMocks
    private ValidateTransactionImpl validateTransactionImpl;

    private AccountDTO account;
    private TransactionTypeDTO transactionType;
    private TransactionDTO transaction;

    @BeforeEach
    void setUp() {
        account = new AccountDTO("1234567890", new BigDecimal(100), StatusEnum.ACTIVE, null);
        transactionType = new TransactionTypeDTO("Retire", "retire", new BigDecimal(5), true, true, StatusEnum.ACTIVE);
        transaction = new TransactionDTO("1234567890", "retire", new BigDecimal(10), "12-01-2025", account, transactionType);
    }

    @Test
    @DisplayName("should validate transaction successfully when account is active and has sufficient funds")
    void shouldValidateTransactionSuccessfully_WhenAccountIsActiveAndHasSufficientFunds() {
        transaction.setAmount(new BigDecimal("50"));
        account.setBalance(new BigDecimal("100"));

        when(findAccountByNumberUseCase.getByNumberAccount(anyString()))
                .thenReturn(Mono.just(AccountMapper.mapToModelFromDTO(account)));

        Mono<TransactionDTO> result = validateTransactionImpl.validateTransaction(transaction, "1234567890");

        StepVerifier.create(result)
                .expectNext(transaction)
                .expectComplete()
                .verify();

        verify(findAccountByNumberUseCase).getByNumberAccount(anyString());
    }

    @Test
    @DisplayName("should reject transaction when account is inactive")
    void shouldRejectTransaction_WhenAccountIsInactive() {
        account.setStatus(StatusEnum.INACTIVE);

        when(findAccountByNumberUseCase.getByNumberAccount(anyString()))
                .thenReturn(Mono.just(AccountMapper.mapToModelFromDTO(account)));

        Mono<TransactionDTO> result = validateTransactionImpl.validateTransaction(transaction, "1234567890");

        StepVerifier.create(result)
                .expectError(TransactionRejectedException.class)
                .verify();

        verify(findAccountByNumberUseCase).getByNumberAccount(anyString());
    }

    @Test
    @DisplayName("should reject transaction when account has insufficient funds")
    void shouldRejectTransaction_WhenAccountHasInsufficientFunds() {
        transaction.setAmount(new BigDecimal("150"));

        when(findAccountByNumberUseCase.getByNumberAccount(anyString()))
                .thenReturn(Mono.just(AccountMapper.mapToModelFromDTO(account)));

        Mono<TransactionDTO> result = validateTransactionImpl.validateTransaction(transaction, "1234567890");

        StepVerifier.create(result)
                .expectError(TransactionRejectedException.class)
                .verify();

        verify(findAccountByNumberUseCase).getByNumberAccount(anyString());
    }

}