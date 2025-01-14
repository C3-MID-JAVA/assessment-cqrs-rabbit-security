package ec.com.sofka.queries.query;

import ec.com.sofka.generics.utils.Query;

import java.math.BigDecimal;

public class GetAccountQuery extends Query {


    public GetAccountQuery(final String aggregateId) {
        super(aggregateId);
    }
}
