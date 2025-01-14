package ec.com.sofka.mapper;

import ec.com.sofka.data.CardReqDTO;
import ec.com.sofka.gateway.dto.card.CardDTO;
import ec.com.sofka.commands.CreateCardCommand;
import ec.com.sofka.queries.responses.card.CreateCardResponse;

public class CardDTOMapper {

    public static CardReqDTO toCardReqDTO(CreateCardResponse card) {
        return  new CardReqDTO(card.getCustomerId(), card.getCardName(), card.getCardNumber(), card.getCardType(), card.getCardStatus(),
                card.getCardExpiryDate(), card.getCardLimit(),
                card.getCardHolderName(), AccountDTOMapper.fromCtoAccountReqDTO(card.getAccount())
        );
    }

    public static CardDTO toCardDTO(CardReqDTO cardDTO) {
        if(cardDTO ==null) return null;

        return new CardDTO( cardDTO.getCustomerId(),cardDTO.getCardName(), cardDTO.getCardNumber(), cardDTO.getCardType(), cardDTO.getCardStatus(),
                cardDTO.getCardExpiryDate(), null, cardDTO.getCardLimit(),
                cardDTO.getCardHolderName(), AccountDTOMapper.toAccountDTO(cardDTO.getAccount())
        );
    }

    public static CreateCardCommand toCardRequest(CardReqDTO cardDTO) {
        if(cardDTO ==null) return null;

        return new CreateCardCommand(cardDTO.getCustomerId(),cardDTO.getCardName(),
                cardDTO.getCardNumber(), cardDTO.getCardType(), cardDTO.getCardStatus(),
                cardDTO.getCardExpiryDate(), null, cardDTO.getCardLimit(),
                cardDTO.getCardHolderName(), AccountDTOMapper.toAccountDTO(cardDTO.getAccount())
        );
    }
}
