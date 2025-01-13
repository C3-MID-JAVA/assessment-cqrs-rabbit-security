package ec.com.sofka.appservice.commands.usecases;

import ec.com.sofka.aggregate.Customer;
import ec.com.sofka.appservice.commands.UpdateAccountCommand;
import ec.com.sofka.appservice.gateway.IBusEvent;
import ec.com.sofka.appservice.queries.responses.UpdateAccountResponse;
import ec.com.sofka.appservice.gateway.IAccountRepository;
import ec.com.sofka.appservice.gateway.IEventStore;
import ec.com.sofka.appservice.gateway.dto.AccountDTO;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.generics.interfaces.IUseCase;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class UpdateAccountUseCase implements IUseCase<UpdateAccountCommand, UpdateAccountResponse> {

    private final IEventStore eventRepository;
    private final IBusEvent busEvent;

    public UpdateAccountUseCase(IEventStore eventRepository, IBusEvent busEvent) {
        this.eventRepository = eventRepository;
        this.busEvent = busEvent;
    }

    @Override
    public Mono<UpdateAccountResponse> execute(UpdateAccountCommand request) {
        return eventRepository.findAggregate(request.getAggregateId()) // Obtener eventos como Flux<DomainEvent>
                .collectList() // Agrupar los eventos en una lista
                .flatMap(events -> {
                    Flux<DomainEvent> eventFlux = Flux.fromIterable(events);

                    return Customer.from(request.getAggregateId(), eventFlux)
                            .flatMap(customer -> {
                                // Actualizar la cuenta en el agregado
                                customer.updateAccount(
                                        customer.getAccount().getId().getValue(),
                                        request.getBalance(),
                                        request.getNumber(),
                                        request.getCustomerName(),
                                        request.getStatus()
                                );

                                // Guardar los eventos no comprometidos de forma reactiva
                                return Flux.fromIterable(customer.getUncommittedEvents())
                                        .flatMap(eventRepository::save) // Guardar cada evento en el EventStore
                                        .doOnNext(updateEvent -> busEvent.sendEventAccountUpdated(Mono.just(updateEvent))) // Enviar eventos a través de BusEvent
                                        .then(Mono.defer(() -> {
                                            // Marcar los eventos como comprometidos
                                            customer.markEventsAsCommitted();

                                            // Crear y devolver la respuesta
                                            return Mono.just(new UpdateAccountResponse(
                                                    request.getAggregateId(),
                                                    customer.getAccount().getId().getValue(),
                                                    request.getNumber(),
                                                    request.getCustomerName(),
                                                    request.getStatus(),
                                                    request.getBalance()
                                            ));
                                        }));
                            });
                });
    }
}
