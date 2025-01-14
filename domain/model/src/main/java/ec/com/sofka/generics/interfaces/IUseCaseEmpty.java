package ec.com.sofka.generics.interfaces;


import org.reactivestreams.Publisher;

public interface IUseCaseEmpty<R> {
    Publisher<R> execute();
}

