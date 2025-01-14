package ec.com.sofka.adapters;

import ec.com.sofka.database.account.IUserMongoRepository;
import ec.com.sofka.mapper.UserMapper;
import ec.com.sofka.usecases.gateway.IUserRepository;
import ec.com.sofka.usecases.gateway.UserDTO;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class UserMongoAdapter implements IUserRepository {
    private final IUserMongoRepository userRepository;

    public UserMongoAdapter(IUserMongoRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<UserDTO> save(UserDTO user) {
        return userRepository.save(UserMapper.toUser(user)).map(UserMapper::toUserDTO);
    }

    @Override
    public Mono<UserDTO> findByUsername(String username) {
        return userRepository.findByUsername(username).map(UserMapper::toUserDTO);
    }

}
