package ec.com.sofka.usecases;

import ec.com.sofka.gateway.AuthenticationRepository;
import ec.com.sofka.gateway.JwtService;
import ec.com.sofka.gateway.PasswordHasher;
import ec.com.sofka.usecases.commands.LoginAuthenticationCommand;
import ec.com.sofka.usecases.queries.AuthenticationResponse;
import reactor.core.publisher.Mono;

import java.nio.file.AccessDeniedException;

public class LoginAuthenticationUseCase {
    private final AuthenticationRepository authenticationRepository;
    private final PasswordHasher passwordHasher;
    private final JwtService jwtService;

    public LoginAuthenticationUseCase(AuthenticationRepository authenticationRepository, PasswordHasher passwordHasher, JwtService jwtService) {
        this.authenticationRepository = authenticationRepository;
        this.passwordHasher = passwordHasher;
        this.jwtService = jwtService;
    }

    public Mono<AuthenticationResponse> execute(LoginAuthenticationCommand loginAuthenticationCommand){
        return authenticationRepository.findByEmail(loginAuthenticationCommand.getEmail())
                .switchIfEmpty(Mono.error(new RuntimeException("Admin not found")))
                .map(userDTO -> {
                    if (!passwordHasher.verifyPassword(loginAuthenticationCommand.getPassword(), userDTO.getPassword())) {
                        try {
                            throw new AccessDeniedException("Bad credentials");
                        } catch (AccessDeniedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    String token = jwtService.generateToken(userDTO.getEmail());
                    return new AuthenticationResponse(userDTO.getId(), token);
                });
    }
}
