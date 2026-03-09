package avengers.waffle.configuration.messaging;

import avengers.waffle.configuration.messaging.outbox.RabbitConfirmHandler;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitConfig {

    @Value("${app.recommendation.rabbit.exchange:exchange}")
    private String exchangeName;

    @Value("${app.recommendation.rabbit.queue:queue}")
    private String queueName;

    @Value("${app.recommendation.rabbit.routing-key:queue}")
    private String routingKey;

    @Value("${app.recommendation.rabbit.dlx:deadExchange}")
    private String deadLetterExchangeName;

    @Value("${app.recommendation.rabbit.dlq:deadQueue}")
    private String deadLetterQueueName;

    @Value("${app.recommendation.rabbit.dlq-routing-key:deadQueue}")
    private String deadLetterRoutingKey;

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchangeName);
    }

    @Bean
    public DirectExchange deadExchange() {
        return new DirectExchange(deadLetterExchangeName);
    }

    @Bean
    public Queue queue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", deadLetterExchangeName);
        args.put("x-dead-letter-routing-key", deadLetterRoutingKey);
        return new Queue(queueName, true, false, false, args);
    }

    @Bean
    public Queue deadQueue() {
        return new Queue(deadLetterQueueName, true);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

    @Bean
    public Binding deadBinding(Queue deadQueue, DirectExchange deadExchange) {
        return BindingBuilder.bind(deadQueue).to(deadExchange).with(deadLetterRoutingKey);
    }

    @Bean
    public MessageConverter rabbitMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         MessageConverter rabbitMessageConverter,
                                         ObjectProvider<RabbitConfirmHandler> handlerProvider) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(rabbitMessageConverter);
        template.setMandatory(true);
        template.setConfirmCallback((correlationData, ack, cause) -> {
            RabbitConfirmHandler handler = handlerProvider.getIfAvailable();
            if (handler != null) {
                handler.handle(correlationData, ack, cause);
            }
        });
        template.setReturnsCallback(returned -> {

        });
        return template;
    }
}
