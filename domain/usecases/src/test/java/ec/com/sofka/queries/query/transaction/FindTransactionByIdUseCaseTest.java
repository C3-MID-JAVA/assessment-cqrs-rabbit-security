package ec.com.sofka.queries.query.transaction;

import ec.com.sofka.aggregates.account.entities.transaction.Transaction;
import ec.com.sofka.aggregates.account.entities.transaction.values.TransactionId;
import ec.com.sofka.aggregates.account.entities.transaction.values.objects.ProcessingDate;
import ec.com.sofka.aggregates.account.entities.transaction.values.objects.TransactionAccount;
import ec.com.sofka.aggregates.account.values.objects.Amount;
import ec.com.sofka.exceptions.RecordNotFoundException;
import ec.com.sofka.gateway.TransactionRepository;
import ec.com.sofka.gateway.busMessage.ErrorBusMessage;
import ec.com.sofka.generics.shared.PropertyQuery;
import ec.com.sofka.generics.shared.QueryResponse;
import ec.com.sofka.mapper.TransactionMapper;
import ec.com.sofka.queries.responses.TransactionResponse;
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

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindTransactionByIdUseCaseTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private FindTransactionByIdUseCase findTransactionByIdUseCase;

    @Mock
    private ErrorBusMessage errorBusMessage;

    private Transaction transaction;
    private PropertyQuery propertyQuery;

    @BeforeEach
    void setUp() {
        transaction = new Transaction(TransactionId.of("1"), TransactionAccount.of("1234567890"), "Retire", Amount.of(new BigDecimal("500.00")), ProcessingDate.of("12-01-2025"), null, null);
        propertyQuery = new PropertyQuery(transaction.getId().getValue());
    }

    @Test
    @DisplayName("When finding transaction with valid ID should return transaction response")
    void findExistingTransactionTest() {
        when(transactionRepository.findById(eq(propertyQuery.getProperty())))
                .thenReturn(Mono.just(TransactionMapper.mapToDTOFromModel(transaction)));

        Mono<QueryResponse<TransactionResponse>> result = findTransactionByIdUseCase.get(propertyQuery);

        StepVerifier.create(result)
                .expectNextMatches(response -> {
                    TransactionResponse transactionResponse = response.getSingleResult()
                            .orElseThrow(() -> new RecordNotFoundException("Transaction not found"));
                    return transactionResponse.getAmount().equals(transaction.getAmount().getValue())
                            && transactionResponse.getDetails().equals(transaction.getDetails())
                            && transactionResponse.getProcessingDate().equals(transaction.getProcessingDate().getValue());
                })
                .verifyComplete();

        verify(transactionRepository).findById(eq(propertyQuery.getProperty()));
        verifyNoInteractions(errorBusMessage);
    }

    @Test
    @DisplayName("When finding non-existing transaction should send error message and throw exception")
    void findNonExistingTransactionTest() {
        when(transactionRepository.findById(propertyQuery.getProperty())).thenReturn(Mono.empty());

        Mono<QueryResponse<TransactionResponse>> result = findTransactionByIdUseCase.get(propertyQuery);

        StepVerifier.create(result)
                .expectError(RecordNotFoundException.class)
                .verify();

        verify(transactionRepository).findById(propertyQuery.getProperty());
        verify(errorBusMessage).sendMsg(argThat(errorMessage ->
                errorMessage.getError().equals("Transaction not found") &&
                        errorMessage.getProcess().equals("Get Transaction by Id")));
    }
}
