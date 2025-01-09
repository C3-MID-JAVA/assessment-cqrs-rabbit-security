package ec.com.sofka.config;

import ec.com.sofka.utils.AccountCreatedProperties;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountRabbitConfig {

    private final AccountCreatedProperties accountProperties;

    public AccountRabbitConfig(AccountCreatedProperties accountProperties) {
        this.accountProperties = accountProperties;
    }

    @Bean
    public TopicExchange accountExchange() {
        return new TopicExchange(accountProperties.getExchangeName());
    }

    @Bean
    public Queue accountQueue() {
        return new Queue(accountProperties.getQueueName(), true);
    }

    @Bean
    public Binding accountBinding() {
        return BindingBuilder.bind(accountQueue())
                .to(accountExchange())
                .with(accountProperties.getRoutingKey());
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate rabbitTemplateBean(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> initializeBeans(AmqpAdmin amqpAdmin) {
        return event -> {
            amqpAdmin.declareExchange(accountExchange());
            amqpAdmin.declareQueue(accountQueue());
            amqpAdmin.declareBinding(accountBinding());
        };
    }
}
