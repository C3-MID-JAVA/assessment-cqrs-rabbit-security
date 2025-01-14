package ec.com.sofka.generics.interfaces;

import ec.com.sofka.generics.utils.QueryResponse;
import org.reactivestreams.Publisher;

public interface IUseCaseGetEmpty<R> {
    Publisher<QueryResponse<R>> get();
}
