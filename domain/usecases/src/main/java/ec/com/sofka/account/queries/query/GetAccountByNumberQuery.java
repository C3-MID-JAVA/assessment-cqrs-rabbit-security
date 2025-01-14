package ec.com.sofka.account.queries.query;

import ec.com.sofka.generics.utils.Query;

public class GetAccountByNumberQuery extends Query {
    private final String accountNumber;

    public GetAccountByNumberQuery(String accountNumber) {
        super(null);
        this.accountNumber = accountNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}
