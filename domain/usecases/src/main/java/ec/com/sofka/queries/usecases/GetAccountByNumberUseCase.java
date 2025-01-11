/*package ec.com.sofka.queries.usecases;

import ec.com.sofka.aggregate.Customer;
import ec.com.sofka.gateway.AccountRepository;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.gateway.dto.AccountDTO;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.generics.interfaces.IUseCaseGet;
import ec.com.sofka.generics.utils.QueryResponse;
import ec.com.sofka.queries.query.GetAccountQuery;
import ec.com.sofka.queries.responses.GetAccountResponse;

import java.util.List;

public class GetAccountByNumberUseCase implements IUseCaseGet<GetAccountQuery, GetAccountResponse> {
    private final AccountRepository accountRepository;
    private final IEventStore eventRepository;

    public GetAccountByNumberUseCase(AccountRepository accountRepository, IEventStore eventRepository) {
        this.accountRepository = accountRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public QueryResponse<GetAccountResponse> get(GetAccountQuery request) {
        //Get events related to the aggregateId on the request
        List<DomainEvent> events = eventRepository.findAggregate(request.getAggregateId());

        //Rebuild the aggregate
        Customer customer = Customer.from(request.getAggregateId(),events);
        System.out.println("numero de cuenta -----"+ customer.getAccount().getNumber().getValue());

        //Get the account from the repository
        try {
            //AccountDTO result = accountRepository.findByNumber(customer.getAccount().getNumber().getValue());
           // System.out.println("Data devuelta de la account -----"+ result);
            //Return the response
            return QueryResponse.ofSingle(new GetAccountResponse(
                    customer.getId().getValue(),
                    customer.getAccount().getId().getValue(),
                    customer.getAccount().getNumber().getValue(),
                    customer.getAccount().getName().getValue(),
                    customer.getAccount().getBalance().getValue(),
                    customer.getAccount().getStatus().getValue()
            ));

        } catch (Exception e) {
            throw new RuntimeException("error "+e.getMessage());
        }
    }
}
*/
package ec.com.sofka.queries.usecases;

import ec.com.sofka.aggregate.Customer;
import ec.com.sofka.gateway.AccountRepository;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.generics.interfaces.IUseCaseGet;
import ec.com.sofka.generics.utils.QueryResponse;
import ec.com.sofka.queries.query.GetAccountQuery;
import ec.com.sofka.queries.responses.GetAccountResponse;
import reactor.core.publisher.Mono;

public class GetAccountByNumberUseCase implements IUseCaseGet<GetAccountQuery, GetAccountResponse> {
    private final AccountRepository accountRepository;
    private final IEventStore eventRepository;

    public GetAccountByNumberUseCase(AccountRepository accountRepository, IEventStore eventRepository) {
        this.accountRepository = accountRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public Mono<QueryResponse<GetAccountResponse>> get(GetAccountQuery request) {
        // Get events related to the aggregateId on the request
        return eventRepository.findAggregate(request.getAggregateId())
                .collectList()
                .flatMap(events -> {
                    // Rebuild the aggregate
                    Customer customer = Customer.from(request.getAggregateId(), events);
                    System.out.println("numero de cuenta -----" + customer.getAccount().getNumber().getValue());

                    // Get the account from the repository
                    return Mono.fromCallable(() -> {
                        // AccountDTO result = accountRepository.findByNumber(customer.getAccount().getNumber().getValue());
                        // System.out.println("Data devuelta de la account -----" + result);
                        // Return the response
                        return QueryResponse.ofSingle(new GetAccountResponse(
                                customer.getId().getValue(),
                                customer.getAccount().getId().getValue(),
                                customer.getAccount().getNumber().getValue(),
                                customer.getAccount().getName().getValue(),
                                customer.getAccount().getBalance().getValue(),
                                customer.getAccount().getStatus().getValue()
                        ));
                    }).onErrorMap(e -> new RuntimeException("error " + e.getMessage()));
                });
    }
}
