package ec.com.sofka.queries.usecases.card;




import ec.com.sofka.aggregate.account.Customer;
import ec.com.sofka.gateway.IAccountRepository;
import ec.com.sofka.gateway.ICardRepository;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.generics.interfaces.IUseCaseGet;
import ec.com.sofka.generics.utils.QueryResponse;
import ec.com.sofka.queries.query.GetCardByNumQuery;
import ec.com.sofka.queries.responses.card.CreateCardResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public class GetAllCardsUseCase implements IUseCaseGet<GetCardByNumQuery, CreateCardResponse> {
    private final ICardRepository cardRepository;
    private final IAccountRepository accountRepository;


    public GetAllCardsUseCase(ICardRepository cardRepository, IAccountRepository accountRepository) {
        this.cardRepository = cardRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public Flux<QueryResponse<CreateCardResponse>> get(GetCardByNumQuery request) {
        return accountRepository.findByAccountNumber(request.getAccountNumber()).switchIfEmpty(Mono.empty())
                .flatMapMany(accountModel -> cardRepository.findByAccount_Id(accountModel.getId()))
                .map(card -> new CreateCardResponse(
                        null,
                        null,
                        card.getCardName(),
                        card.getCardNumber(),
                        card.getCardType(),
                        card.getCardStatus(),
                        card.getCardExpiryDate(),
                        card.getCardCVV(),
                        card.getCardLimit(),
                        card.getCardHolderName(),
                        null

                ))
                .collectList()
                .flatMapMany(cards ->
                        Flux.just(QueryResponse.ofMultiple(cards)));

    }
}
