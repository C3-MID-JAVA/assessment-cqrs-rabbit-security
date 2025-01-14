package ec.com.sofka.database.bank;

import ec.com.sofka.data.AdminEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface AdminMongoRepository extends ReactiveMongoRepository<AdminEntity, String> {
    Mono<AdminEntity> findByEmail(String email);
}
