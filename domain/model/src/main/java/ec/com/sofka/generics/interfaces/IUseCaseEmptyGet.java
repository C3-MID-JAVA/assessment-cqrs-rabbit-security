package ec.com.sofka.generics.interfaces;

import ec.com.sofka.generics.utils.QueryResponse;
import reactor.core.publisher.Mono;

public interface IUseCaseEmptyGet<R> {
    Mono<QueryResponse<R>> get();
}
