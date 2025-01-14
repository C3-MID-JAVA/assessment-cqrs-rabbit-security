package ec.com.sofka.repository.account;

import ec.com.sofka.data.AccountEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface IAccountRepository extends ReactiveMongoRepository<AccountEntity, String> {

    Mono<AccountEntity> findByAccountNumber(String number);

}