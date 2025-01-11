package ec.com.sofka.generics.interfaces;

import ec.com.sofka.generics.utils.Query;
import ec.com.sofka.generics.utils.QueryResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

public interface IUseCaseGet<Q extends Query, R> {
    Mono<QueryResponse<R>> get(Q request);
}
