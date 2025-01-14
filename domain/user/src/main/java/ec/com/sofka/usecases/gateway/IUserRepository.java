package ec.com.sofka.usecases.gateway;

import ec.com.sofka.User;
import reactor.core.publisher.Mono;

public interface IUserRepository {
    Mono<UserDTO> save(UserDTO user);
    Mono<UserDTO> findByUsername(String username);
}
