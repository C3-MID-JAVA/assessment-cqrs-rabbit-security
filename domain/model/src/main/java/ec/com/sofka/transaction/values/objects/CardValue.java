package ec.com.sofka.transaction.values.objects;

import ec.com.sofka.card.Card;
import ec.com.sofka.generics.interfaces.IValueObject;

public class CardValue implements IValueObject<Card> {
    private final Card card;

    private CardValue(final Card card) {
        this.card = card;
    }

    public static CardValue of(final Card card) {
        return new CardValue(card);
    }

    @Override
    public Card getValue() {
        return card;
    }

}

