package ec.com.sofka.transaction.queries;


import ec.com.sofka.NotFoundException;
import ec.com.sofka.gateway.AccountRepository;
import ec.com.sofka.gateway.TransactionRepository;
import ec.com.sofka.gateway.dto.AccountDTO;
import ec.com.sofka.gateway.dto.TransactionDTO;
import ec.com.sofka.transaction.TransactionType;
import ec.com.sofka.transaction.queries.query.GetAllByAccountNumberQuery;
import ec.com.sofka.transaction.queries.usecases.GetAllByAccountNumberViewUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllByAccountNumberUseCaseTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private GetAllByAccountNumberViewUseCase useCase;

    @Test
    void shouldThrowNotFoundExceptionWhenAccountNotFound() {
        String accountNumber = "12345678";

        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Mono.empty());

        GetAllByAccountNumberQuery request = new GetAllByAccountNumberQuery(accountNumber);

        StepVerifier.create(useCase.get(request))
                .expectErrorMatches(throwable ->
                        throwable instanceof NotFoundException &&
                                throwable.getMessage().equals("Account not found"))
                .verify();

        verify(accountRepository, times(1)).findByAccountNumber(accountNumber);
    }

    @Test
    void shouldReturnTransactionsSuccessfully() {
        String accountNumber = "12345678";
        String operationId = "operation-1";
        String userId = "user-1";

        AccountDTO accountCreated = new AccountDTO(
                "account1",
                accountNumber,
                new BigDecimal("1500.00"),
                userId
        );

        TransactionDTO transactionCreated = new TransactionDTO(
                operationId,
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(90),
                TransactionType.BRANCH_DEPOSIT,
                LocalDateTime.now(),
                accountNumber
        );

        when(accountRepository.findByAccountNumber(anyString())).thenReturn(Mono.just(accountCreated));
        when(transactionRepository.getAllByAccountId(anyString())).thenReturn(Flux.just(transactionCreated));

        GetAllByAccountNumberQuery request = new GetAllByAccountNumberQuery(accountNumber);

        StepVerifier.create(useCase.get(request))
                .expectNextMatches(response ->
                        response.getMultipleResults().get(0).getOperationId().equals(operationId) &&
                                response.getMultipleResults().get(0).getAmount().equals(BigDecimal.valueOf(100)) &&
                                response.getMultipleResults().get(0).getFee().equals(BigDecimal.valueOf(10)) &&
                                response.getMultipleResults().get(0).getFinalAmount().equals(BigDecimal.valueOf(90)) &&
                                response.getMultipleResults().get(0).getType() == TransactionType.BRANCH_DEPOSIT &&
                                response.getMultipleResults().get(0).getAccountId().equals(accountNumber)
                )
                .verifyComplete();

        verify(accountRepository, times(1)).findByAccountNumber(anyString());
        verify(transactionRepository, times(1)).getAllByAccountId(anyString());
    }

    @Test
    void shouldReturnEmptyWhenNoTransactionsExist() {
        String accountNumber = "12345678";
        String userId = "user-1";

        AccountDTO accountCreated = new AccountDTO(
                "account1",
                accountNumber,
                new BigDecimal("1500.00"),
                userId
        );

        when(accountRepository.findByAccountNumber(anyString())).thenReturn(Mono.just(accountCreated));
        when(transactionRepository.getAllByAccountId(anyString())).thenReturn(Flux.empty());

        GetAllByAccountNumberQuery request = new GetAllByAccountNumberQuery(accountNumber);

        StepVerifier.create(useCase.get(request))
                .expectNextMatches(Objects::nonNull)
                .verifyComplete();

        verify(accountRepository, times(1)).findByAccountNumber(anyString());
        verify(transactionRepository, times(1)).getAllByAccountId(anyString());
    }

}