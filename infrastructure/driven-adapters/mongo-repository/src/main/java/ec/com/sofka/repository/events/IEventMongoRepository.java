package ec.com.sofka.repository.events;

import ec.com.sofka.data.EventEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface IEventMongoRepository extends ReactiveMongoRepository<EventEntity, String> {
    Flux<EventEntity> findByAggregateId(String aggregateId);

    Flux<EventEntity> findByEventType(String eventType);
}