package ec.com.sofka;

import ec.com.sofka.gateway.busMessage.ErrorBusMessage;
import ec.com.sofka.model.ErrorMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class LogErrorBusAdapter implements ErrorBusMessage {
    private final RabbitTemplate rabbitTemplate;
    private final Environment environment;

    public LogErrorBusAdapter(RabbitTemplate rabbitTemplate, Environment environment) {
        this.rabbitTemplate = rabbitTemplate;
        this.environment = environment;
    }

    @Override
    public void sendMsg(ErrorMessage message) {

        rabbitTemplate.convertAndSend(Objects.requireNonNull(environment.getProperty("log.exchange")),
                Objects.requireNonNull(environment.getProperty("log.routingKey")),
                message);

    }

}