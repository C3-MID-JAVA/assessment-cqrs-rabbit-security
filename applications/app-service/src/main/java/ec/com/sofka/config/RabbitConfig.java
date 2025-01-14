package ec.com.sofka.config;

import ec.com.sofka.utils.AccountCreatedProperties;
import ec.com.sofka.utils.AccountUpdatedProperties;
import ec.com.sofka.utils.TransactionCreatedProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
@Configuration
public class RabbitConfig {

    private final AccountCreatedProperties accountCreatedProperties;
    private final AccountUpdatedProperties accountUpdatedProperties;
    private final TransactionCreatedProperties transactionProperties;

    public RabbitConfig(AccountCreatedProperties accountCreatedProperties, AccountUpdatedProperties accountUpdatedProperties, TransactionCreatedProperties transactionProperties) {
        this.accountCreatedProperties = accountCreatedProperties;
        this.accountUpdatedProperties = accountUpdatedProperties;
        this.transactionProperties = transactionProperties;
    }

    @Bean
    public TopicExchange accountCreatedExchange() {
        return new TopicExchange(accountCreatedProperties.getExchangeName());
    }

    @Bean
    public Queue accountCreatedQueue() {
        return new Queue(accountCreatedProperties.getQueueName(), true);
    }

    @Bean
    public Binding accountCreatedBinding() {
        return BindingBuilder.bind(accountCreatedQueue())
                .to(accountCreatedExchange())
                .with(accountCreatedProperties.getRoutingKey());
    }

    @Bean
    public TopicExchange accountUpdatedExchange() {
        return new TopicExchange(accountUpdatedProperties.getExchangeName());
    }

    @Bean
    public Queue accountUpdatedQueue() {
        return new Queue(accountUpdatedProperties.getQueueName(), true);
    }

    @Bean
    public Binding accountUpdatedBinding() {
        return BindingBuilder.bind(accountUpdatedQueue())
                .to(accountUpdatedExchange())
                .with(accountUpdatedProperties.getRoutingKey());
    }
    @Bean
    public TopicExchange transactionCreatedExchange() {
        return new TopicExchange(transactionProperties.getExchangeName());
    }

    @Bean
    public Queue transactionCreatedQueue() {
        return new Queue(transactionProperties.getQueueName(), true);
    }

    @Bean
    public Binding transactionCreatedBinding() {
        return BindingBuilder.bind(transactionCreatedQueue())
                .to(transactionCreatedExchange())
                .with(transactionProperties.getRoutingKey());
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
            amqpAdmin.declareExchange(accountCreatedExchange());
            amqpAdmin.declareQueue(accountCreatedQueue());
            amqpAdmin.declareBinding(accountCreatedBinding());

            amqpAdmin.declareExchange(transactionCreatedExchange());
            amqpAdmin.declareQueue(transactionCreatedQueue());
            amqpAdmin.declareBinding(transactionCreatedBinding());

            amqpAdmin.declareExchange(accountUpdatedExchange());
            amqpAdmin.declareQueue(accountUpdatedQueue());
            amqpAdmin.declareBinding(accountUpdatedBinding());
        };
    }
}
