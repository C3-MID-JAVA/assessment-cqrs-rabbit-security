package ec.com.sofka.commands.usecases;

import ec.com.sofka.aggregate.Customer;
import ec.com.sofka.aggregate.Operation;
import ec.com.sofka.commands.CreateTransactionCommand;
import ec.com.sofka.gateway.BusEvent;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.gateway.dto.AccountDTO;
import ec.com.sofka.gateway.dto.TransactionDTO;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.generics.interfaces.IUseCaseExecute;
import ec.com.sofka.queries.responses.CreateAccountResponse;
import ec.com.sofka.queries.responses.CreateTransactionResponse;
import ec.com.sofka.queries.responses.UpdateAccountResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

public class CreateTransactionUseCase  implements IUseCaseExecute<CreateTransactionCommand, Mono<CreateTransactionResponse>> {
    private final IEventStore repository;
    private final BusEvent busEvent;

    public CreateTransactionUseCase(IEventStore repository, BusEvent busEvent) {
        this.repository = repository;
        this.busEvent = busEvent;
    }

    @Override
    public Mono<CreateTransactionResponse> execute(CreateTransactionCommand request) {
        return null;
    }

    public Mono<CreateTransactionResponse> validarTransaction2(AccountDTO cuenta, BigDecimal monto, String tipo, BigDecimal costo, boolean  esRetiro, String agregateId){
        if (esRetiro && cuenta.getBalance().compareTo(monto.add(costo)) < 0) {
            throw new RuntimeException("Saldo insuficiente para realizar esta transacción");
        }

        actualizarSaldo(cuenta, monto, costo, tipo, esRetiro);
//Primero encointramos el evento con el agregadoId de la cuenta
      return  repository.findAggregate(agregateId)
                .collectList()
                .flatMap(events -> {
                    Customer customer = Customer.from(agregateId, events);
                      //Actualizamos la cuenta en el customer.
                    customer.updateAccount(
                            customer.getAccount().getId().getValue(),
                            cuenta.getBalance(),
                            cuenta.getAccountNumber(),
                            cuenta.getName(),
                            cuenta.getStatus(),
                            cuenta.getIdUser());

                    return Flux.fromIterable(customer.getUncommittedEvents())
                            .flatMap(repository::save)
                            .doOnNext(busEvent::sendEvent)
                            .then(Mono.fromCallable(() -> {
                                customer.markEventsAsCommitted();
                                ///
                                Operation operation = new Operation();
                                operation.createTransaction(monto, tipo,costo,cuenta.getId(),cuenta.getStatus());

                                return Flux.fromIterable(operation.getUncommittedEvents())
                                        .flatMap(repository::save)
                                        .doOnNext(busEvent::sendEvent)
                                        .then(Mono.fromCallable(() -> {
                                            operation.markEventsAsCommitted();
                                            return new CreateTransactionResponse(
                                                    operation.getId().getValue(),
                                                    operation.getTransaction().getId().getValue(),
                                                    operation.getTransaction().getAmount().getValue(),
                                                    operation.getTransaction().getType().getValue(),
                                                    operation.getTransaction().getCost().getValue(),
                                                    operation.getTransaction().getIdAccount().getValue(),
                                                    operation.getTransaction().getStatus().getValue()
                                            );
                                        }));
                            }).flatMap(mono -> mono));
                });
    }

    private void actualizarSaldo(AccountDTO cuenta, BigDecimal monto, BigDecimal costo, String tipo, boolean esRetiro) {
        if (esRetiro) {
            cuenta.setBalance(cuenta.getBalance().subtract(monto.add(costo)));
        } else {
            cuenta.setBalance(cuenta.getBalance().add(monto).subtract(costo));
        }
    }
}



