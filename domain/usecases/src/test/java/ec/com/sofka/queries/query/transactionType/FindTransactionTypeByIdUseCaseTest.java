package ec.com.sofka.queries.query.transactionType;

import ec.com.sofka.aggregates.account.entities.transactionType.TransactionType;
import ec.com.sofka.aggregates.account.entities.transactionType.values.TransactionTypeId;
import ec.com.sofka.aggregates.account.values.objects.Amount;
import ec.com.sofka.exceptions.RecordNotFoundException;
import ec.com.sofka.gateway.TransactionTypeRepository;
import ec.com.sofka.gateway.busMessage.ErrorBusMessage;
import ec.com.sofka.generics.shared.PropertyQuery;
import ec.com.sofka.generics.shared.QueryResponse;
import ec.com.sofka.mapper.TransactionTypeMapper;
import ec.com.sofka.queries.query.transcationType.FindTransactionTypeByIdUseCase;
import ec.com.sofka.queries.responses.TransactionTypeResponse;
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

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindTransactionTypeByIdUseCaseTest {

    @Mock
    private TransactionTypeRepository transactionTypeRepository;

    @InjectMocks
    private FindTransactionTypeByIdUseCase findTransactionTypeByIdUseCase;

    @Mock
    private ErrorBusMessage errorBusMessage;

    private TransactionType transactionType;
    private PropertyQuery propertyQuery;

    @BeforeEach
    void setUp() {
        transactionType = new TransactionType(TransactionTypeId.of("1"), "Deposit", "Deposit transaction type", Amount.of(new BigDecimal(10)), true, true, StatusEnum.ACTIVE);
        propertyQuery = new PropertyQuery(transactionType.getId().getValue());
    }

    @Test
    @DisplayName("When finding transaction type with valid ID should return transaction type response")
    void findExistingTransactionTypeTest() {
        when(transactionTypeRepository.findById(eq(propertyQuery.getProperty())))
                .thenReturn(Mono.just(TransactionTypeMapper.mapToDTOFromModel(transactionType)));

        Mono<QueryResponse<TransactionTypeResponse>> result = findTransactionTypeByIdUseCase.get(propertyQuery);

        StepVerifier.create(result)
                .expectNextMatches(response -> {
                    TransactionTypeResponse transactionTypeResponse = response.getSingleResult()
                            .orElseThrow(() -> new RecordNotFoundException("Transaction Type not found"));
                    return transactionTypeResponse.getTransactionCost().equals(transactionType.getTransactionCost())
                            && transactionTypeResponse.getType().equals(transactionType.getType())
                            && transactionTypeResponse.getDescription().equals(transactionType.getDescription());
                })
                .verifyComplete();

        verify(transactionTypeRepository).findById(eq(propertyQuery.getProperty()));
        verifyNoInteractions(errorBusMessage);
    }

    @Test
    @DisplayName("When finding non-existing transaction type should send error message and throw exception")
    void findNonExistingTransactionTypeTest() {
        when(transactionTypeRepository.findById(propertyQuery.getProperty())).thenReturn(Mono.empty());

        Mono<QueryResponse<TransactionTypeResponse>> result = findTransactionTypeByIdUseCase.get(propertyQuery);

        StepVerifier.create(result)
                .expectError(RecordNotFoundException.class)
                .verify();

        verify(transactionTypeRepository).findById(propertyQuery.getProperty());
        verify(errorBusMessage).sendMsg(argThat(errorMessage ->
                errorMessage.getError().equals("Transaction Type not found") &&
                        errorMessage.getProcess().equals("Get TransactionType by Id")));
    }
}
