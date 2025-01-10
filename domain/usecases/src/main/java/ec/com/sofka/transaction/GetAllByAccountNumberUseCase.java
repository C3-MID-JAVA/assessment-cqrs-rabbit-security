package ec.com.sofka.transaction;

import ec.com.sofka.aggregate.customer.Customer;
import ec.com.sofka.aggregate.operation.Operation;
import ec.com.sofka.exception.NotFoundException;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.generics.interfaces.IUseCase;
import ec.com.sofka.transaction.request.GetAllByAccountNumberRequest;
import ec.com.sofka.transaction.responses.TransactionResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.stream.Collectors;

public class GetAllByAccountNumberUseCase implements IUseCase<GetAllByAccountNumberRequest, TransactionResponse> {

    private final IEventStore repository;

    public GetAllByAccountNumberUseCase(IEventStore repository) {
        this.repository = repository;
    }

    @Override
    public Flux<TransactionResponse> execute(GetAllByAccountNumberRequest cmd) {
        Flux<DomainEvent> eventsCustomer = repository.findAggregate(cmd.getCustomerId(), "customer");

        return Customer.from(cmd.getCustomerId(), eventsCustomer)
                .flatMapMany(customer -> Mono.justOrEmpty(
                                        customer.getAccounts().stream()
                                                .filter(account -> account.getAccountNumber().getValue().equals(cmd.getAccountNumber()))
                                                .findFirst()
                                )
                                .switchIfEmpty(Mono.error(new NotFoundException("Account not found")))
                                .flatMapMany(account -> {
                                    return repository.findAllAggregate("operation")
                                            .collectList()
                                            .flatMapMany(eventsOperation -> {
                                                Map<String, DomainEvent> latestEvents = eventsOperation.stream()
                                                        .collect(Collectors.toMap(
                                                                DomainEvent::getAggregateRootId,
                                                                event -> event,
                                                                (existing, replacement) -> existing.getVersion() >= replacement.getVersion() ? existing : replacement
                                                        ));

                                                return Flux.fromIterable(latestEvents.values())
                                                        .flatMap(event -> Operation.from(event.getAggregateRootId(), Flux.fromIterable(eventsOperation)));
                                            })
                                            .map(operation -> new TransactionResponse(
                                                    operation.getId().getValue(),
                                                    operation.getTransaction().getAmount().getValue(),
                                                    operation.getTransaction().getFee().getValue(),
                                                    operation.getTransaction().getNetAmount().getValue(),
                                                    operation.getTransaction().getType().getValue(),
                                                    operation.getTransaction().getTimestamp().getValue(),
                                                    operation.getTransaction().getAccountId().getValue(),
                                                    customer.getId().getValue()
                                            ));
                                })
                );
    }
}