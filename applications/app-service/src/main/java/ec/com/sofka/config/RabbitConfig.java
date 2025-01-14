package ec.com.sofka.config;

import ec.com.sofka.RabbitEnvProperties;
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
public class RabbitConfig {
    private final RabbitEnvProperties envProperties;

    public static final String EXCHANGE_NAME = "account.exchange";
    public static final String QUEUE_NAME = "account.created.queue";
    public static final String ROUTING_KEY = "account.routingKey";

    public RabbitConfig(RabbitEnvProperties envProperties) {
        this.envProperties = envProperties;
    }

    @Bean
    public TopicExchange accountExchange() {
        return new TopicExchange(envProperties.getAccountExchange(), true, false);
    }


    @Bean
    public Queue accountQueue() {
        return new Queue(envProperties.getAccountQueue(), true);
    }

    @Bean
    public Binding accountBinding() {
        return BindingBuilder.bind(accountQueue())
                .to(accountExchange())
                .with(envProperties.getAccountRoutingKey());
    }

    @Bean
    public TopicExchange cardExchange() {
        return new TopicExchange(envProperties.getCardExchange(), true, false);
    }


    @Bean
    public Queue cardQueue() {
        return new Queue(envProperties.getCardQueue(), true);
    }

    @Bean
    public Binding cardBinding() {
        return BindingBuilder.bind(cardQueue())
                .to(cardExchange())
                .with(envProperties.getCardRoutingKey());
    }

    @Bean
    public TopicExchange transactionExchange() {
        return new TopicExchange(envProperties.getTransactionExchange(), true, false);
    }


    @Bean
    public Queue transactionQueue() {
        return new Queue(envProperties.getTransactionQueue(), true);
    }

    @Bean
    public Binding transactionBinding() {
        return BindingBuilder.bind(transactionQueue())
                .to(transactionExchange())
                .with(envProperties.getTransactionRoutingKey());
    }

    @Bean
    public TopicExchange accountUpdatedExchange() {
        return new TopicExchange(envProperties.getAccountUpdatedExchange(), true, false);
    }


    @Bean
    public Queue accountUpdatedQueue() {
        return new Queue(envProperties.getAccountUpdatedQueue(), true);
    }

    @Bean
    public Binding accountUpdatedBinding() {
        return BindingBuilder.bind(accountUpdatedQueue())
                .to(accountUpdatedExchange())
                .with(envProperties.getAccountUpdateRoutingKey());
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> initializeBeans(AmqpAdmin amqpAdmin) {
        return event -> {


            amqpAdmin.declareExchange(accountExchange());
            amqpAdmin.declareQueue(accountQueue());
            amqpAdmin.declareBinding(accountBinding());

            amqpAdmin.declareExchange(cardExchange());
            amqpAdmin.declareQueue(cardQueue());
            amqpAdmin.declareBinding(cardBinding());

            amqpAdmin.declareExchange(transactionExchange());
            amqpAdmin.declareQueue(transactionQueue());
            amqpAdmin.declareBinding(transactionBinding());

            amqpAdmin.declareExchange(accountUpdatedExchange());
            amqpAdmin.declareQueue(accountUpdatedQueue());
            amqpAdmin.declareBinding(accountUpdatedBinding());
        };
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

}
