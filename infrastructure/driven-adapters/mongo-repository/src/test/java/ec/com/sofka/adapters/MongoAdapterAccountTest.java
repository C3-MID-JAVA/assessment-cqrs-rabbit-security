package ec.com.sofka.adapters;

import ec.com.sofka.AccountMongoAdapter;
import ec.com.sofka.data.AccountEntity;
import ec.com.sofka.database.account.IMongoRepository;
import ec.com.sofka.gateway.dto.AccountDTO;
import ec.com.sofka.mapper.AccountMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class MongoAdapterAccountTest {

    @Mock
    private IMongoRepository repository;

    @InjectMocks
    private AccountMongoAdapter accountMongoAdapter;

    private AccountDTO accountDTO;
    private AccountEntity accountEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        accountDTO = new AccountDTO("1", "Test Account", "123456789", new BigDecimal("1000.0"), "active", "userId");
        accountEntity = AccountMapper.toEntity(accountDTO);
    }

    @Test
    void testFindAllSuccess() {
        when(repository.findAll()).thenReturn(Flux.just(accountEntity));

        Flux<AccountDTO> result = accountMongoAdapter.findAll();

        StepVerifier.create(result)
                .expectNextMatches(account -> account.getAccountNumber().equals("123456789"))
                .verifyComplete();
    }

    @Test
    void testFindByIdSuccess() {
        when(repository.findById("1")).thenReturn(Mono.just(accountEntity));

        Mono<AccountDTO> result = accountMongoAdapter.findById("1");

        StepVerifier.create(result)
                .expectNextMatches(account -> account.getId().equals("1"))
                .verifyComplete();
    }

    @Test
    void testFindByIdNotFound() {
        when(repository.findById("1")).thenReturn(Mono.empty());

        Mono<AccountDTO> result = accountMongoAdapter.findById("1");

        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void testFindByNumberSuccess() {
        when(repository.findByAccountNumber("123456789")).thenReturn(Mono.just(accountEntity));

        Mono<AccountDTO> result = accountMongoAdapter.findByNumber("123456789");

        StepVerifier.create(result)
                .expectNextMatches(account -> account.getAccountNumber().equals("123456789"))
                .verifyComplete();
    }

    @Test
    void testFindByNumberNotFound() {
        when(repository.findByAccountNumber("123456789")).thenReturn(Mono.empty());

        Mono<AccountDTO> result = accountMongoAdapter.findByNumber("123456789");

        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void testSaveAccountSuccess() {
        when(repository.save(any(AccountEntity.class))).thenReturn(Mono.just(accountEntity));

        Mono<AccountDTO> result = accountMongoAdapter.save(accountDTO);

        StepVerifier.create(result)
                .expectNextMatches(savedAccount -> savedAccount.getAccountNumber().equals("123456789"))
                .verifyComplete();
    }

    @Test
    void testSaveAccountFailure() {
        when(repository.save(any(AccountEntity.class))).thenReturn(Mono.error(new RuntimeException("Save failed")));

        Mono<AccountDTO> result = accountMongoAdapter.save(accountDTO);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException && throwable.getMessage().equals("Save failed"))
                .verify();
    }

    @Test
    void testUpdateAccountSuccess() {
        when(repository.findById("1")).thenReturn(Mono.just(accountEntity));
        when(repository.save(any(AccountEntity.class))).thenReturn(Mono.just(accountEntity));

        Mono<AccountDTO> result = accountMongoAdapter.update(accountDTO);

        StepVerifier.create(result)
                .expectNextMatches(updatedAccount -> updatedAccount.getAccountNumber().equals("123456789"))
                .verifyComplete();
    }

    @Test
    void testUpdateAccountNotFound() {
        when(repository.findById("1")).thenReturn(Mono.empty());

        Mono<AccountDTO> result = accountMongoAdapter.update(accountDTO);

        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void testDeleteAccountSuccess() {
        when(repository.findById("1")).thenReturn(Mono.just(accountEntity));
        when(repository.delete(any(AccountEntity.class))).thenReturn(Mono.empty());

        Mono<AccountDTO> result = accountMongoAdapter.delete(accountDTO);

        StepVerifier.create(result)
                .expectNextMatches(deletedAccount -> deletedAccount.getAccountNumber().equals("123456789"))
                .verifyComplete();
    }

    @Test
    void testDeleteAccountNotFound() {
        when(repository.findById("1")).thenReturn(Mono.empty());

        Mono<AccountDTO> result = accountMongoAdapter.delete(accountDTO);

        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }
}