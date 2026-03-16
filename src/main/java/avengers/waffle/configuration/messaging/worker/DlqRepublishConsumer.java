package avengers.waffle.configuration.messaging.worker;

import avengers.waffle.configuration.messaging.UserRecommendJobMessage;
import avengers.waffle.configuration.messaging.outbox.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("worker")
@RequiredArgsConstructor
@Slf4j
public class DlqRepublishConsumer {

    private final RabbitTemplate rabbitTemplate;
    private final OutboxRepository outboxRepository;

    @Value("${app.recommendation.rabbit.exchange:exchange}")
    private String exchangeName;

    @Value("${app.recommendation.rabbit.routing-key:queue}")
    private String routingKey;

    @RabbitListener(
            queues = "${app.recommendation.rabbit.dlq:deadQueue}",
            concurrency = "${app.worker.dlq.concurrency:1}"
    )
    public void handleDlq(UserRecommendJobMessage msg) {
        String requestId = msg.getRequestId();

        outboxRepository.findById(requestId).ifPresent(outbox -> {
            // SENT → PENDING으로 변경
            // 폴링이 재시도 or Worker가 직접 재처리
            outbox.setStatus("PENDING");
            outboxRepository.save(outbox);
            log.warn("DLQ - PENDING 복구! requestId: {}", requestId);
        });

        rabbitTemplate.convertAndSend(exchangeName, routingKey, msg);
    }
}
