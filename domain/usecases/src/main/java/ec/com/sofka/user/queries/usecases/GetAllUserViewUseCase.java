package ec.com.sofka.user.queries.usecases;

import ec.com.sofka.gateway.UserRepository;
import ec.com.sofka.generics.interfaces.IUseCaseEmptyGet;
import ec.com.sofka.generics.utils.QueryResponse;
import ec.com.sofka.user.queries.responses.UserResponse;
import reactor.core.publisher.Mono;


public class GetAllUserViewUseCase implements IUseCaseEmptyGet<UserResponse> {
    private final UserRepository userRepository;

    public GetAllUserViewUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<QueryResponse<UserResponse>> get() {
        return userRepository.getAll()
                .map(userDTO -> new UserResponse(
                        userDTO.getId(),
                        userDTO.getName(),
                        userDTO.getDocumentId())
                )
                .collectList()
                .flatMap(users -> Mono.just(QueryResponse.ofMultiple(users)));
    }
}
