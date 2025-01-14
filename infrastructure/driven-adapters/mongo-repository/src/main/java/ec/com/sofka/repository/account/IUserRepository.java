package ec.com.sofka.repository.account;

import ec.com.sofka.data.UserEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface IUserRepository extends ReactiveMongoRepository<UserEntity, String> {

    Mono<UserEntity> findByEmail(String email);

}
