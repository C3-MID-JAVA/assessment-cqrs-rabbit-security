package ec.com.sofka.aggregate.account.events;


import ec.com.sofka.account.Account;
import ec.com.sofka.generics.domain.DomainEvent;

import java.math.BigDecimal;

public class CardCreated extends DomainEvent {
    private String id;
    private  String cardName;
    private  String cardNumber;
    private  String cardType;
    private  String cardStatus;
    private  String cardExpiryDate;
    private  String cardCVV;
    private  BigDecimal cardLimit;
    private  String cardHolderName;
    private  Account accountValue;


    public CardCreated(String id, String cardName, String cardNumber, String cardType, String cardStatus, String cardExpiryDate, String cardCVV, BigDecimal cardLimit, String cardHolderName, Account accountValue) {
        super(EventsEnum.CARD_CREATED.name());
        this.id = id;

        this.cardName = cardName;
        this.cardNumber = cardNumber;
        this.cardType = cardType;
        this.cardStatus = cardStatus;
        this.cardExpiryDate = cardExpiryDate;
        this.cardCVV = cardCVV;
        this.cardLimit = cardLimit;
        this.cardHolderName = cardHolderName;
        this.accountValue = accountValue;
    }

    public CardCreated() {
        super(EventsEnum.CARD_CREATED.name());
    }

    public String getCardName() {
        return cardName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCardType() {
        return cardType;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public String getCardExpiryDate() {
        return cardExpiryDate;
    }

    public String getCardCVV() {
        return cardCVV;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public Account getAccountValue() {
        return accountValue;
    }

    public BigDecimal getCardLimit() {
        return cardLimit;
    }

    public String getId() {
        return id;
    }
}
