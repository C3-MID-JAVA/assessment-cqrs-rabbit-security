package ec.com.sofka.queries.usecases.card;

import ec.com.sofka.gateway.IAccountRepository;
import ec.com.sofka.gateway.ICardRepository;
import ec.com.sofka.gateway.dto.account.AccountDTO;
import ec.com.sofka.gateway.dto.card.CardDTO;
import ec.com.sofka.generics.utils.QueryResponse;
import ec.com.sofka.queries.query.GetCardByNumQuery;
import ec.com.sofka.queries.responses.account.CreateAccountResponse;
import ec.com.sofka.queries.responses.card.CreateCardResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllCardsUseCaseTest {

    @Mock
    private ICardRepository cardRepository;

    @Mock
    private IAccountRepository accountRepository;

    @InjectMocks
    private GetAllCardsUseCase getCardsByAccountUseCase;

    AccountDTO account;
    CardDTO card;
    CardDTO card2;

    @BeforeEach
    void setUp() {
        account = new AccountDTO();
        account.setAccountNumber("123456789");
        account.setId("145263");

        card = new CardDTO();
        card2 = new CardDTO();

        card2.setCardNumber("123456789");
        card.setCardNumber("987654321");
    }

    @Test
    @DisplayName("Should return all card relate with account")
    void apply() {

        when(accountRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(Mono.just(account));
        when(cardRepository.findByAccount_Id(account.getId())).thenReturn(Flux.just(card, card2));

        Flux<QueryResponse<CreateCardResponse>> result = getCardsByAccountUseCase.get(new GetCardByNumQuery(null
                ,account.getAccountNumber()));

        StepVerifier.create(result)
                .expectNextMatches(query  -> query.getMultipleResults().size()  == 2)

                .verifyComplete();

        verify(cardRepository, times(1)).findByAccount_Id(account.getId());
        verify(accountRepository, times(1)).findByAccountNumber(account.getAccountNumber());
    }

    @Test
    @DisplayName("Should return zero cards")
    void apply_errorEnAccount() {

        when(accountRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(Mono.empty());

        Flux<QueryResponse<CreateCardResponse>> result = getCardsByAccountUseCase.get(new GetCardByNumQuery(null
                ,account.getAccountNumber()));

        StepVerifier.create(result)
                .expectNextMatches(query  -> query.getMultipleResults().isEmpty())
                .verifyComplete();

        verify(cardRepository, times(0)).findByAccount_Id(account.getId());
        verify(accountRepository, times(1)).findByAccountNumber(account.getAccountNumber());
    }
}