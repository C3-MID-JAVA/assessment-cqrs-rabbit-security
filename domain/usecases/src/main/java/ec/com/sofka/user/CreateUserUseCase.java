package ec.com.sofka.user;

import ec.com.sofka.aggregate.customer.Customer;
import ec.com.sofka.exception.ConflictException;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.gateway.UserRepository;
import ec.com.sofka.gateway.dto.UserDTO;
import ec.com.sofka.generics.interfaces.IUseCase;
import ec.com.sofka.user.request.CreateUserRequest;
import ec.com.sofka.user.responses.UserResponse;
import reactor.core.publisher.Flux;
import ec.com.sofka.gateway.BusEvent;
import reactor.core.publisher.Mono;

public class CreateUserUseCase implements IUseCase<CreateUserRequest, UserResponse> {
    private final IEventStore repository;
    private final UserRepository userRepository;
    private final BusEvent busEvent;

    public CreateUserUseCase(
            IEventStore repository,
            UserRepository userRepository,
            BusEvent busEvent
    ) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.busEvent = busEvent;
    }

    @Override
    public Mono<UserResponse> execute(CreateUserRequest cmd) {
        return userRepository.findByDocumentId(cmd.getDocumentId())
                .flatMap(existingUser -> Mono.error(new ConflictException("User with document ID already exists")))
                .then(Mono.defer(() -> {

                    Customer customer = new Customer();
                    customer.createUser(cmd.getName(), cmd.getDocumentId());

                    return userRepository.save(new UserDTO(
                                    customer.getUser().getId().getValue(),
                                    customer.getUser().getName().getValue(),
                                    customer.getUser().getDocumentId().getValue()
                            ))
                            .flatMap(savedUser -> {
                                // Guardar eventos no confirmados
                                return Flux.fromIterable(customer.getUncommittedEvents())
                                        .flatMap(event -> repository.save(event)
                                                .doOnSuccess(savedEvent -> busEvent.sendEventUserCreated(Mono.just(savedEvent))))
                                        .then(Mono.just(savedUser));
                            })
                            .doOnTerminate(customer::markEventsAsCommitted)
                            .thenReturn(new UserResponse(
                                    customer.getId().getValue(),
                                    customer.getUser().getName().getValue(),
                                    customer.getUser().getDocumentId().getValue()
                            ));
                }));
    }
}
