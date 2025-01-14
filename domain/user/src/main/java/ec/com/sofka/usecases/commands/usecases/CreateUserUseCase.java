package ec.com.sofka.usecases.commands.usecases;

import ec.com.sofka.User;
import ec.com.sofka.generics.interfaces.IUseCaseExecute;
import ec.com.sofka.usecases.commands.CreateUserCommand;
import ec.com.sofka.usecases.gateway.IUserRepository;
import ec.com.sofka.usecases.gateway.UserDTO;
import ec.com.sofka.usecases.queries.responses.CreateUserResponse;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

public class CreateUserUseCase implements IUseCaseExecute<CreateUserCommand, CreateUserResponse> {
    private final IUserRepository userRepository;


    public CreateUserUseCase(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<CreateUserResponse> execute(CreateUserCommand request) {
        return userRepository.findByUsername(request.getUsername())
                .hasElement()
                .flatMap(element -> {
                    if (element) {
                        return Mono.error(new RuntimeException("User already exists"));
                    }
                    return userRepository.save(
                                    new UserDTO(null,
                                            request.getUsername(),
                                            request.getPassword(),
                                            request.getRoles()))
                            .map(u -> new CreateUserResponse(
                                    u.getUsername(),
                                    u.getPassword(),
                                    u.getRoles()));
                });
    }
}
