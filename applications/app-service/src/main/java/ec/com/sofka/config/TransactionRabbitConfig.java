package ec.com.sofka.config;

import ec.com.sofka.utils.TransactionCreatedProperties;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransactionRabbitConfig {

    private final TransactionCreatedProperties transactionProperties;

    public TransactionRabbitConfig(TransactionCreatedProperties transactionProperties) {
        this.transactionProperties = transactionProperties;
    }

    @Bean
    public TopicExchange transactionExchange() {
        return new TopicExchange(transactionProperties.getExchangeName());
    }

    @Bean
    public Queue transactionQueue() {
        return new Queue(transactionProperties.getQueueName(), true);
    }

    @Bean
    public Binding transactionBinding() {
        return BindingBuilder.bind(transactionQueue())
                .to(transactionExchange())
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

}
