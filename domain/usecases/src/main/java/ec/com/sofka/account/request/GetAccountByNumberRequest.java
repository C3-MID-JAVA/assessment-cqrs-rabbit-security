package ec.com.sofka.account.request;

import ec.com.sofka.generics.utils.Request;

public class GetAccountByNumberRequest extends Request {

    private String accountNumber;

    public GetAccountByNumberRequest(final String aggregateId, String accountNumber) {
        super(aggregateId);
        this.accountNumber = accountNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}
