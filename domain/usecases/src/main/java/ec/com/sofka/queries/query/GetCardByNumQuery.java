package ec.com.sofka.queries.query;

import ec.com.sofka.generics.utils.Query;

public class GetCardByNumQuery extends Query {
    private final String accountNumber;



    public GetCardByNumQuery(String aggregateId, String accountNumber) {
        super(aggregateId);
        this.accountNumber = accountNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}
