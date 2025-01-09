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

public class UpdateAccountUseCase implements IUseCase<UpdateAccountCommand, UpdateAccountResponse> {

    private final IAccountRepository accountRepository;
    private final IEventStore eventRepository;

    public UpdateAccountUseCase(IAccountRepository accountRepository, IEventStore eventRepository) {
        this.accountRepository = accountRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public Mono<UpdateAccountResponse> execute(UpdateAccountCommand request) {
        return eventRepository.findAggregate(request.getAggregateId()) // Obtener eventos como Flux<DomainEvent>
                .collectList() // Agrupar los eventos en una lista
                .flatMap(events -> {
                    // Reconstruir el aggregate usando el método from
                    Customer customer = Customer.from(request.getAggregateId(), events);

                    // Actualizar datos en el cliente
                    customer.updateAccount(
                            customer.getAccount().getId().getValue(),
                            request.getBalance(),
                            request.getNumber(),
                            request.getCustomerName(),
                            request.getStatus()
                    );

                    // Crear DTO para la actualización de la cuenta
                    AccountDTO accountDTO = new AccountDTO(
                            customer.getAccount().getId().getValue(),
                            request.getCustomerName(),
                            request.getNumber(),
                            customer.getAccount().getBalance().getValue(),
                            customer.getAccount().getStatus().getValue()
                    );

                    // Actualizar la cuenta en el repositorio de manera reactiva
                    return accountRepository.update(accountDTO)
                            .flatMap(result -> {
                                // Guardar los eventos no comprometidos de forma reactiva
                                return Flux.fromIterable(customer.getUncommittedEvents())
                                        .flatMap(eventRepository::save)
                                        .then(Mono.defer(() -> {
                                            // Marcar eventos como comprometidos
                                            customer.markEventsAsCommitted();

                                            // Crear respuesta después de la actualización
                                            return Mono.just(new UpdateAccountResponse(
                                                    request.getAggregateId(),
                                                    result.getAccountId(),
                                                    result.getAccountNumber(),
                                                    result.getName(),
                                                    result.getStatus(),
                                                    result.getBalance()
                                            ));
                                        }));
                            });
                });
    }



}
