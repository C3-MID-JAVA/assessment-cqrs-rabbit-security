package ec.com.sofka.database.account;

import ec.com.sofka.data.TransactionEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface IMongoTransactionRepository extends ReactiveMongoRepository<TransactionEntity, String> {
    Flux<TransactionEntity> findAllByAccountId(String accountId);
}
