package ec.com.sofka.queries.usecases.viewusecases;

import ec.com.sofka.gateway.ICardRepository;
import ec.com.sofka.gateway.dto.card.CardDTO;
import ec.com.sofka.generics.interfaces.IUseCaseAccept;


public class CardSavedViewUseCase implements IUseCaseAccept<CardDTO, Void> {
    private final ICardRepository cardRepository;

    public CardSavedViewUseCase(ICardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }
    @Override
    public void accept(CardDTO dto) {

        cardRepository.save(dto).subscribe();
    }
}