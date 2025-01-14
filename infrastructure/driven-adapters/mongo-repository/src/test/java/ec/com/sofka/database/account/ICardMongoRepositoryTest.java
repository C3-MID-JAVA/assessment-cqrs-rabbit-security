package ec.com.sofka.database.account;


import ec.com.sofka.data.AccountEntity;
import ec.com.sofka.data.CardEntity;
import ec.com.sofka.database.TestMongoConfig;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import reactor.test.StepVerifier;

@ActiveProfiles("test")
@DataMongoTest
@AutoConfigureDataMongo
@ContextConfiguration(classes = TestMongoConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ICardMongoRepositoryTest {

    @Autowired
    private ICardMongoRepository cardMongoRepository;

    @BeforeEach
    void setUp() {
        cardMongoRepository.deleteAll().block();
    }

    @AfterEach
    void tearDown() {
        cardMongoRepository.deleteAll().block();
    }

    @Test
    @DisplayName("Should return FALSE when CardCvv does not exist")
    void existsByCardCVV() {
        CardEntity card = new CardEntity();
        card.setCardNumber("123456789");

        StepVerifier.create( cardMongoRepository.save(card))
                .expectNext(card)
                .verifyComplete();
        StepVerifier.create(cardMongoRepository.existsByCardCVV("1234"))
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should return TRUE when CardCvv already exist")
    void existsByCardCVV_Existing() {
        CardEntity card = new CardEntity();
        card.setCardNumber("123456789");
        card.setCardCVV("1234");

        StepVerifier.create( cardMongoRepository.save(card))
                .expectNext(card)
                .verifyComplete();
        StepVerifier.create(cardMongoRepository.existsByCardCVV("1234"))
                .expectNext(true)
                .verifyComplete();

    }

    @Test
    @DisplayName("Should found a Card on the database with the same card number")
    void findByCardNumber() {
        CardEntity card = new CardEntity();
        card.setCardNumber("123456789");

        StepVerifier.create( cardMongoRepository.save(card))
                .expectNext(card)
                .verifyComplete();

        StepVerifier.create(cardMongoRepository.findByCardNumber(card.getCardNumber()))
                .expectNextMatches(cc->cc.getCardNumber().equals(card.getCardNumber()))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should found all the Cards on the database with the same account id")
    void findByAccount_Id() {
        AccountEntity account = new AccountEntity();
        account.setAccountNumber("123456789");
        account.setId("6762467aee327545bebedebb");
        CardEntity card = new CardEntity();
        CardEntity card2 = new CardEntity();
        CardEntity card3 = new CardEntity();
        card.setCardNumber("123456789");
        card2.setCardNumber("987654321");
        card3.setCardNumber("147258369");
        card.setAccount(account);
        card2.setAccount(account);
        card3.setAccount(account);
        StepVerifier.create(cardMongoRepository.save(card))
                .expectNext(card)
                .verifyComplete();
        StepVerifier.create(cardMongoRepository.save(card2))
                .expectNext(card2)
                .verifyComplete();
        StepVerifier.create(cardMongoRepository.save(card3))
                .expectNext(card3)
                .verifyComplete();

        StepVerifier.create(cardMongoRepository.findByAccount_Id(account.getId()))
                .expectNextMatches(cc->cc.getCardNumber().equals(card.getCardNumber()))
                .expectNextMatches(cc->cc.getCardNumber().equals(card2.getCardNumber()))
                .expectNextMatches(cc->cc.getCardNumber().equals(card3.getCardNumber()))
                .verifyComplete();
    }
}