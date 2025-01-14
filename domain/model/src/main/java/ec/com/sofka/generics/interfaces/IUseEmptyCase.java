package ec.com.sofka.generics.interfaces;

import org.reactivestreams.Publisher;

public interface IUseEmptyCase<R> {
    Publisher<R> execute();
}