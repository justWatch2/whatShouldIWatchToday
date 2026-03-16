package avengers.waffle.configuration.messaging.outbox;

import avengers.waffle.configuration.messaging.UserRecommendJobMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Profile("web")
@RequiredArgsConstructor
@Slf4j
public class OutboxPublishListener {
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;
    private final RabbitTemplate rabbitTemplate;

    @Value("${app.recommendation.rabbit.exchange:exchange}")
    private String exchangeName;

    @Value("${app.recommendation.rabbit.routing-key:queue}")
    private String routingKey;


    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onOutboxCreated(OutboxCreatedEvent event) throws JsonProcessingException {
        Outbox outbox = outboxRepository.findById(event.getRequestId()).orElseThrow();

        UserRecommendJobMessage msg =
                objectMapper.readValue(outbox.getPayload(), UserRecommendJobMessage.class);

        CorrelationData data = new CorrelationData(outbox.getRequestId());
        log.info("rabbitmq 발행 시도! requestId: {}, exchange: {}, routingKey: {}",
                msg.getRequestId(), exchangeName, routingKey);
        rabbitTemplate.convertAndSend(exchangeName, routingKey,msg,data);
        log.info("rabbitmq 발행 완료! requestId: {}", msg.getRequestId());

    }
}
