package ec.com.sofka.database.account;

import ec.com.sofka.data.CardEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICardMongoRepository extends ReactiveMongoRepository<CardEntity, String> {
    Mono<Boolean> existsByCardCVV(String cardCVV);
    Mono<CardEntity> findByCardNumber(String cardNumber);
    Flux<CardEntity> findByAccount_Id(String accountId);

}
