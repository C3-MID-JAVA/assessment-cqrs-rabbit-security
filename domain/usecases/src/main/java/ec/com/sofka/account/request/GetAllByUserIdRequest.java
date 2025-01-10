package ec.com.sofka.account.request;

import ec.com.sofka.generics.utils.Request;

public class GetAllByUserIdRequest extends Request {
    public GetAllByUserIdRequest(String aggregateId) {
        super(aggregateId);
    }
}
