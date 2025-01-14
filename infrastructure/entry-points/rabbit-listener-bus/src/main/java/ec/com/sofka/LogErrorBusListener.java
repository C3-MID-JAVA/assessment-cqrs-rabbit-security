package ec.com.sofka;

import ec.com.sofka.gateway.busMessage.BusErrorListener;
import ec.com.sofka.model.ErrorMessage;
import ec.com.sofka.queries.usecases.ErrorLogSavedViewUseCase;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class LogErrorBusListener implements BusErrorListener {
    private final ErrorLogSavedViewUseCase errorLogSavedViewUseCase;

    public LogErrorBusListener(ErrorLogSavedViewUseCase errorLogSavedViewUseCase) {
        this.errorLogSavedViewUseCase = errorLogSavedViewUseCase;
    }

    @Override
    @RabbitListener(queues = "${log.queue}")
    public void receiveLogError(ErrorMessage errorMessage) {
            errorLogSavedViewUseCase.accept(errorMessage);
    }
}
