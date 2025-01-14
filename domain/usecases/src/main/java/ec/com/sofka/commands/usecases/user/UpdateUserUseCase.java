package ec.com.sofka.commands.usecases.user;

import ec.com.sofka.aggregates.auth.AuthAggregate;
import ec.com.sofka.commands.UserCommand;
import ec.com.sofka.exceptions.ConflictException;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.gateway.UserRepository;
import ec.com.sofka.gateway.busMessage.ErrorBusMessage;
import ec.com.sofka.gateway.busMessage.EventBusMessage;
import ec.com.sofka.generics.interfaces.IUseCaseExecute;
import ec.com.sofka.model.ErrorMessage;
import ec.com.sofka.queries.responses.UserResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class UpdateUserUseCase implements IUseCaseExecute<UserCommand, UserResponse> {
    private final IEventStore repository;
    private final UserRepository userRepository;
    private final ErrorBusMessage errorBusMessage;
    private final EventBusMessage eventBusMessage;
    private final PasswordEncoder passwordEncoder;

    public UpdateUserUseCase(IEventStore repository, UserRepository userRepository, ErrorBusMessage errorBusMessage, EventBusMessage eventBusMessage, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.errorBusMessage = errorBusMessage;
        this.eventBusMessage = eventBusMessage;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Mono<UserResponse> execute(UserCommand userCommand) {
        AuthAggregate authAggregate = new AuthAggregate();

        return userRepository.findByEmail(userCommand.getEmail())
                .flatMap(userFound -> {
                    errorBusMessage.sendMsg(new ErrorMessage("Email is already registered (" + userCommand.getEmail() + ")",
                            "Create User"));
                    return Mono.<UserResponse>error(new ConflictException("The user is already registered."));
                })
                .switchIfEmpty(Mono.defer(() -> {
                    authAggregate.updateUser(
                            userCommand.getAggregateId(),
                            userCommand.getFirstName(),
                            userCommand.getLastName(),
                            userCommand.getEmail(),
                            passwordEncoder.encode(userCommand.getPassword()),
                            userCommand.getRole()
                    );

                    return Flux.fromIterable(authAggregate.getUncommittedEvents())
                            .flatMap(repository::save)
                            .doOnNext(eventBusMessage::sendEvent)
                            .then(Mono.fromCallable(() -> {
                                authAggregate.markEventsAsCommitted();
                                return new UserResponse(userCommand.getFirstName(), userCommand.getLastName(), userCommand.getEmail(), userCommand.getRole());
                            }));
                }));
    }
}
