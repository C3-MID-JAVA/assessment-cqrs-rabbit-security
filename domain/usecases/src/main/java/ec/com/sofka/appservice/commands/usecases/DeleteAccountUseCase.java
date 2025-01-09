package ec.com.sofka.appservice.commands.usecases;

import ec.com.sofka.aggregate.Customer;
import ec.com.sofka.appservice.commands.UpdateAccountCommand;
import ec.com.sofka.appservice.queries.responses.UpdateAccountResponse;
import ec.com.sofka.appservice.gateway.IAccountRepository;
import ec.com.sofka.appservice.gateway.IEventStore;
import ec.com.sofka.appservice.gateway.dto.AccountDTO;
import ec.com.sofka.generics.interfaces.IUseCase;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class DeleteAccountUseCase implements IUseCase<UpdateAccountCommand, UpdateAccountResponse> {

    private final IAccountRepository accountRepository;
    private final IEventStore eventRepository;

    public DeleteAccountUseCase(IAccountRepository accountRepository, IEventStore eventRepository) {
        this.accountRepository = accountRepository;
        this.eventRepository = eventRepository;
    }


    @Override
    public Mono<UpdateAccountResponse> execute(UpdateAccountCommand request) {
        return eventRepository.findAggregate(request.getAggregateId()) // Retorna Flux<DomainEvent>
                .collectList() // Convertir Flux en Mono<List<DomainEvent>>
                .flatMap(events -> {
                    // Reconstruir el aggregate usando el método from
                    Customer customer = Customer.from(request.getAggregateId(), events);

                    // Actualizar los datos del cliente
                    customer.updateAccount(
                            customer.getAccount().getId().getValue(),
                            request.getBalance(),
                            request.getNumber(),
                            request.getCustomerName(),
                            request.getStatus()
                    );

                    // Crear el DTO para la actualización
                    AccountDTO accountDTO = new AccountDTO(
                            customer.getAccount().getId().getValue(),
                            request.getCustomerName(),
                            request.getNumber(),
                            customer.getAccount().getBalance().getValue(),
                            customer.getAccount().getStatus().getValue()
                    );

                    // Actualizar la cuenta en el repositorio y procesar los eventos
                    return accountRepository.update(accountDTO)
                            .flatMap(result ->
                                    Flux.fromIterable(customer.getUncommittedEvents())
                                            .flatMap(eventRepository::save) // Guardar los eventos
                                            .then(Mono.defer(() -> {
                                                customer.markEventsAsCommitted(); // Marcar eventos como comprometidos
                                                return Mono.just(new UpdateAccountResponse(
                                                        request.getAggregateId(),
                                                        result.getAccountId(),
                                                        result.getAccountNumber(),
                                                        result.getName(),
                                                        result.getStatus()
                                                ));
                                            }))
                            );
                })
                .defaultIfEmpty(new UpdateAccountResponse()); // Manejar el caso donde no hay eventos
    }



}
