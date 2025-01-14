package ec.com.sofka.account.queries.query;


import ec.com.sofka.generics.utils.Query;

public class GetAllByUserIdQuery extends Query {
    private final String userId;

    public GetAllByUserIdQuery(String userId) {
        super(null);
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
