package ec.com.sofka.appservice.commands.usecases;

import ec.com.sofka.appservice.commands.CreateUserCommand;
import ec.com.sofka.appservice.gateway.IUserRepository;
import ec.com.sofka.appservice.gateway.dto.UserDTO;
import ec.com.sofka.appservice.queries.responses.CreateUserResponse;
import ec.com.sofka.generics.interfaces.IUseCase;
import reactor.core.publisher.Mono;

public class CreateUserUseCase implements IUseCase<CreateUserCommand, CreateUserResponse> {
    private final IUserRepository userRepository;


    public CreateUserUseCase(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<CreateUserResponse> execute(CreateUserCommand request) {
        return userRepository.save(
                        new UserDTO(null,
                                request.getUsername(),
                                request.getPassword(),
                                request.getRoles()))
                .map(userDTO -> new CreateUserResponse(
                        userDTO.getUsername(),
                        userDTO.getPassword(),
                        userDTO.getRoles()));

    }
}