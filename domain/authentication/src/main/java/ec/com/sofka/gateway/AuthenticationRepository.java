package ec.com.sofka.gateway;

import ec.com.sofka.UserDTO;
import reactor.core.publisher.Mono;

public interface AuthenticationRepository {
    Mono<UserDTO> save(UserDTO userDTO);
    Mono<UserDTO> findByEmail(String email);
}
