package ec.com.sofka.database.account;

import ec.com.sofka.data.AccountEntity;
import ec.com.sofka.data.UserEntity;
import ec.com.sofka.database.TestMongoConfig;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataMongoTest
@AutoConfigureDataMongo
@ContextConfiguration(classes = TestMongoConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class IUserMongoRepositoryTest {
    @Autowired
    private  IUserMongoRepository userRepository;


    @BeforeEach
    void setUp() {
        userRepository.deleteAll().subscribe();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll().subscribe();
    }

    @Test
    @DisplayName("Should found an user on the database with the same username")
    void findByUsername() {
        UserEntity userEntity  = new UserEntity();
        userEntity.setUsername("usernameTest");
        Mono<UserEntity> userSaved = userRepository.save(userEntity);

        StepVerifier.create(userSaved)
                .expectNext(userEntity)
                .verifyComplete();


        Mono<UserEntity> userFound = userRepository.findByUsername(userEntity.getUsername());

        StepVerifier.create(userFound)
                .expectNextMatches(user -> user.getUsername().equals("usernameTest"))
                .verifyComplete();


    }

    @Test
    @DisplayName("Should NOT found an user on the database with the same username")
    void findByUsername_error() {
        Mono<UserEntity> userFound = userRepository.findByUsername("usernameError");

        StepVerifier.create(userFound)
                .expectNextCount(0)
                .verifyComplete();
    }
}