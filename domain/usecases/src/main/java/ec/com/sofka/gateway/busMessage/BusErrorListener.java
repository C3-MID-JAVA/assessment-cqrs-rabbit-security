package ec.com.sofka.gateway.busMessage;

import ec.com.sofka.model.ErrorMessage;

public interface BusErrorListener {

    void receiveLogError(ErrorMessage message);

}