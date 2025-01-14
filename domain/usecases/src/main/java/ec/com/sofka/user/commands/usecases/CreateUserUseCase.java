package ec.com.sofka.user.commands.usecases;

import ec.com.sofka.ConflictException;
import ec.com.sofka.aggregate.customer.Customer;
import ec.com.sofka.gateway.BusEvent;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.generics.interfaces.IUseCaseExecute;
import ec.com.sofka.user.commands.CreateUserCommand;
import ec.com.sofka.user.queries.query.GetUserByDocumentQuery;
import ec.com.sofka.user.queries.responses.UserResponse;
import ec.com.sofka.user.queries.usecases.GetUserByDocumentViewUseCase;
import reactor.core.publisher.Mono;

public class CreateUserUseCase implements IUseCaseExecute<CreateUserCommand, UserResponse> {
    private final IEventStore repository;
    private final BusEvent busEvent;
    private final GetUserByDocumentViewUseCase getUserByDocumentViewUseCase;


    public CreateUserUseCase(IEventStore repository, BusEvent busEvent, GetUserByDocumentViewUseCase getUserByDocumentViewUseCase) {
        this.repository = repository;
        this.busEvent = busEvent;
        this.getUserByDocumentViewUseCase = getUserByDocumentViewUseCase;
    }

    @Override
    public Mono<UserResponse> execute(CreateUserCommand cmd) {
        return getUserByDocumentViewUseCase.get(new GetUserByDocumentQuery(cmd.getDocumentId()))
                .flatMap(existingUser -> Mono.<UserResponse>error(new ConflictException("User with document ID already exists")))
                .switchIfEmpty(Mono.defer(() -> {
                    Customer customer = new Customer();

                    customer.createUser(cmd.getName(), cmd.getDocumentId());

                    customer.getUncommittedEvents()
                            .stream()
                            .map(repository::save)
                            .forEach(busEvent::sendEventUserCreated);

                    customer.markEventsAsCommitted();

                    return Mono.just(new UserResponse(
                            customer.getUser().getId().getValue(),
                            customer.getUser().getName().getValue(),
                            customer.getUser().getDocumentId().getValue(),
                            customer.getId().getValue()
                    ));
                }));
    }
}
