package ec.com.sofka.transaction.request;

import ec.com.sofka.generics.utils.Request;

public class GetAllByAccountNumberRequest extends Request {
    private final String customerId;
    private final String accountNumber;

    public GetAllByAccountNumberRequest(String customerId, String accountNumber) {
        super(null);
        this.customerId = customerId;
        this.accountNumber = accountNumber;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}
