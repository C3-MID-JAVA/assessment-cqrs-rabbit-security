package ec.com.sofka.user;

import ec.com.sofka.aggregate.customer.Customer;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.generics.interfaces.IUseEmptyCase;
import ec.com.sofka.user.responses.UserResponse;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.stream.Collectors;

public class GetAllUserUseCase implements IUseEmptyCase<UserResponse> {
    private final IEventStore repository;

    public GetAllUserUseCase(IEventStore repository) {
        this.repository = repository;
    }

    @Override
    public Flux<UserResponse> execute() {
        return repository.findAllAggregate("customer")
                .collectList()
                .flatMapMany(events -> {
                    Map<String, DomainEvent> latestEvents = events.stream()
                            .collect(Collectors.toMap(
                                    DomainEvent::getAggregateRootId,
                                    event -> event,
                                    (existing, replacement) -> existing.getVersion() >= replacement.getVersion() ? existing : replacement
                            ));

                    return Flux.fromIterable(latestEvents.values())
                            .flatMap(event -> Customer.from(event.getAggregateRootId(), Flux.fromIterable(events)));
                })
                .map(customer -> new UserResponse(
                        customer.getId().getValue(),
                        customer.getUser().getName().getValue(),
                        customer.getUser().getDocumentId().getValue())
                );
    }
}
