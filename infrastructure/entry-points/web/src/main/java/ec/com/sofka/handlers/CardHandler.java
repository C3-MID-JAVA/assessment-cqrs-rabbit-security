package ec.com.sofka.handlers;

import ec.com.sofka.commands.usecases.CreateCardUseCase;
import ec.com.sofka.queries.usecases.card.GetAllCardsUseCase;
import ec.com.sofka.data.CardReqDTO;
import ec.com.sofka.mapper.CardDTOMapper;
import ec.com.sofka.queries.query.GetCardByNumQuery;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class CardHandler {
    private final CreateCardUseCase createCardUseCase;
    private final GetAllCardsUseCase getAllCardsUseCase;


    public CardHandler(CreateCardUseCase createCardUseCase, GetAllCardsUseCase getAllCardsUseCase) {
        this.createCardUseCase = createCardUseCase;
        this.getAllCardsUseCase = getAllCardsUseCase;
    }

    public Mono<CardReqDTO> createCard(CardReqDTO request) {

        return createCardUseCase.execute(CardDTOMapper.toCardRequest(request)).map(CardDTOMapper::toCardReqDTO);
    }

    public Flux<CardReqDTO> getCardsByAccountNumber(String accountNumber) {
        return getAllCardsUseCase.get(new GetCardByNumQuery(null, accountNumber))
                .flatMap(res ->
                        Flux.fromIterable(res.getMultipleResults()
                                .stream()
                                .map(CardDTOMapper::toCardReqDTO)
                                .toList()));
    }
}
