package ec.com.sofka.appservice.gateway;

import ec.com.sofka.appservice.gateway.dto.UserDTO;
import reactor.core.publisher.Mono;

public interface IUserRepository {
    Mono<UserDTO> save(UserDTO user);
    Mono<UserDTO> findByUsername(String username);
}
