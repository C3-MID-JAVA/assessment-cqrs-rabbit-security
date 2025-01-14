package ec.com.sofka.user.queries.usecases;

import ec.com.sofka.gateway.UserRepository;
import ec.com.sofka.gateway.dto.UserDTO;
import ec.com.sofka.user.queries.query.GetUserByDocumentQuery;
import reactor.core.publisher.Mono;

public class GetUserByDocumentViewUseCase {

    private final UserRepository userRepository;

    public GetUserByDocumentViewUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<UserDTO> get(GetUserByDocumentQuery query) {
        return userRepository.findByDocumentId(query.getDocumentId())
                .switchIfEmpty(Mono.empty());
    }
}
