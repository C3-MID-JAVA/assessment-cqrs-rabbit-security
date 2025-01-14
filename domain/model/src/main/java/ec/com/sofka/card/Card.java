package ec.com.sofka.card;

import ec.com.sofka.card.values.CardId;
import ec.com.sofka.card.values.objects.*;
import ec.com.sofka.generics.utils.Entity;

public class Card extends Entity<CardId> {

    private final CardName cardName;
    private final CardNumber cardNumber;
    private final CardType cardType;
    private final CardStatus cardStatus;
    private final CardExpiryDate cardExpiryDate;
    private final CardCVV cardCVV;
    private final CardLimit cardLimit;
    private final CardHolderName cardHolderName;
    private final AccountValue accountValue;


    public Card(CardId id, CardName cardName, CardNumber cardNumber, CardType cardType, CardStatus cardStatus, CardExpiryDate cardExpiryDate, CardCVV cardCVV, CardLimit cardLimit, CardHolderName cardHolderName, AccountValue account) {
        super(id);
        this.cardName = cardName;
        this.cardNumber = cardNumber;
        this.cardType = cardType;
        this.cardStatus = cardStatus;
        this.cardExpiryDate = cardExpiryDate;
        this.cardCVV = cardCVV;
        this.cardLimit = cardLimit;
        this.cardHolderName = cardHolderName;
        this.accountValue = account;
    }

    public CardName getCardName() {
        return cardName;
    }

    public CardNumber getCardNumber() {
        return cardNumber;
    }

    public CardType getCardType() {
        return cardType;
    }

    public CardStatus getCardStatus() {
        return cardStatus;
    }

    public CardExpiryDate getCardExpiryDate() {
        return cardExpiryDate;
    }

    public CardCVV getCardCVV() {
        return cardCVV;
    }

    public CardHolderName getCardHolderName() {
        return cardHolderName;
    }

    public AccountValue getAccountValue() {
        return accountValue;
    }

    public CardLimit getCardLimit() {
        return cardLimit;
    }
}
