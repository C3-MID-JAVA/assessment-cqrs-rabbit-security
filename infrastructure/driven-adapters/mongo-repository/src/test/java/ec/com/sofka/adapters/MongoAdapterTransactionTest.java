package ec.com.sofka.adapters;

import ec.com.sofka.TransactionMongoAdapter;
import ec.com.sofka.data.TransactionEntity;
import ec.com.sofka.database.account.ITransactionRepository;
import ec.com.sofka.gateway.dto.TransactionDTO;
import ec.com.sofka.mapper.TransactionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class MongoAdapterTransactionTest {

    @Mock
    private ITransactionRepository repository;

    @InjectMocks
    private TransactionMongoAdapter transactionMongoAdapter;

    private TransactionDTO transactionDTO;
    private TransactionEntity transactionEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionDTO = new TransactionDTO("1", new BigDecimal("100.0"), "type", new BigDecimal("10.0"), "accountId", "status");
        transactionEntity = TransactionMapper.toEntity(transactionDTO);
    }

    @Test
    void testSaveTransactionSuccess() {
        when(repository.save(any(TransactionEntity.class))).thenReturn(Mono.just(transactionEntity));

        Mono<TransactionDTO> result = transactionMongoAdapter.save(transactionDTO);

        StepVerifier.create(result)
                .expectNextMatches(savedTransaction -> savedTransaction.getId().equals("1"))
                .verifyComplete();
    }

    @Test
    void testSaveTransactionFailure() {
        when(repository.save(any(TransactionEntity.class))).thenReturn(Mono.error(new RuntimeException("Save failed")));

        Mono<TransactionDTO> result = transactionMongoAdapter.save(transactionDTO);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException && throwable.getMessage().equals("Save failed"))
                .verify();
    }
}