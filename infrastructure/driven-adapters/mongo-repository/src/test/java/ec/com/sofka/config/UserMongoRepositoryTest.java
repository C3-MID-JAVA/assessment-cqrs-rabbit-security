package ec.com.sofka.config;

import ec.com.sofka.TestMongoConfig;
import ec.com.sofka.data.UserEntity;
import ec.com.sofka.database.bank.UserMongoRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;


@ActiveProfiles("test")
@DataMongoTest
@AutoConfigureDataMongo
@ContextConfiguration(classes = TestMongoConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserMongoRepositoryTest {

    @Autowired
    private UserMongoRepository userRepository;

    private UserEntity user1;
    private UserEntity user2;

    @BeforeAll
    void setup() {
        user1 = new UserEntity("1", "John Doe", "123456789");
        user2 = new UserEntity("2", "Jane Smith", "987654321");
    }

    @BeforeEach
    void init() {
        userRepository.deleteAll().block();
        userRepository.saveAll(Flux.just(user1, user2)).blockLast();
    }

    @Test
    void findById_shouldReturnUser_whenUserExists() {
        StepVerifier.create(userRepository.findById("1"))
                .expectNextMatches(user -> user.getName().equals("John Doe") && user.getDocumentId().equals("123456789"))
                .verifyComplete();
    }

    @Test
    void findById_shouldReturnEmpty_whenUserDoesNotExist() {
        StepVerifier.create(userRepository.findById("99"))
                .verifyComplete();
    }

    @Test
    void save_shouldPersistUser() {
        UserEntity newUser = new UserEntity("3", "Alice Brown", "555555555");

        StepVerifier.create(userRepository.save(newUser))
                .expectNextMatches(user -> user.getId().equals("3") && user.getName().equals("Alice Brown"))
                .verifyComplete();

        StepVerifier.create(userRepository.findById("3"))
                .expectNextMatches(user -> user.getDocumentId().equals("555555555"))
                .verifyComplete();
    }

    @Test
    void delete_shouldRemoveUser() {
        StepVerifier.create(userRepository.deleteById("1"))
                .verifyComplete();

        StepVerifier.create(userRepository.findById("1"))
                .verifyComplete();
    }
}