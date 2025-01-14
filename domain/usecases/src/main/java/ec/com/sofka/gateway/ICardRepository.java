package ec.com.sofka.gateway;

import ec.com.sofka.gateway.dto.card.CardDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICardRepository {
    Mono<Boolean> existsByCardCVV(String cardCVV);
    Mono<CardDTO> findByCardNumber(String cardNumber);
    Flux<CardDTO> findByAccount_Id(String accountId);
    Mono<CardDTO> save(CardDTO card);
}

