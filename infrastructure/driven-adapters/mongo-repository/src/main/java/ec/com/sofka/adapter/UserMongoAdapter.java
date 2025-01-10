package ec.com.sofka.adapter;

import ec.com.sofka.data.UserEntity;
import ec.com.sofka.database.bank.UserMongoRepository;
import ec.com.sofka.gateway.UserRepository;
import ec.com.sofka.gateway.dto.UserDTO;
import ec.com.sofka.mapper.UserMapperEntity;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class UserMongoAdapter implements UserRepository {
    private final UserMongoRepository repository;

    public UserMongoAdapter(UserMongoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<UserDTO> save(UserDTO user) {
        UserEntity userEntity = UserMapperEntity.toEntity(user);
        return repository.save(userEntity).map(UserMapperEntity::fromEntity);
    }

    @Override
    public Flux<UserDTO> getAll() {
        return repository.findAll().map(UserMapperEntity::fromEntity);
    }

    @Override
    public Mono<UserDTO> findByDocumentId(String documentId) {
        return repository.findByDocumentId(documentId).map(UserMapperEntity::fromEntity);
    }
}
