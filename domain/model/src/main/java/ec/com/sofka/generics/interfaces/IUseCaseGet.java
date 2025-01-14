package ec.com.sofka.generics.interfaces;

import ec.com.sofka.generics.utils.Query;
import ec.com.sofka.generics.utils.QueryResponse;
import org.reactivestreams.Publisher;

public interface IUseCaseGet<Q extends Query, R> {
    //R get(Q request); // For single result
    Publisher<QueryResponse<R>> get(Q request);  // For multiple results
}
