package ec.com.sofka.queries.query.transaction;

import ec.com.sofka.aggregates.account.entities.transaction.Transaction;
import ec.com.sofka.aggregates.account.entities.transaction.values.TransactionId;
import ec.com.sofka.aggregates.account.entities.transaction.values.objects.ProcessingDate;
import ec.com.sofka.aggregates.account.entities.transaction.values.objects.TransactionAccount;
import ec.com.sofka.aggregates.account.values.objects.Amount;
import ec.com.sofka.gateway.TransactionRepository;
import ec.com.sofka.mapper.TransactionMapper;
import ec.com.sofka.queries.responses.TransactionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Date;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetAllTransactionsUseCaseTest {
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private GetAllTransactionsUseCase getAllTransactionsUseCase ;

    private Transaction transaction;

    @BeforeEach
    void setUp() {
        transaction = new Transaction(TransactionId.of("u1"), TransactionAccount.of("1234567890"),"transaction made in Manabí.", Amount.of(new BigDecimal(1000)), ProcessingDate.of(new Date().toString()), null, null);
    }

    @Test
    @DisplayName("Should get accounts when records exist")
    public void testGetAllTransactions_WhenTransactionsExist() {
        when(transactionRepository.findAll())
                .thenReturn(Flux.just(TransactionMapper.mapToDTOFromModel(transaction)));


        Flux<TransactionResponse> result = getAllTransactionsUseCase.get()
                .flatMap(queryResponse -> Flux.fromIterable(queryResponse.getMultipleResults()));

        StepVerifier.create(result)
                .expectNextCount(1)
                .verifyComplete();

        verify(transactionRepository).findAll();
    }

    @Test
    @DisplayName("Should no transactions found when records not exist")
    public void testGetAllTransactionTypes_WhenNoTransactionTypesExist() {
        when(transactionRepository.findAll()).thenReturn(Flux.empty());

        Flux<Transaction> result = getAllTransactionsUseCase.getAll();

        StepVerifier.create(result)
                .expectNext()
                .verifyComplete();

        verify(transactionRepository).findAll();
    }

}