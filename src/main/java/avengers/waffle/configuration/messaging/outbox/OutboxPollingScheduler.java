package avengers.waffle.configuration.messaging.outbox;


import avengers.waffle.configuration.messaging.UserRecommendJobMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Profile("web")
@RequiredArgsConstructor
@Slf4j
public class OutboxPollingScheduler {

    private static final int MAX_RETRY = 3;

    private static final int PENDING_THRESHOLD_SECONDS = 30;

    // 한 번에 가져올 최대 개수
    private static final int BATCH_SIZE = 100;

    private final OutboxRepository outboxRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Value("${app.recommendation.rabbit.exchange:exchange}")
    private String exchangeName;

    @Value("${app.recommendation.rabbit.routing-key:queue}")
    private String routingKey;

    @Scheduled(fixedDelay = 5000)
    public void polling(){
        LocalDateTime threshold = LocalDateTime.now().minusSeconds(PENDING_THRESHOLD_SECONDS);

        List<Outbox> pendingList = outboxRepository.findByStatusAndCreatedAtBeforeOrderByCreatedAtAsc(
                "PENDING",
                threshold,
                PageRequest.of(0, BATCH_SIZE)
        );

        if( pendingList.isEmpty() ) return;

        log.info("폴링 실행 갯수 {}", pendingList.size());

        for(Outbox outbox : pendingList){
            retryPublish(outbox);
        }

    }

    private void retryPublish(Outbox outbox) {
        if(outbox.getRetryCount() >= MAX_RETRY){
            outbox.setStatus("FAILED");
            outboxRepository.save(outbox);
            log.info("polling enough : {}", outbox.getRequestId());
            return;
        }

        try{
            UserRecommendJobMessage msg =
                    objectMapper.readValue(outbox.getPayload(),UserRecommendJobMessage.class);

            outbox.setRetryCount(outbox.getRetryCount() + 1);
            outboxRepository.save(outbox);

            CorrelationData retryData = new CorrelationData(outbox.getRequestId());

            rabbitTemplate.convertAndSend(exchangeName, routingKey, msg, retryData);
        }catch (Exception e){
            outbox.setStatus("FAILED");
            outboxRepository.save(outbox);
            log.error("폴링 - 재발행 중 예외 FAILED 처리! requestId: {}",
                    outbox.getRequestId(), e);
        }
    }

}
