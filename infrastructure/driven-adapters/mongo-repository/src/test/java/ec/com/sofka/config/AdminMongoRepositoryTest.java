package ec.com.sofka.config;

import ec.com.sofka.ROLE;
import ec.com.sofka.TestMongoConfig;
import ec.com.sofka.data.AdminEntity;
import ec.com.sofka.database.bank.AdminMongoRepository;
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
public class AdminMongoRepositoryTest {

    @Autowired
    private AdminMongoRepository adminRepository;

    private AdminEntity admin1;
    private AdminEntity admin2;

    @BeforeAll
    void setup() {
        admin1 = new AdminEntity("admin1@example.com", "password1", ROLE.GOD);
        admin2 = new AdminEntity("admin2@example.com", "password2", ROLE.GOD);
    }

    @BeforeEach
    void init() {
        adminRepository.deleteAll().block();
        adminRepository.saveAll(Flux.just(admin1, admin2)).blockLast();
    }

    @Test
    void findByEmail_shouldReturnAdmin_whenAdminExists() {
        StepVerifier.create(adminRepository.findByEmail("admin1@example.com"))
                .expectNextMatches(admin -> admin.getEmail().equals("admin1@example.com")
                        && admin.getPassword().equals("password1"))
                .verifyComplete();
    }

    @Test
    void findByEmail_shouldReturnEmpty_whenAdminDoesNotExist() {
        StepVerifier.create(adminRepository.findByEmail("nonexistent@example.com"))
                .verifyComplete();
    }

    @Test
    void save_shouldPersistAdmin() {
        AdminEntity newAdmin = new AdminEntity("admin3@example.com", "password3", ROLE.GOD);

        StepVerifier.create(adminRepository.save(newAdmin))
                .expectNextMatches(admin -> admin.getEmail().equals("admin3@example.com"))
                .verifyComplete();

        StepVerifier.create(adminRepository.findByEmail("admin3@example.com"))
                .expectNextMatches(admin -> admin.getPassword().equals("password3"))
                .verifyComplete();
    }

    @Test
    void delete_shouldRemoveAdmin() {
        StepVerifier.create(adminRepository.deleteById(admin1.getId()))
                .verifyComplete();

        StepVerifier.create(adminRepository.findByEmail("admin1@example.com"))
                .verifyComplete();
    }

    @Test
    void delete_shouldNotRemoveOtherAdmin() {
        StepVerifier.create(adminRepository.deleteById(admin1.getId()))
                .verifyComplete();

        StepVerifier.create(adminRepository.findByEmail("admin2@example.com"))
                .expectNextMatches(admin -> admin.getEmail().equals("admin2@example.com"))
                .verifyComplete();
    }
}
