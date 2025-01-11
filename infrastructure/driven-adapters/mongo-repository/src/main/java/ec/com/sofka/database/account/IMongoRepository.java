package ec.com.sofka.database.account;

import ec.com.sofka.data.AccountEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface IMongoRepository extends ReactiveMongoRepository<AccountEntity, String> {
    Mono<AccountEntity> findByAccountNumber(String number);
   // Optional<AccountEntity> findById(String accountId);

}
