package ec.com.sofka.adapters;

import ec.com.sofka.database.account.ICardMongoRepository;
import ec.com.sofka.gateway.ICardRepository;
import ec.com.sofka.gateway.dto.card.CardDTO;
import ec.com.sofka.mapper.CardMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class CardMongoAdapter implements ICardRepository {
    private final ICardMongoRepository cardMongoRepository;
    private final ReactiveMongoTemplate mongoTemplate;

    public CardMongoAdapter(ICardMongoRepository cardMongoRepository, @Qualifier("accountMongoTemplate")  ReactiveMongoTemplate mongoTemplate) {
        this.cardMongoRepository = cardMongoRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Mono<Boolean> existsByCardCVV(String cardCVV) {
        return cardMongoRepository.existsByCardCVV(cardCVV);

    }

    @Override
    public Mono<CardDTO> findByCardNumber(String cardNumber) {
        return cardMongoRepository.findByCardNumber(cardNumber).map(CardMapper::toCardDTO);

    }

    @Override
    public Flux<CardDTO> findByAccount_Id(String accountId) {
        return cardMongoRepository.findByAccount_Id(accountId).map(CardMapper::toCardDTO);

    }

    @Override
    public Mono<CardDTO> save(CardDTO card) {
        System.out.println("Saving card: " + card.getCardNumber() +" "+card.getAccount().getId());
        return cardMongoRepository.save(CardMapper.toCardEntity(card)).map(CardMapper::toCardDTO);
    }
}
