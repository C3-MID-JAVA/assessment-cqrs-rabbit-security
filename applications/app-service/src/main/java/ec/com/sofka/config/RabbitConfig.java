package ec.com.sofka.config;

import ec.com.sofka.RabbitProperties;
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
    private final RabbitProperties rabbitProperties;

    public RabbitConfig(RabbitProperties rabbitProperties) {
        this.rabbitProperties = rabbitProperties;
    }

    @Bean
    public TopicExchange accountCreatedExchange() {
        return new TopicExchange(rabbitProperties.getAccountExchange());
    }

    @Bean
    public Queue accountCreatedQueue() {
        return new Queue(rabbitProperties.getAccountQueue(), true);
    }

    @Bean
    public Binding accountCreatedBinding() {
        return BindingBuilder.bind(accountCreatedQueue())
                .to(accountCreatedExchange())
                .with(rabbitProperties.getAccountRoutingKey());
    }

    @Bean
    public TopicExchange accountUpdatedExchange() {
        return new TopicExchange(rabbitProperties.getAccountUpdatedExchange());
    }

    @Bean
    public Queue accountUpdatedQueue() {
        return new Queue(rabbitProperties.getAccountUpdatedQueue(), true);
    }

    @Bean
    public Binding accountUpdatedBinding() {
        return BindingBuilder.bind(accountUpdatedQueue())
                .to(accountUpdatedExchange())
                .with(rabbitProperties.getAccountUpdatedRoutingKey());
    }

    @Bean
    public TopicExchange userCreatedExchange() {
        return new TopicExchange(rabbitProperties.getUserExchange());
    }

    @Bean
    public Queue userCreatedQueue() {
        return new Queue(rabbitProperties.getUserQueue(), true);
    }

    @Bean
    public Binding userCreatedBinding() {
        return BindingBuilder.bind(userCreatedQueue())
                .to(userCreatedExchange())
                .with(rabbitProperties.getUserRoutingKey());
    }

    @Bean
    public TopicExchange transactionCreatedExchange() {
        return new TopicExchange(rabbitProperties.getTransactionExchange());
    }

    @Bean
    public Queue transactionCreatedQueue() {
        return new Queue(rabbitProperties.getTransactionQueue(), true);
    }

    @Bean
    public Binding transactionCreatedBinding() {
        return BindingBuilder.bind(transactionCreatedQueue())
                .to(transactionCreatedExchange())
                .with(rabbitProperties.getTransactionRoutingKey());
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

            amqpAdmin.declareExchange(userCreatedExchange());
            amqpAdmin.declareQueue(userCreatedQueue());
            amqpAdmin.declareBinding(userCreatedBinding());

            amqpAdmin.declareExchange(transactionCreatedExchange());
            amqpAdmin.declareQueue(transactionCreatedQueue());
            amqpAdmin.declareBinding(transactionCreatedBinding());

            amqpAdmin.declareExchange(accountUpdatedExchange());
            amqpAdmin.declareQueue(accountUpdatedQueue());
            amqpAdmin.declareBinding(accountUpdatedBinding());
        };
    }
}