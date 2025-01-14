package ec.com.sofka.database.bank;

import ec.com.sofka.data.TransactionEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface TransactionMongoRepository extends ReactiveMongoRepository<TransactionEntity, String> {
    Flux<TransactionEntity> findAllByAccountId(String accountId);
}
