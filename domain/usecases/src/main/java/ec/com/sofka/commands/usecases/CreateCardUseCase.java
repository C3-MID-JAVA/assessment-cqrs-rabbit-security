package ec.com.sofka.commands.usecases;

import ec.com.sofka.Utils;
import ec.com.sofka.card.values.CardId;
import ec.com.sofka.gateway.BusEvent;
import ec.com.sofka.queries.responses.card.CreateCardResponse;
import ec.com.sofka.queries.usecases.account.GetAccountByAccountNumberUseCase;
import ec.com.sofka.aggregate.account.Customer;
import ec.com.sofka.aggregate.account.events.CardCreated;
import ec.com.sofka.card.Card;
import ec.com.sofka.gateway.ICardRepository;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.commands.CreateCardCommand;
import ec.com.sofka.generics.interfaces.IUseCaseExecute;
import ec.com.sofka.queries.query.GetAccountQuery;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.function.Supplier;

public class CreateCardUseCase implements IUseCaseExecute<CreateCardCommand, CreateCardResponse> {

    private final IEventStore eventRepository;
    private final ICardRepository cardRepository;
    private final GetAccountByAccountNumberUseCase getAccountByAccountNumberUseCase;
    private final BusEvent busEvent;




    public CreateCardUseCase(IEventStore eventStore, ICardRepository cardRepository, GetAccountByAccountNumberUseCase getAccountByAccountNumberUseCase, BusEvent busEvent) {
        this.eventRepository = eventStore;
        this.cardRepository = cardRepository;
        this.getAccountByAccountNumberUseCase = getAccountByAccountNumberUseCase;

        this.busEvent = busEvent;
    }

    @Override
    public Mono<CreateCardResponse> execute(CreateCardCommand cmd) {
        Flux<DomainEvent> events = eventRepository.findAggregate(cmd.getCustomerId());

        return Customer.from(cmd.getCustomerId(), events)
                .flatMap(customer -> {
                    Optional<Card> optionalCard = customer.getCards().stream()
                            .filter(cardRec -> cardRec.getCardNumber().getValue().equals(cmd.getCardNumber()))
                            .findFirst();
                    if (optionalCard.isPresent()) {
                        return Mono.error(new RuntimeException("Card already exists"));
                    } else {
                        return getAccountByAccountNumberUseCase.get(new GetAccountQuery(cmd.getCustomerId()))
                                .switchIfEmpty(Mono.error(new RuntimeException("Account does not exist")))
                                .flatMap(accountResponse -> getCvvCard()
                                        .flatMap(cvv -> {
                                            CardCreated cardCreated = new CardCreated(
                                                    new CardId().getValue(),
                                                    cmd.getCardName(),
                                                    cmd.getCardNumber(),
                                                    cmd.getCardType(),
                                                    cmd.getCardStatus(),
                                                    cmd.getCardExpiryDate(),
                                                    cvv,
                                                    cmd.getCardLimit(),
                                                    cmd.getCardHolderName(),
                                                    accountResponse.getSingleResult().get().getAccount()
                                            );
                                            customer.createCard(cardCreated);

                                            Card cardFromCustomer = customer.getCards().stream()
                                                    .filter(cardRec -> cardRec.getCardNumber().getValue().equals(cmd.getCardNumber()))
                                                    .findFirst()
                                                    .orElseThrow(() -> new RuntimeException("Getting creation failed in transaction"));

                                            CreateCardResponse cardToSave = new CreateCardResponse(
                                                    cardFromCustomer.getId().getValue(),
                                                    cmd.getCustomerId(),
                                                    cardFromCustomer.getCardName().getValue(),
                                                    cardFromCustomer.getCardNumber().getValue(),
                                                    cardFromCustomer.getCardType().getValue(),
                                                    cardFromCustomer.getCardStatus().getValue(),
                                                    cardFromCustomer.getCardExpiryDate().getValue(),
                                                    cardFromCustomer.getCardCVV().getValue(),
                                                    cardFromCustomer.getCardLimit().getValue(),
                                                    cardFromCustomer.getCardHolderName().getValue(),
                                                    accountResponse.getSingleResult().get().getCreateAccountResponse()
                                            );
                                            customer.getUncommittedEvents()
                                                    .stream()
                                                    .map(eventRepository::save)
                                                    .forEach(busEvent::sendEventCardCreated);

                                            customer.markEventsAsCommitted();
                                            return Mono.just(cardToSave);
                                        }));
                    }

                });


    }

    public Mono<String> getCvvCard() {
        Supplier<String> cvvGenerator = Utils::generateCvvCode;

        return Mono.defer(() -> Mono.just(cvvGenerator.get()))
                .flatMap(cvv -> existsCvv(cvv)
                        .flatMap(exists -> exists ? Mono.empty() : Mono.just(cvv)))
                .repeat()
                .next();
    }
    private Mono<Boolean> existsCvv(String cvv) {
        return cardRepository.existsByCardCVV(cvv);
    }


}
