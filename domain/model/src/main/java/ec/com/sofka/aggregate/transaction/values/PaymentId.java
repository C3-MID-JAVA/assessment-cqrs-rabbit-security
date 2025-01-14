package ec.com.sofka.aggregate.transaction.values;


import ec.com.sofka.generics.utils.Identity;

public class PaymentId extends Identity {
    public PaymentId() {
        super();
    }

    private PaymentId(final String id) {
        super(id);
    }

    public static PaymentId of(final String id) {
        return new PaymentId(id);
    }
}
