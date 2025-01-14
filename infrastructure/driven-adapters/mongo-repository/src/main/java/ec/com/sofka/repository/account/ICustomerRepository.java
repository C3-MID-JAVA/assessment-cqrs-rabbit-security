package ec.com.sofka.repository.account;

import ec.com.sofka.data.CustomerEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICustomerRepository extends ReactiveMongoRepository<CustomerEntity, String> {

    Flux<CustomerEntity> findAll();

    Mono<CustomerEntity> findByIdentityCard(String identityCard);

    Mono<CustomerEntity> findById(String id);
}