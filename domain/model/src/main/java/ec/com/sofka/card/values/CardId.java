package ec.com.sofka.card.values;

import ec.com.sofka.generics.utils.Identity;

public class CardId extends Identity {
    public CardId() {
        super();
    }

    private CardId(final String id) {
        super(id);
    }

    public static CardId of(final String id) {
        return new CardId(id);
    }
}
