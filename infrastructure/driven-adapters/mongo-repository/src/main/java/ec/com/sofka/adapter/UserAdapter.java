package ec.com.sofka.adapter;

import ec.com.sofka.appservice.gateway.IUserRepository;
import ec.com.sofka.appservice.gateway.dto.UserDTO;
import ec.com.sofka.database.account.IUserMongoRepository;
import ec.com.sofka.mapper.UserMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class UserAdapter implements IUserRepository {

    private final IUserMongoRepository userRepository;

    public UserAdapter(IUserMongoRepository userRepository) {
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
