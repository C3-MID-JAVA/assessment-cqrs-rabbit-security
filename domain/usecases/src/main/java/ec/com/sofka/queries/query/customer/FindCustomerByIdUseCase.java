package ec.com.sofka.queries.query.customer;

import ec.com.sofka.aggregates.account.entities.customer.Customer;
import ec.com.sofka.exceptions.RecordNotFoundException;
import ec.com.sofka.gateway.CustomerRepository;
import ec.com.sofka.gateway.busMessage.ErrorBusMessage;
import ec.com.sofka.generics.interfaces.IUseCaseGetElement;
import ec.com.sofka.generics.shared.QueryResponse;
import ec.com.sofka.mapper.CustomerMapper;
import ec.com.sofka.model.ErrorMessage;
import ec.com.sofka.generics.shared.PropertyQuery;
import ec.com.sofka.queries.responses.CustomerResponse;
import reactor.core.publisher.Mono;

public class FindCustomerByIdUseCase implements IUseCaseGetElement<PropertyQuery, CustomerResponse> {
    private final CustomerRepository customerRepository;
    private final ErrorBusMessage errorBusMessage;

    public FindCustomerByIdUseCase(CustomerRepository customerRepository, ErrorBusMessage errorBusMessage) {
        this.customerRepository = customerRepository;
        this.errorBusMessage = errorBusMessage;
    }

    @Override
    public Mono<QueryResponse<CustomerResponse>> get(PropertyQuery request) {
        return getById(request.getProperty())
                .map(CustomerMapper::mapToResponseFromModel)
                .flatMap(customerResponse -> Mono.just(QueryResponse.ofSingle(customerResponse)));
    }

    public Mono<Customer> getById(String id) {
        return customerRepository.findById(id)
                .switchIfEmpty(Mono.defer(() -> {
                    errorBusMessage.sendMsg(new ErrorMessage("User not found", "Get User by Id"));
                    return Mono.error(new RecordNotFoundException("User not found."));
                }))
                .map(CustomerMapper::mapToModelFromDTO);
    }

}