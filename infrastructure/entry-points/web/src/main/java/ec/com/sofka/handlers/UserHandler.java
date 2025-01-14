/*package ec.com.sofka.handlers;

import ec.com.sofka.data.RequestDTO;
import ec.com.sofka.data.ResponseDTO;
import ec.com.sofka.data.user.AuthResponseDTO;
import ec.com.sofka.data.user.UserRequestDTO;
import ec.com.sofka.data.user.UserResponseDTO;
import ec.com.sofka.usecases.LoginAuthenticationUseCase;
import ec.com.sofka.usecases.RegisterAuthenticationUseCase;
import ec.com.sofka.usecases.commands.LoginAuthenticationCommand;
import ec.com.sofka.usecases.commands.RegisterAuthenticationCommand;
import ec.com.sofka.validator.RequestValidator;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class UserHandler {
    private final RegisterAuthenticationUseCase registerAuthenticationUseCase;
    private final LoginAuthenticationUseCase loginAuthenticationUseCase;
    private final RequestValidator requestValidator;


    public UserHandler(RegisterAuthenticationUseCase registerAuthenticationUseCase, LoginAuthenticationUseCase loginAuthenticationUseCase, RequestValidator requestValidator) {
        this.registerAuthenticationUseCase = registerAuthenticationUseCase;
        this.loginAuthenticationUseCase = loginAuthenticationUseCase;
        this.requestValidator = requestValidator;
    }

    public Mono<UserResponseDTO> create (UserRequestDTO requestDTO){
        return registerAuthenticationUseCase.execute(
                new RegisterAuthenticationCommand(
                        requestDTO.getEmail(),
                        requestDTO.getPassword()
                )).map( response -> new UserResponseDTO(
                    response.getEmail()
        ));
    }

    public Mono<AuthResponseDTO> login(UserRequestDTO requestDTO){
        return loginAuthenticationUseCase.execute( new LoginAuthenticationCommand(
                requestDTO.getEmail(),
                requestDTO.getPassword()
        )).map(response -> new AuthResponseDTO(
                response.getToken()
        ));
    }
}
*/

package ec.com.sofka.handlers;

import ec.com.sofka.data.user.AuthResponseDTO;
import ec.com.sofka.data.user.UserRequestDTO;
import ec.com.sofka.data.user.UserResponseDTO;
import ec.com.sofka.usecases.LoginAuthenticationUseCase;
import ec.com.sofka.usecases.RegisterAuthenticationUseCase;
import ec.com.sofka.usecases.commands.LoginAuthenticationCommand;
import ec.com.sofka.usecases.commands.RegisterAuthenticationCommand;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Component
public class UserHandler {
    private final RegisterAuthenticationUseCase registerAuthenticationUseCase;
    private final LoginAuthenticationUseCase loginAuthenticationUseCase;

    public UserHandler(RegisterAuthenticationUseCase registerAuthenticationUseCase, LoginAuthenticationUseCase loginAuthenticationUseCase) {
        this.registerAuthenticationUseCase = registerAuthenticationUseCase;
        this.loginAuthenticationUseCase = loginAuthenticationUseCase;
    }

    public Mono<UserResponseDTO> create(UserRequestDTO requestDTO) {
        if (requestDTO.getEmail() == null || requestDTO.getPassword() == null) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request"));
        }
        return registerAuthenticationUseCase.execute(
                new RegisterAuthenticationCommand(
                        requestDTO.getEmail(),
                        requestDTO.getPassword()
                )).map(response -> new UserResponseDTO(response.getEmail()));
    }

    public Mono<AuthResponseDTO> login(UserRequestDTO requestDTO) {
        if (requestDTO.getEmail() == null || requestDTO.getPassword() == null) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request"));
        }
        return loginAuthenticationUseCase.execute(
                new LoginAuthenticationCommand(
                        requestDTO.getEmail(),
                        requestDTO.getPassword()
                )).map(response -> new AuthResponseDTO(response.getToken()));
    }
}
