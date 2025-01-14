package ec.com.sofka.generics.interfaces;

import ec.com.sofka.generics.shared.QueryResponse;
import org.reactivestreams.Publisher;

public interface IUseCaseGetElement<PropertyQuery, R> {
    Publisher<QueryResponse<R>> get(PropertyQuery request);
}