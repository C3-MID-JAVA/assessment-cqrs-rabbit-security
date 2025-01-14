package ec.com.sofka.queries.responses.card;

import ec.com.sofka.queries.responses.account.CreateAccountResponse;

import java.math.BigDecimal;

public class CreateCardResponse {
    private String id;
    private String customerId;
    private String cardName;
    private String cardNumber;
    private String cardType;
    private String cardStatus;
    private String cardExpiryDate;
    private String cardCVV;
    private BigDecimal cardLimit;
    private String cardHolderName;
    private CreateAccountResponse account;

    public CreateCardResponse(String id, String customerId, String cardName, String cardNumber, String cardType, String cardStatus, String cardExpiryDate, String cardCVV, BigDecimal cardLimit, String cardHolderName, CreateAccountResponse account) {
        this.id = id;
        this.customerId = customerId;
        this.cardName = cardName;
        this.cardNumber = cardNumber;
        this.cardType = cardType;
        this.cardStatus = cardStatus;
        this.cardExpiryDate = cardExpiryDate;
        this.cardCVV = cardCVV;
        this.cardLimit = cardLimit;
        this.cardHolderName = cardHolderName;
        this.account = account;
    }

    public String getId() {
        return id;
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

    public BigDecimal getCardLimit() {
        return cardLimit;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public CreateAccountResponse getAccount() {
        return account;
    }

    public String getCustomerId() {
        return customerId;
    }
}
