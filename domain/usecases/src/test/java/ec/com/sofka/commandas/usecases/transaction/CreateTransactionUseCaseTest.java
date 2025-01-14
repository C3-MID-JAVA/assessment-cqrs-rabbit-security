package ec.com.sofka.commandas.usecases.transaction;

import ec.com.sofka.commands.AccountCommand;
import ec.com.sofka.commands.TransactionCommand;
import ec.com.sofka.commands.usecases.account.UpdateAccountUseCase;
import ec.com.sofka.commands.usecases.transaction.CreateTransactionUseCase;
import ec.com.sofka.gateway.dto.AccountDTO;
import ec.com.sofka.gateway.dto.TransactionDTO;
import ec.com.sofka.gateway.dto.TransactionTypeDTO;
import ec.com.sofka.mapper.AccountMapper;
import ec.com.sofka.mapper.TransactionTypeMapper;
import ec.com.sofka.queries.query.transcationType.FindTransactionTypeByIdUseCase;
import ec.com.sofka.queries.responses.TransactionResponse;
import ec.com.sofka.rules.BalanceCalculator;
import ec.com.sofka.rules.ValidateTransaction;
import ec.com.sofka.utils.enums.StatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.gateway.busMessage.EventBusMessage;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateTransactionUseCaseTest {

    @Mock
    private IEventStore repository;

    @Mock
    private ValidateTransaction validateTransaction;

    @Mock
    private BalanceCalculator balanceCalculator;

    @Mock
    private FindTransactionTypeByIdUseCase findTransactionTypeByIdUseCase;

    @Mock
    private UpdateAccountUseCase updateAccountUseCase;

    @Mock
    private EventBusMessage eventBusMessage;

    private CreateTransactionUseCase createTransactionUseCase;

    @BeforeEach
    public void setUp() {
        createTransactionUseCase = new CreateTransactionUseCase(
                repository, validateTransaction, balanceCalculator,
                findTransactionTypeByIdUseCase, updateAccountUseCase, eventBusMessage);
    }

    @Test
    public void testExecute_ValidTransaction() {
        TransactionCommand command = new TransactionCommand(new BigDecimal(100), "14-01-2025", "2222222222", "transaction", "1","1");
        AccountDTO accountDTO = new AccountDTO("2222222222", BigDecimal.valueOf(5000), StatusEnum.ACTIVE, null);
        TransactionTypeDTO transactionTypeDTO = new TransactionTypeDTO("TRANSFER","Transfer money", new BigDecimal("10"),true, true, StatusEnum.ACTIVE);
        TransactionDTO transactionDTO = new TransactionDTO("2222222222","transaction", new BigDecimal(100), "14-01-2025",   accountDTO, transactionTypeDTO);

        when(findTransactionTypeByIdUseCase.getById("typeId"))
                .thenReturn(Mono.just(TransactionTypeMapper.mapToModelFromDTO(transactionTypeDTO)));
        when(validateTransaction.validateTransaction(any(), any()))
                .thenReturn(Mono.just(transactionDTO));
        when(balanceCalculator.calculate(any(), any()))
                .thenReturn(BigDecimal.valueOf(100));
        when(updateAccountUseCase.execute(any()))
                .thenReturn(Mono.just(AccountMapper.mapToResponseFromDTO(accountDTO)));

        Mono<TransactionResponse> responseMono = createTransactionUseCase.execute(command);

        StepVerifier.create(responseMono)
                .expectNextCount(1)
                .verifyComplete();

        verify(findTransactionTypeByIdUseCase).getById("typeId");
        verify(validateTransaction).validateTransaction(any(), any());
        verify(balanceCalculator).calculate(any(), any());
        verify(updateAccountUseCase).execute(any());
    }

    @Test
    public void testExecute_TransactionTypeNotFound() {
        TransactionCommand command = new TransactionCommand(new BigDecimal(100), "14-01-2025", "2222222222", "transaction", "1","1");

        when(findTransactionTypeByIdUseCase.getById("invalidTypeId"))
                .thenReturn(Mono.empty());

        Mono<TransactionResponse> responseMono = createTransactionUseCase.execute(command);

        StepVerifier.create(responseMono)
                .expectError()
                .verify();

        verify(findTransactionTypeByIdUseCase).getById("invalidTypeId");
    }

    @Test
    public void testExecute_TransactionValidationFailed() {
        TransactionCommand command = new TransactionCommand(new BigDecimal(100), "14-01-2025", "2222222222", "transaction", "1","1");

        TransactionTypeDTO transactionTypeDTO = mock(TransactionTypeDTO.class);
        when(findTransactionTypeByIdUseCase.getById("typeId"))
                .thenReturn(Mono.just(TransactionTypeMapper.mapToModelFromDTO(transactionTypeDTO)));

        when(validateTransaction.validateTransaction(any(), any()))
                .thenReturn(Mono.error(new RuntimeException("Validation failed")));

        Mono<TransactionResponse> responseMono = createTransactionUseCase.execute(command);

        StepVerifier.create(responseMono)
                .expectError(RuntimeException.class)
                .verify();

        verify(validateTransaction).validateTransaction(any(), any());
    }

    @Test
    public void testExecute_InvalidBalanceCalculation() {
        TransactionCommand command = new TransactionCommand(new BigDecimal(100), "14-01-2025", "2222222222", "transaction", "1","1");

        TransactionTypeDTO transactionTypeDTO = mock(TransactionTypeDTO.class);
        TransactionDTO transactionDTO = mock(TransactionDTO.class);

        when(findTransactionTypeByIdUseCase.getById("typeId"))
                .thenReturn(Mono.just(TransactionTypeMapper.mapToModelFromDTO(transactionTypeDTO)));
        when(validateTransaction.validateTransaction(any(), any()))
                .thenReturn(Mono.just(transactionDTO));

        when(balanceCalculator.calculate(any(), any()))
                .thenThrow(new IllegalArgumentException("Invalid balance"));

        Mono<TransactionResponse> responseMono = createTransactionUseCase.execute(command);

        StepVerifier.create(responseMono)
                .expectError(IllegalArgumentException.class)
                .verify();
    }

}