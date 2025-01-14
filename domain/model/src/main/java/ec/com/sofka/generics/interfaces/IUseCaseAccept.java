package ec.com.sofka.generics.interfaces;

public interface IUseCaseAccept<T, Void> {
    void accept(T dto);
}
