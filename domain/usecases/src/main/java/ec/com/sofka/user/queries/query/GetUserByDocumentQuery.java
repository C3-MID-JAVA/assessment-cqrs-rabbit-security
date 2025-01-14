package ec.com.sofka.user.queries.query;

import ec.com.sofka.generics.utils.Query;

public class GetUserByDocumentQuery extends Query {
    private final String documentId;

    public GetUserByDocumentQuery(String documentId) {
        super(null);
        this.documentId = documentId;
    }

    public String getDocumentId() {
        return documentId;
    }
}
