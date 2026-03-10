package avengers.waffle.configuration.messaging.outbox;

import avengers.waffle.configuration.messaging.UserRecommendJobMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Component
@Profile("web")
@RequiredArgsConstructor
@Slf4j
public class RabbitConfirmHandler {

    private static final int MAX_RETRY = 3;

    private final OutboxRepository outboxRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Value("${app.recommendation.rabbit.exchange:exchange}")
    private String exchangeName;

    @Value("${app.recommendation.rabbit.routing-key:queue}")
    private String routingKey;

    public void handle(CorrelationData data , boolean ack , String cause){

        if(data == null) return;

        Long outboxId = Long.valueOf(data.getId());

        if(ack){
            log.info("rabbitmq confirm 성공! outboxId: {}", outboxId);
            outboxRepository.deleteById(outboxId);
            log.info("outbox 삭제 완료! outboxId: {}", outboxId);
        }else{
            log.warn("rabbitmq confirm 실패! outboxId: {}, cause: {}", outboxId, cause);
            outboxRepository.findById(outboxId).ifPresent(outbox -> {
                if(outbox.getRetryCount() > MAX_RETRY){

                    outbox.setStatus("FAILED");
                    outbox.setRetryCount(outbox.getRetryCount() + 1);
                    outboxRepository.save(outbox);
                    log.error("outbox 재시도 초과로 FAILED 처리! outboxId: {}, retryCount: {}",
                            outbox.getId(), outbox.getRetryCount());
                    return;
                }

                try{
                    UserRecommendJobMessage msg =
                            objectMapper.readValue(outbox.getPayload(), UserRecommendJobMessage.class);
                    outbox.setRetryCount(outbox.getRetryCount() + 1);
                    outboxRepository.save(outbox);

                    CorrelationData retryData = new CorrelationData(outbox.getId().toString());
                    log.warn("rabbitmq 재발행 시도! requestId: {}, outboxId: {}, retryCount: {}",
                            msg.getRequestId(), outbox.getId(), outbox.getRetryCount());
                    rabbitTemplate.convertAndSend(exchangeName ,  routingKey, msg, retryData);
                }catch(Exception e){
                    outbox.setStatus("FAILED");
                    outbox.setRetryCount(outbox.getRetryCount() + 1);
                    outboxRepository.save(outbox);
                    log.error("rabbitmq 재발행 중 예외로 FAILED 처리! outboxId: {}, retryCount: {}",
                            outbox.getId(), outbox.getRetryCount(), e);
                }
            });
        }
    }
}
