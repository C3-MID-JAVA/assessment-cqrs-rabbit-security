package ec.com.sofka.database.events;

import ec.com.sofka.data.EventEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public interface IEventMongoRepository extends ReactiveMongoRepository<EventEntity, String> {
    Flux<EventEntity> findAllByAggregateId(String aggregateId);
    Flux<EventEntity> findAllByAggregateRootName(String aggregateRootName);
}
