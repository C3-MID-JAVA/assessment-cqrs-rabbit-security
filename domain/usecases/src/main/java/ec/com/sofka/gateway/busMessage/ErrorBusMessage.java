package ec.com.sofka.gateway.busMessage;

import ec.com.sofka.model.ErrorMessage;

public interface ErrorBusMessage {

    void sendMsg(ErrorMessage message);

}