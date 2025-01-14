package ec.com.sofka.generics.interfaces;

import ec.com.sofka.generics.utils.Request;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

//9. Generics creation to apply DDD: IUseCase - Interface to execute use cases
public interface IUseCase<T extends Request, R> {
    Publisher<R> execute(T request);
}
