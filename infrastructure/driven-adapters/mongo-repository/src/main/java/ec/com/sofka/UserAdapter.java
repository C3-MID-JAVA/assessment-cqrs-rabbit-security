package ec.com.sofka;

import ec.com.sofka.repository.account.IUserRepository;
import ec.com.sofka.gateway.UserRepository;
import ec.com.sofka.gateway.dto.UserDTO;
import ec.com.sofka.mapper.UserEntityMapper;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class UserAdapter implements UserRepository {
    private final IUserRepository userRepository;
    private final ReactiveMongoTemplate accountReactiveMongoTemplate;

    public UserAdapter(IUserRepository userRepository, ReactiveMongoTemplate accountReactiveMongoTemplate) {
        this.userRepository = userRepository;
        this.accountReactiveMongoTemplate = accountReactiveMongoTemplate;
    }

    @Override
    public Mono<UserDTO> save(UserDTO userDTO) {
        return userRepository.save(UserEntityMapper.mapToEntity(userDTO))
                .map(UserEntityMapper::mapToDTO);
    }

    @Override
    public Mono<UserDTO> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserEntityMapper::mapToDTO);
    }

}