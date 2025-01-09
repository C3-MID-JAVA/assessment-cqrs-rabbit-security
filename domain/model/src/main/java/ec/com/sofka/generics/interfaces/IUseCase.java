package ec.com.sofka.generics.interfaces;
import org.reactivestreams.Publisher;

import ec.com.sofka.generics.utils.Command;
import reactor.core.publisher.Mono;

//9. Generics creation to apply DDD: IUseCase - Interface to execute use cases
public interface IUseCase<T extends Command, R> {
   Mono<R> execute(T request);
}
