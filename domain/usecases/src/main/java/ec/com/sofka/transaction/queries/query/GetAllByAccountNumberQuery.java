package ec.com.sofka.transaction.queries.query;

import ec.com.sofka.generics.utils.Query;

public class GetAllByAccountNumberQuery extends Query {
    private final String accountNumber;

    public GetAllByAccountNumberQuery(String accountNumber) {
        super(null);
        this.accountNumber = accountNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}
