package ec.com.sofka.generics.interfaces;

import ec.com.sofka.generics.utils.Request;
import org.reactivestreams.Publisher;

public interface IUseEmptyCase<R> {
    Publisher<R> execute();
}
