package ec.com.sofka.commands.usecases.customer;

import ec.com.sofka.aggregates.account.AccountAggregate;
import ec.com.sofka.exceptions.ConflictException;
import ec.com.sofka.gateway.busMessage.ErrorBusMessage;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.gateway.CustomerRepository;
import ec.com.sofka.gateway.busMessage.EventBusMessage;
import ec.com.sofka.generics.interfaces.IUseCaseExecute;
import ec.com.sofka.mapper.CustomerMapper;
import ec.com.sofka.model.ErrorMessage;
import ec.com.sofka.commands.CustomerCommand;
import ec.com.sofka.queries.responses.CustomerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CreateCustomerUseCase implements IUseCaseExecute<CustomerCommand, CustomerResponse> {
    private final IEventStore repository;
    private final CustomerRepository customerRepository;
    private final ErrorBusMessage errorBusMessage;
    private final EventBusMessage eventBusMessage;

    public CreateCustomerUseCase(IEventStore repository, CustomerRepository customerRepository, ErrorBusMessage errorBusMessage, EventBusMessage eventBusMessage) {
        this.repository = repository;
        this.customerRepository = customerRepository;
        this.errorBusMessage = errorBusMessage;
        this.eventBusMessage = eventBusMessage;
    }

    public Mono<CustomerResponse> execute(CustomerCommand customerCommand) {
        AccountAggregate accountAggregate = new AccountAggregate();

        return customerRepository.findByIdentityCard(customerCommand.getIdentityCard())
                .flatMap(userFound -> {
                    errorBusMessage.sendMsg(new ErrorMessage("Customer is already registered (" + customerCommand.getIdentityCard() + ")",
                            "Create User"));
                    return Mono.<CustomerResponse>error(new ConflictException("The customer is already registered."));
                })
                .switchIfEmpty(Mono.defer(() -> {
                    accountAggregate.createCustomer(
                            customerCommand.getFirstName(),
                            customerCommand.getLastName(),
                            customerCommand.getIdentityCard(),
                            customerCommand.getStatus()
                    );

                    return Flux.fromIterable(accountAggregate.getUncommittedEvents())
                            .flatMap(repository::save)
                            .doOnNext(eventBusMessage::sendEvent)
                            .then(Mono.fromCallable(() -> {
                                accountAggregate.markEventsAsCommitted();
                                return CustomerMapper.mapToResponseFromModel(accountAggregate.getCustomer());
                            }));
                }));
    }

}