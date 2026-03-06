package avengers.waffle.configuration.messaging.worker;

import avengers.waffle.configuration.messaging.UserRecommendJobMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("worker")
@RequiredArgsConstructor
public class DlqRepublishConsumer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.recommendation.rabbit.exchange:exchange}")
    private String exchangeName;

    @Value("${app.recommendation.rabbit.routing-key:queue}")
    private String routingKey;

    @RabbitListener (queues = "${app.recommendation.rabbit.dlq:deadQueue}")
    public void handleDlq(UserRecommendJobMessage msg){
        rabbitTemplate.convertAndSend(exchangeName, routingKey, msg);
    }
}
