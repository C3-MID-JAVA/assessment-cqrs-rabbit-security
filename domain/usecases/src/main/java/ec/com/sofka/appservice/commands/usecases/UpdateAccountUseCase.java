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

    private final IAccountRepository accountRepository;
    private final IEventStore eventRepository;
    private final IBusEvent busEvent;

    public UpdateAccountUseCase(IAccountRepository accountRepository, IEventStore eventRepository, IBusEvent busEvent) {
        this.accountRepository = accountRepository;
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
                                customer.updateAccount(
                                        customer.getAccount().getId().getValue(),
                                        request.getBalance(),
                                        request.getNumber(),
                                        request.getCustomerName(),
                                        request.getStatus()
                                );

                                AccountDTO accountDTO = new AccountDTO(
                                        customer.getAccount().getId().getValue(),
                                        request.getCustomerName(),
                                        request.getNumber(),
                                        customer.getAccount().getBalance().getValue(),
                                        customer.getAccount().getStatus().getValue()
                                );

                                return accountRepository.update(accountDTO)
                                        .flatMap(result -> {
                                            // Guardar los eventos no comprometidos de forma reactiva
                                            return Flux.fromIterable(customer.getUncommittedEvents())
                                                    .flatMap(eventRepository::save)
                                                    .doOnNext(updateEvents ->busEvent.sendEventAccountUpdated(Mono.just(updateEvents)))
                                                    .then(Mono.defer(() -> {
                                                        customer.markEventsAsCommitted();

                                                        return Mono.just(new UpdateAccountResponse(
                                                                request.getAggregateId(),
                                                                result.getAccountNumber(),
                                                                result.getName(),
                                                                result.getStatus(),
                                                                result.getBalance()
                                                        ));
                                                    }));
                                        });
                            });
                })
                .defaultIfEmpty(new UpdateAccountResponse());
    }




}
