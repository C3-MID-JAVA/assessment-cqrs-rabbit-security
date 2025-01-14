package ec.com.sofka;

import ec.com.sofka.data.UserEntity;
import ec.com.sofka.database.account.IUserMongoRepository;
import ec.com.sofka.gateway.AuthenticationRepository;
import ec.com.sofka.mapper.UserMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class UserMongoAdapter implements AuthenticationRepository {
    private final IUserMongoRepository  repository;

    public UserMongoAdapter(IUserMongoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<UserDTO> save(UserDTO userDTO) {
        UserEntity userEntity= UserMapper.toEntity(userDTO);
        return repository.save(userEntity).map(UserMapper::toDTO);
    }

    @Override
    public Mono<UserDTO> findByEmail(String email) {
        return repository.findByEmail(email).map(UserMapper::toDTO);
    }
}
