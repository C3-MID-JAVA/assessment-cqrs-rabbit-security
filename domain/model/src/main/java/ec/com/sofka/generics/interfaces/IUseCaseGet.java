package ec.com.sofka.generics.interfaces;

import ec.com.sofka.generics.utils.Query;
import ec.com.sofka.generics.utils.QueryResponse;
import org.reactivestreams.Publisher;


public interface IUseCaseGet <Q extends Query, R> {
    Publisher<QueryResponse<R>> get(Q request);
}