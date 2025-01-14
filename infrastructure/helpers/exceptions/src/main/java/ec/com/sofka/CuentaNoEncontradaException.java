package ec.com.sofka;

public class CuentaNoEncontradaException extends RuntimeException{
    public CuentaNoEncontradaException(String message){
        super(message);
    }
}
