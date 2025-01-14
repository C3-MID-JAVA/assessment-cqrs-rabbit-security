package ec.com.sofka.repository.account;

import ec.com.sofka.data.TransactionTypeEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ITransactionTypeRepository extends ReactiveMongoRepository<TransactionTypeEntity, String> {

    Mono<TransactionTypeEntity> findById(String transactionTypeId);

    Mono<TransactionTypeEntity> findByType(String transactionTypeId);

}