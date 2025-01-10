package ec.com.sofka.config;

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

    // Configuración para account
    @Bean
    public TopicExchange accountExchange() {
        return new TopicExchange("account.created.exchange");
    }

    @Bean
    public Queue accountQueue() {
        return new Queue("account.created.queue", true);
    }

    @Bean
    public Binding accountBinding() {
        return BindingBuilder.bind(accountQueue())
                .to(accountExchange())
                .with("account.created.event");
    }

    // Configuración para createUser
    @Bean
    public TopicExchange createUserExchange() {
        return new TopicExchange("user.created.exchange");
    }

    @Bean
    public Queue createUserQueue() {
        return new Queue("user.created.queue", true);
    }

    @Bean
    public Binding createUserBinding() {
        return BindingBuilder.bind(createUserQueue())
                .to(createUserExchange())
                .with("user.created.event");
    }

    // Configuración para transaction
    @Bean
    public TopicExchange transactionExchange() {
        return new TopicExchange("transaction.created.exchange");
    }

    @Bean
    public Queue transactionQueue() {
        return new Queue("transaction.created.queue", true);
    }

    @Bean
    public Binding transactionBinding() {
        return BindingBuilder.bind(transactionQueue())
                .to(transactionExchange())
                .with("transaction.created.event");
    }

    // Configuración para balance
    @Bean
    public TopicExchange balanceExchange() {
        return new TopicExchange("balance.updated.exchange");
    }

    @Bean
    public Queue balanceQueue() {
        return new Queue("balance.updated.queue", true);
    }

    @Bean
    public Binding balanceBinding() {
        return BindingBuilder.bind(balanceQueue())
                .to(balanceExchange())
                .with("balance.updated.event");
    }

    // Configuración general
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
            // Declarar beans para account
            amqpAdmin.declareExchange(accountExchange());
            amqpAdmin.declareQueue(accountQueue());
            amqpAdmin.declareBinding(accountBinding());

            // Declarar beans para createUser
            amqpAdmin.declareExchange(createUserExchange());
            amqpAdmin.declareQueue(createUserQueue());
            amqpAdmin.declareBinding(createUserBinding());

            // Declarar beans para transaction
            amqpAdmin.declareExchange(transactionExchange());
            amqpAdmin.declareQueue(transactionQueue());
            amqpAdmin.declareBinding(transactionBinding());

            // Declarar beans para balance
            amqpAdmin.declareExchange(balanceExchange());
            amqpAdmin.declareQueue(balanceQueue());
            amqpAdmin.declareBinding(balanceBinding());
        };
    }
}
