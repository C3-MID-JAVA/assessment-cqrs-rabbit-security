package ec.com.sofka.account;

import ec.com.sofka.account.request.GetAllByUserIdRequest;
import ec.com.sofka.account.responses.AccountResponse;
import ec.com.sofka.aggregate.customer.Customer;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.generics.interfaces.IUseCase;
import reactor.core.publisher.Flux;

public class GetAllByUserIdUseCase implements IUseCase<GetAllByUserIdRequest, AccountResponse> {
    private final IEventStore repository;

    public GetAllByUserIdUseCase(IEventStore repository) {
        this.repository = repository;
    }

    @Override
    public Flux<AccountResponse> execute(GetAllByUserIdRequest request) {
        Flux<DomainEvent> events = repository.findAggregate(request.getAggregateId(), "customer");
        return Customer.from(request.getAggregateId(), events)
                .flatMapMany(customer ->
                        Flux.fromIterable(customer.getAccounts())
                                .map(account -> new AccountResponse(
                                        customer.getId().getValue(),
                                        account.getAccountNumber().getValue(),
                                        account.getBalance().getValue(),
                                        account.getUserId().getValue()
                                ))
                );
    }
}
