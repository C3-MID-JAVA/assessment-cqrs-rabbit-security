package ec.com.sofka.appservice.queries.query;

import ec.com.sofka.generics.utils.Query;

public class GetByElementQuery extends Query {
    private final String element;

    public GetByElementQuery(final String aggregateId,
                             final String element) {
        super(aggregateId);
        this.element = element;
    }

    public String getElement() {
        return element;
    }
}
