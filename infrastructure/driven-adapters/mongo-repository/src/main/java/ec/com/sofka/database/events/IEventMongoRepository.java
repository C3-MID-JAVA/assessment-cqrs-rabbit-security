package ec.com.sofka.database.events;

import ec.com.sofka.data.EventEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface IEventMongoRepository extends ReactiveMongoRepository<EventEntity, String> {
    Flux<EventEntity> findAllByAggregateId(String aggregateId);
    Flux<EventEntity> findAllByAggregateRootName(String aggregateRootName);
    Flux<EventEntity> findAllByAggregateRootNameAndEventType(String aggregateRootName, String eventType);
}
