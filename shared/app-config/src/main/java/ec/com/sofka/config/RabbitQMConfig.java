package ec.com.sofka.config;

import ec.com.sofka.utils.enums.EventsDetailsEnum;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.util.Objects;

@Configuration
@PropertySource("classpath:appConfig.properties")
public class RabbitQMConfig {

    private final Environment environment;

    public RabbitQMConfig(Environment environment) {
        this.environment = environment;
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
    public DirectExchange errorLogExchange() {
        return new DirectExchange(environment.getProperty("log.exchange"));
    }

    @Bean
    public Queue errorLogQueue() {
        return new Queue(Objects.requireNonNull(environment.getProperty("log.queue")), true);
    }

    @Bean
    public Binding errorLogBinding(Queue errorLogQueue, DirectExchange errorLogExchange) {
        return BindingBuilder.bind(errorLogQueue).to(errorLogExchange).with(environment.getProperty("log.routingKey"));
    }

    @Bean
    public DirectExchange userCreateExchange() {
        return new DirectExchange(environment.getProperty(EventsDetailsEnum.USER_CREATED.getExchange()));
    }

    @Bean
    public Queue userCreateQueue() {
        return new Queue(Objects.requireNonNull(environment.getProperty(EventsDetailsEnum.USER_CREATED.getQueue())), true);
    }

    @Bean
    public Binding userCreateBinding(Queue userCreateQueue, DirectExchange userCreateExchange) {
        return BindingBuilder.bind(userCreateQueue).to(userCreateExchange).with(environment.getProperty(EventsDetailsEnum.USER_CREATED.getRoutingKey()));
    }

    @Bean
    public DirectExchange userUpdateExchange() {
        return new DirectExchange(environment.getProperty(EventsDetailsEnum.USER_UPDATED.getExchange()));
    }

    @Bean
    public Queue userUpdateQueue() {
        return new Queue(Objects.requireNonNull(environment.getProperty(EventsDetailsEnum.USER_UPDATED.getQueue())), true);
    }

    @Bean
    public Binding userUpdateBinding(Queue userUpdateQueue, DirectExchange userUpdateExchange) {
        return BindingBuilder.bind(userUpdateQueue).to(userUpdateExchange).with(environment.getProperty(EventsDetailsEnum.USER_UPDATED.getRoutingKey()));
    }

    @Bean
    public DirectExchange customerCreateExchange() {
        return new DirectExchange(environment.getProperty(EventsDetailsEnum.CUSTOMER_CREATED.getExchange()));
    }

    @Bean
    public Queue customerCreateQueue() {
        return new Queue(Objects.requireNonNull(environment.getProperty(EventsDetailsEnum.CUSTOMER_CREATED.getQueue())), true);
    }

    @Bean
    public Binding customerCreateBinding(Queue customerCreateQueue, DirectExchange customerCreateExchange) {
        return BindingBuilder.bind(customerCreateQueue).to(customerCreateExchange).with(environment.getProperty(EventsDetailsEnum.CUSTOMER_CREATED.getRoutingKey()));
    }

    @Bean
    public DirectExchange accountCreateExchange() {
        return new DirectExchange(environment.getProperty(EventsDetailsEnum.ACCOUNT_CREATED.getExchange()));
    }

    @Bean
    public Queue accountCreateQueue() {
        return new Queue(Objects.requireNonNull(environment.getProperty(EventsDetailsEnum.ACCOUNT_CREATED.getQueue())), true);
    }

    @Bean
    public Binding accountCreateBinding(Queue accountCreateQueue, DirectExchange accountCreateExchange) {
        return BindingBuilder.bind(accountCreateQueue).to(accountCreateExchange).with(environment.getProperty(EventsDetailsEnum.ACCOUNT_CREATED.getRoutingKey()));
    }

    @Bean
    public DirectExchange accountUpdateExchange() {
        return new DirectExchange(environment.getProperty(EventsDetailsEnum.ACCOUNT_UPDATED.getExchange()));
    }

    @Bean
    public Queue accountUpdateQueue() {
        return new Queue(Objects.requireNonNull(environment.getProperty(EventsDetailsEnum.ACCOUNT_UPDATED.getQueue())), true);
    }

    @Bean
    public Binding accountUpdateBinding(Queue accountUpdateQueue, DirectExchange accountUpdateExchange) {
        return BindingBuilder.bind(accountUpdateQueue).to(accountUpdateExchange).with(environment.getProperty(EventsDetailsEnum.ACCOUNT_UPDATED.getRoutingKey()));
    }

    @Bean
    public DirectExchange transactionTypeCreateExchange() {
        return new DirectExchange(environment.getProperty(EventsDetailsEnum.TRANSACTION_TYPE_CREATED.getExchange()));
    }

    @Bean
    public Queue transactionTypeCreateQueue() {
        return new Queue(Objects.requireNonNull(environment.getProperty(EventsDetailsEnum.TRANSACTION_TYPE_CREATED.getQueue())), true);
    }

    @Bean
    public Binding transactionTypeCreateBinding(Queue transactionTypeCreateQueue, DirectExchange transactionTypeCreateExchange) {
        return BindingBuilder.bind(transactionTypeCreateQueue).to(transactionTypeCreateExchange).with(environment.getProperty(EventsDetailsEnum.TRANSACTION_TYPE_CREATED.getRoutingKey()));
    }

    @Bean
    public DirectExchange transactionCreateExchange() {
        return new DirectExchange(environment.getProperty(EventsDetailsEnum.TRANSACTION_CREATED.getExchange()));
    }

    @Bean
    public Queue transactionCreateQueue() {
        return new Queue(Objects.requireNonNull(environment.getProperty(EventsDetailsEnum.TRANSACTION_CREATED.getQueue())), true);
    }

    @Bean
    public Binding transactionCreateBinding(Queue transactionCreateQueue, DirectExchange transactionCreateExchange) {
        return BindingBuilder.bind(transactionCreateQueue).to(transactionCreateExchange).with(environment.getProperty(EventsDetailsEnum.TRANSACTION_CREATED.getRoutingKey()));
    }

}