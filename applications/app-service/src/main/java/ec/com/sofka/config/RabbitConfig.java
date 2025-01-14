/*package ec.com.sofka.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Value("${app.account.created.queue}")
    private String accountCreatedQueue;

    @Value("${app.account.created.exchange}")
    private String accountCreatedExchange;

    @Value("${app.account.created.routingKey}")
    private String accountCreatedRoutingKey;

    //public static final String EXCHANGE_NAME = "account.exchange";
   // public static final String QUEUE_NAME = "account.created.queue";
    public static final String ROUTING_KEY = "account.routingKey";

    @Bean
    public TopicExchange accountExchange() {
        return new TopicExchange(accountCreatedExchange);
    }

    @Bean
    public Queue accountQueue() {
        return new Queue(accountCreatedQueue, true);
    }

    @Bean
    public Binding accountBinding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with(accountCreatedRoutingKey);
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
*/
/*
package ec.com.sofka.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${app.account.created.queue}")
    private String accountCreatedQueue;

    @Value("${app.account.created.exchange}")
    private String accountCreatedExchange;

    @Value("${app.account.created.routingKey}")
    private String accountCreatedRoutingKey;

    @Value("${app.account.updated.queue}")
    private String accountUpdatedQueue;

    @Value("${app.account.updated.exchange}")
    private String accountUpdatedExchange;

    @Value("${app.account.updated.routingKey}")
    private String accountUpdatedRoutingKey;

    @Bean
    public TopicExchange accountCreatedExchange() {
        return new TopicExchange(accountCreatedExchange);
    }

    @Bean
    public Queue accountCreatedQueue() {
        return new Queue(accountCreatedQueue, true);
    }

    @Bean
    public Binding accountCreatedBinding(Queue accountCreatedQueue, TopicExchange accountCreatedExchange) {
        return BindingBuilder.bind(accountCreatedQueue)
                .to(accountCreatedExchange)
                .with(accountCreatedRoutingKey);
    }

    @Bean
    public TopicExchange accountUpdatedExchange() {
        return new TopicExchange(accountUpdatedExchange);
    }

    @Bean
    public Queue accountUpdatedQueue() {
        return new Queue(accountUpdatedQueue, true);
    }

    @Bean
    public Binding accountUpdatedBinding(Queue accountUpdatedQueue, TopicExchange accountUpdatedExchange) {
        return BindingBuilder.bind(accountUpdatedQueue)
                .to(accountUpdatedExchange)
                .with(accountUpdatedRoutingKey);
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
*/
package ec.com.sofka.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${app.account.created.queue}")
    private String accountCreatedQueue;

    @Value("${app.account.created.exchange}")
    private String accountCreatedExchange;

    @Value("${app.account.created.routingKey}")
    private String accountCreatedRoutingKey;

    @Value("${app.account.updated.queue}")
    private String accountUpdatedQueue;

    @Value("${app.account.updated.exchange}")
    private String accountUpdatedExchange;

    @Value("${app.account.updated.routingKey}")
    private String accountUpdatedRoutingKey;

    @Value("${app.transaction.created.queue}")
    private String transactionCreatedQueue;

    @Value("${app.transaction.created.exchange}")
    private String transactionCreatedExchange;

    @Value("${app.transaction.created.routingKey}")
    private String transactionCreatedRoutingKey;

    @Bean
    public TopicExchange accountCreatedExchange() {
        return new TopicExchange(accountCreatedExchange);
    }

    @Bean
    public Queue accountCreatedQueue() {
        return new Queue(accountCreatedQueue, true);
    }

    @Bean
    public Binding accountCreatedBinding(Queue accountCreatedQueue, TopicExchange accountCreatedExchange) {
        return BindingBuilder.bind(accountCreatedQueue)
                .to(accountCreatedExchange)
                .with(accountCreatedRoutingKey);
    }

    @Bean
    public TopicExchange accountUpdatedExchange() {
        return new TopicExchange(accountUpdatedExchange);
    }

    @Bean
    public Queue accountUpdatedQueue() {
        return new Queue(accountUpdatedQueue, true);
    }

    @Bean
    public Binding accountUpdatedBinding(Queue accountUpdatedQueue, TopicExchange accountUpdatedExchange) {
        return BindingBuilder.bind(accountUpdatedQueue)
                .to(accountUpdatedExchange)
                .with(accountUpdatedRoutingKey);
    }

    @Bean
    public TopicExchange transactionCreatedExchange() {
        return new TopicExchange(transactionCreatedExchange);
    }

    @Bean
    public Queue transactionCreatedQueue() {
        return new Queue(transactionCreatedQueue, true);
    }

    @Bean
    public Binding transactionCreatedBinding(Queue transactionCreatedQueue, TopicExchange transactionCreatedExchange) {
        return BindingBuilder.bind(transactionCreatedQueue)
                .to(transactionCreatedExchange)
                .with(transactionCreatedRoutingKey);
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
