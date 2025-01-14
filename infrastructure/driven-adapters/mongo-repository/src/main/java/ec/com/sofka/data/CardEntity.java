package ec.com.sofka.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Document(collection = "cards")
public class CardEntity {
    @Id
    private String id;

    @Field(name = "card_name")
    private String cardName;

    @Field(name = "card_number")
    private String cardNumber;

    @Field(name = "card_type")
    private String cardType;

    @Field(name = "card_status")
    private String cardStatus;

    @Field(name = "card_expiry_date")
    private String cardExpiryDate;

    @Field(name = "card_cvv")
    private String cardCVV;

    @Field(name = "card_limit")
    private BigDecimal cardLimit;

    @Field(name = "card_holder_name")
    private String cardHolderName;

    @DocumentReference
    @Field(name = "account")
    private AccountEntity account;

    public CardEntity() {

    }

    public CardEntity(String id, String cardName, String cardNumber, String cardType, String cardStatus, String cardExpiryDate, String cardCVV, BigDecimal cardLimit, String cardHolderName, AccountEntity account) {
        this.id = id;
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

    public void setId(String id) {
        this.id = id;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }

    public String getCardExpiryDate() {
        return cardExpiryDate;
    }

    public void setCardExpiryDate(String cardExpiryDate) {
        this.cardExpiryDate = cardExpiryDate;
    }

    public String getCardCVV() {
        return cardCVV;
    }

    public void setCardCVV(String cardCVV) {
        this.cardCVV = cardCVV;
    }

    public BigDecimal getCardLimit() {
        return cardLimit;
    }

    public void setCardLimit(BigDecimal cardLimit) {
        this.cardLimit = cardLimit;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public AccountEntity getAccount() {
        return account;
    }

    public void setAccount(AccountEntity account) {
        this.account = account;
    }
}