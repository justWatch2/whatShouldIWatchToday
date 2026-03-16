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

        String requestId = data.getId();

        if(ack){
            log.info("rabbitmq confirm 성공! requestId: {}", requestId);
            outboxRepository.findById(requestId).ifPresent(outbox -> {
                outbox.setStatus("SENT");
                outboxRepository.save(outbox);
                log.info("outbox SENT 처리! requestId: {}", requestId);
            });
            log.info("outbox confirm 처리 완료(삭제 안 함)! requestId: {}", requestId);
        }else{
            log.warn("rabbitmq confirm 실패! requestId: {}, cause: {}", requestId, cause);
            outboxRepository.findById(requestId).ifPresent(outbox -> {
                if(outbox.getRetryCount() >= MAX_RETRY){

                    outboxRepository.deleteById(requestId);
//                    outbox.setRetryCount(outbox.getRetryCount() + 1);

                    log.error("outbox 재시도 초과로 FAILED 처리! requestId: {}, retryCount: {}",
                            outbox.getRequestId(), outbox.getRetryCount());
                    return;
                }

                try{
                    UserRecommendJobMessage msg =
                            objectMapper.readValue(outbox.getPayload(), UserRecommendJobMessage.class);
                    outbox.setRetryCount(outbox.getRetryCount() + 1);
                    outboxRepository.save(outbox);

                    CorrelationData retryData = new CorrelationData(outbox.getRequestId());
                    log.warn("rabbitmq 재발행 시도! requestId: {}, retryCount: {}",
                            msg.getRequestId(), outbox.getRetryCount());
                    rabbitTemplate.convertAndSend(exchangeName ,  routingKey, msg, retryData);
                }catch(Exception e){
//                    outbox.setStatus("FAILED");
                    outbox.setRetryCount(outbox.getRetryCount() + 1);
                    outboxRepository.save(outbox);
                    log.error("rabbitmq 재발행 중 예외로 FAILED 처리! requestId: {}, retryCount: {}",
                            outbox.getRequestId(), outbox.getRetryCount(), e);
                }
            });
        }
    }
}
