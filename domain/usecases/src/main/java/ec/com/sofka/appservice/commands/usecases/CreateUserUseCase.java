package ec.com.sofka.appservice.commands.usecases;

import ec.com.sofka.ConflictException;
import ec.com.sofka.appservice.commands.CreateUserCommand;
import ec.com.sofka.appservice.gateway.IUserRepository;
import ec.com.sofka.appservice.gateway.dto.UserDTO;
import ec.com.sofka.appservice.queries.responses.CreateUserResponse;
import ec.com.sofka.enums.ROLE;
import ec.com.sofka.generics.interfaces.IUseCase;
import reactor.core.publisher.Mono;

public class CreateUserUseCase implements IUseCase<CreateUserCommand, CreateUserResponse> {
    private final IUserRepository userRepository;


    public CreateUserUseCase(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<CreateUserResponse> execute(CreateUserCommand request) {

        if (!isValidRole(request.getRoles())) {
            return Mono.error(new IllegalArgumentException("The role is invalid"));
        }

        return userRepository.findByUsername(request.getUsername())
                .flatMap(existingUser -> Mono.<CreateUserResponse>error(new ConflictException("Username already exists")))
        .switchIfEmpty(
                userRepository.save(
                                new UserDTO(null,
                                        request.getUsername(),
                                        request.getPassword(),
                                        request.getRoles()))
                        .map(userDTO -> new CreateUserResponse(
                                userDTO.getUsername(),
                                userDTO.getPassword(),
                                userDTO.getRoles()))
        ) ;
    }

    private boolean isValidRole(String role) {
        try {
            ROLE.valueOf(role);
            return true;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("The role is not valid");
        }
    }

}