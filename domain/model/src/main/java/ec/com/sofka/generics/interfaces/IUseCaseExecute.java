package ec.com.sofka.generics.interfaces;

import ec.com.sofka.generics.utils.Command;
import org.reactivestreams.Publisher;

public interface IUseCaseExecute<T extends Command, R> {
    Publisher<R> execute(T cmd);
}
