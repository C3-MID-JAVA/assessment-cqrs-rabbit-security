package ec.com.sofka.database.account;

import ec.com.sofka.data.AccountEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface IAccountMongoRepository extends ReactiveMongoRepository<AccountEntity, String> {
    Mono<AccountEntity> findByAccountNumber(String accountNumber);
}
