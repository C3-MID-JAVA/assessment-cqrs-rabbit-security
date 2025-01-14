package ec.com.sofka.adapters;

import ec.com.sofka.UserDTO;
import ec.com.sofka.UserMongoAdapter;

import ec.com.sofka.data.UserEntity;
import ec.com.sofka.database.account.IUserMongoRepository;
import ec.com.sofka.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class MongoAdapterUserTest {

    @Mock
    private IUserMongoRepository repository;

    @InjectMocks
    private UserMongoAdapter userMongoAdapter;

    private UserDTO userDTO;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userDTO = new UserDTO("test@example.com", "Password1!");
        userEntity = UserMapper.toEntity(userDTO);
    }

    @Test
    void testSaveUserSuccess() {
        when(repository.save(any(UserEntity.class))).thenReturn(Mono.just(userEntity));

        Mono<UserDTO> result = userMongoAdapter.save(userDTO);

        StepVerifier.create(result)
                .expectNextMatches(savedUser -> savedUser.getEmail().equals("test@example.com"))
                .verifyComplete();
    }

    @Test
    void testSaveUserFailure() {
        when(repository.save(any(UserEntity.class))).thenReturn(Mono.error(new RuntimeException("Save failed")));

        Mono<UserDTO> result = userMongoAdapter.save(userDTO);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException && throwable.getMessage().equals("Save failed"))
                .verify();
    }

    @Test
    void testFindByEmailSuccess() {
        when(repository.findByEmail("test@example.com")).thenReturn(Mono.just(userEntity));

        Mono<UserDTO> result = userMongoAdapter.findByEmail("test@example.com");

        StepVerifier.create(result)
                .expectNextMatches(foundUser -> foundUser.getEmail().equals("test@example.com"))
                .verifyComplete();
    }

    @Test
    void testFindByEmailNotFound() {
        when(repository.findByEmail("test@example.com")).thenReturn(Mono.empty());

        Mono<UserDTO> result = userMongoAdapter.findByEmail("test@example.com");

        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }
}