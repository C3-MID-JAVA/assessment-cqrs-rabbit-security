/*package ec.com.sofka.commands.usecases;

import ec.com.sofka.aggregate.Customer;
import ec.com.sofka.gateway.AccountRepository;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.gateway.dto.AccountDTO;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.generics.interfaces.IUseCaseExecute;
import ec.com.sofka.commands.UpdateAccountCommand;
import ec.com.sofka.gateway.BusEvent;
import ec.com.sofka.queries.responses.UpdateAccountResponse;

import java.util.List;

public class UpdateAccountUseCase implements IUseCaseExecute<UpdateAccountCommand, UpdateAccountResponse> {
    //private final AccountRepository accountRepository;
    private final IEventStore eventRepository;
    private final BusEvent busEvent;

    public UpdateAccountUseCase(IEventStore eventRepository, BusEvent busEvent) {

        this.eventRepository = eventRepository;
        this.busEvent = busEvent;
    }

    @Override
    public UpdateAccountResponse execute(UpdateAccountCommand request) {
        //Get events related to the aggregateId on the request
        List<DomainEvent> events = eventRepository.findAggregate(request.getAggregateId());


        //Rebuild the aggregate
        Customer customer = Customer.from(request.getAggregateId(),events);


        customer.updateAccount(
                customer.getAccount().getId().getValue(),
                request.getBalance(),
                request.getNumber(),
                request.getCustomerName(),
                request.getStatus(),
                request.getIdUser());

        customer.getUncommittedEvents()
                .stream()
                .map(eventRepository::save)
                .forEach(busEvent::sendEvent);


        customer.markEventsAsCommitted();

        return new UpdateAccountResponse(
                request.getAggregateId(),
                customer.getAccount().getId().getValue(),
                customer.getAccount().getNumber().getValue(),
                customer.getAccount().getName().getValue(),
                customer.getAccount().getStatus().getValue(),
                customer.getAccount().getUserId().getValue());
    }
}
*/
package ec.com.sofka.commands.usecases;

import ec.com.sofka.aggregate.Customer;
import ec.com.sofka.commands.UpdateAccountCommand;
import ec.com.sofka.gateway.BusEvent;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.generics.interfaces.IUseCaseExecute;
import ec.com.sofka.queries.responses.UpdateAccountResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class UpdateAccountUseCase implements IUseCaseExecute<UpdateAccountCommand, Mono<UpdateAccountResponse>> {
    private final IEventStore eventRepository;
    private final BusEvent busEvent;

    public UpdateAccountUseCase(IEventStore eventRepository, BusEvent busEvent) {
        this.eventRepository = eventRepository;
        this.busEvent = busEvent;
    }

    @Override
    public Mono<UpdateAccountResponse> execute(UpdateAccountCommand request) {
        return eventRepository.findAggregate(request.getAggregateId())
                .collectList()
                .flatMap(events -> {
                    if (events.isEmpty()) {
                        return Mono.error(new RuntimeException("Cuenta no existe en el repositorio"));
                    }
                    Customer customer = Customer.from(request.getAggregateId(), events);

                    customer.updateAccount(
                            customer.getAccount().getId().getValue(),
                            request.getBalance(),
                            request.getNumber(),
                            request.getCustomerName(),
                            request.getStatus(),
                            request.getIdUser());

                    return Flux.fromIterable(customer.getUncommittedEvents())
                            .flatMap(eventRepository::save)
                            .doOnNext(busEvent::sendEvent)
                            .then(Mono.fromCallable(() -> {
                                customer.markEventsAsCommitted();
                                return new UpdateAccountResponse(
                                        request.getAggregateId(),
                                        customer.getAccount().getId().getValue(),
                                        customer.getAccount().getNumber().getValue(),
                                        customer.getAccount().getName().getValue(),
                                        customer.getAccount().getStatus().getValue(),
                                        customer.getAccount().getUserId().getValue());
                            }));
                });
    }
}