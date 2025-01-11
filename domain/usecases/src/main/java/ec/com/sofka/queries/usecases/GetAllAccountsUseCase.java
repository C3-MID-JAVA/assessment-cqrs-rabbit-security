/*package ec.com.sofka.queries.usecases;

import ec.com.sofka.aggregate.Customer;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.generics.interfaces.IUseCaseGet;
import ec.com.sofka.generics.utils.QueryResponse;
import ec.com.sofka.queries.query.GetAccountQuery;
import ec.com.sofka.queries.responses.GetAccountResponse;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GetAllAccountsUseCase implements IUseCaseGet<GetAccountQuery, GetAccountResponse> {

    private final IEventStore eventRepository;

    public GetAllAccountsUseCase(IEventStore eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public QueryResponse<GetAccountResponse> get(GetAccountQuery query) {
        //Get all events
        List<DomainEvent> events = eventRepository.findAllAggregates();

        //Get the last events
        Map<String, DomainEvent> maplatestEvents = events.stream()
                .collect(Collectors.toMap(
                        DomainEvent::getAggregateRootId,   // Key: aggregateId
                        event -> event,                // Value: the event itself
                        (existing, replacement) -> existing.getVersion() >= replacement.getVersion() ? existing : replacement // Keep the latest version
                ));

        //Keep the events
        List<DomainEvent> latestEvents = maplatestEvents.values().stream().toList();

        //------ Then I can rebuild all the customers
        List<Customer> customers = latestEvents.stream()
                .map(event -> Customer.from(event.getAggregateRootId(),latestEvents))
                .toList();

        //------ Finally I can return the response
        return QueryResponse.ofMultiple(
                customers.stream()
                .map(customer -> new GetAccountResponse(
                                customer.getId().getValue(),
                                customer.getAccount().getId().getValue(),
                                customer.getAccount().getNumber().getValue(),
                                customer.getAccount().getName().getValue(),
                                customer.getAccount().getBalance().getValue(),
                                customer.getAccount().getStatus().getValue()
                        )
                ).collect(Collectors.toList())
        );
    }

}
*/
package ec.com.sofka.queries.usecases;

import ec.com.sofka.aggregate.Customer;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.generics.interfaces.IUseCaseGet;
import ec.com.sofka.generics.utils.QueryResponse;
import ec.com.sofka.queries.query.GetAccountQuery;
import ec.com.sofka.queries.responses.GetAccountResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.stream.Collectors;

public class GetAllAccountsUseCase implements IUseCaseGet<GetAccountQuery, GetAccountResponse> {

    private final IEventStore eventRepository;

    public GetAllAccountsUseCase(IEventStore eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Mono<QueryResponse<GetAccountResponse>> get(GetAccountQuery query) {
        // Get all events
        return eventRepository.findAllAggregates()
                .collectList()
                .flatMap(events -> {
                    // Get the last events
                    Map<String, DomainEvent> mapLatestEvents = events.stream()
                            .collect(Collectors.toMap(
                                    DomainEvent::getAggregateRootId,   // Key: aggregateId
                                    event -> event,                // Value: the event itself
                                    (existing, replacement) -> existing.getVersion() >= replacement.getVersion() ? existing : replacement // Keep the latest version
                            ));

                    // Keep the events
                    var latestEvents = mapLatestEvents.values().stream().toList();

                    // Rebuild all the customers
                    var customers = latestEvents.stream()
                            .map(event -> Customer.from(event.getAggregateRootId(), latestEvents))
                            .toList();

                    // Return the response
                    var responses = customers.stream()
                            .map(customer -> new GetAccountResponse(
                                    customer.getId().getValue(),
                                    customer.getAccount().getId().getValue(),
                                    customer.getAccount().getNumber().getValue(),
                                    customer.getAccount().getName().getValue(),
                                    customer.getAccount().getBalance().getValue(),
                                    customer.getAccount().getStatus().getValue()
                            ))
                            .collect(Collectors.toList());

                    return Mono.just(QueryResponse.ofMultiple(responses));
                });
    }
}

