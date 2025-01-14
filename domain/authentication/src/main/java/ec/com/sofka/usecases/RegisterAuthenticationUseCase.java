package ec.com.sofka.usecases;

import ec.com.sofka.UserDTO;
//import ec.com.sofka.exceptions.ConflictException;
import ec.com.sofka.gateway.AuthenticationRepository;
import ec.com.sofka.gateway.JwtService;
import ec.com.sofka.gateway.PasswordHasher;
import ec.com.sofka.usecases.commands.RegisterAuthenticationCommand;
import ec.com.sofka.usecases.queries.AuthenticationResponse;
import ec.com.sofka.usecases.queries.RegisterResponse;
import reactor.core.publisher.Mono;

public class RegisterAuthenticationUseCase {
    private final AuthenticationRepository authenticationRepository;
    private final PasswordHasher passwordHasher;
    private final JwtService jwtService;

    public RegisterAuthenticationUseCase(AuthenticationRepository authenticationRepository, PasswordHasher passwordHasher, JwtService jwtService) {
        this.authenticationRepository = authenticationRepository;
        this.passwordHasher = passwordHasher;
        this.jwtService = jwtService;
    }
    public Mono<RegisterResponse> execute(RegisterAuthenticationCommand registerAdminCommand) {
        try {
            String hashedPassword = passwordHasher.hashPassword(registerAdminCommand.getPassword());
            return authenticationRepository.findByEmail(registerAdminCommand.getEmail())
                    .flatMap(existingAdmin -> Mono.<RegisterResponse>error(new RuntimeException("Admin already exists")))
                    .switchIfEmpty(Mono.defer(() -> {
                                return authenticationRepository.save(new UserDTO(registerAdminCommand.getEmail(), hashedPassword))
                                        .map(savedAdmin -> {
                                            //String token = jwtService.generateToken(savedAdmin.getEmail());
                                            return new RegisterResponse(savedAdmin.getEmail());
                                        });
                            })
                    );

        }catch (RuntimeException e) {
            return Mono.error(e);
        }

    }
}
