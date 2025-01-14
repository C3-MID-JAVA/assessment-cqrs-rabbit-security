package ec.com.sofka.mapper;

import ec.com.sofka.data.CardEntity;
import ec.com.sofka.gateway.dto.card.CardDTO;

public class CardMapper {

    public static CardDTO toCardDTO(CardEntity cardEntity) {
        if (cardEntity == null) return null;
        return new CardDTO(cardEntity.getId(), null, cardEntity.getCardName(),cardEntity.getCardNumber(),
                cardEntity.getCardType(), cardEntity.getCardStatus(), cardEntity.getCardExpiryDate(),
                cardEntity.getCardCVV(), cardEntity.getCardLimit(), cardEntity.getCardHolderName(),
                AccountMapper.toDTO(cardEntity.getAccount()));
    }


    public static CardEntity toCardEntity(CardDTO card) {
        if (card == null) return null;

        return new CardEntity(card.getId(),card.getCardName(),card.getCardNumber(),
                card.getCardType(), card.getCardStatus(), card.getCardExpiryDate(),
                card.getCardCVV(), card.getCardLimit(), card.getCardHolderName(),
                AccountMapper.toEntity(card.getAccount()));
    }
}