package avengers.waffle.configuration.messaging.outbox;

import avengers.waffle.configuration.messaging.UserRecommendJobMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Component
@Profile("web")
@RequiredArgsConstructor
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
            outboxRepository.deleteById(outboxId);
        }else{
            outboxRepository.findById(outboxId).ifPresent(outbox -> {
                if(outbox.getRetryCount() > MAX_RETRY){

                    outbox.setStatus("FAILED");
                    outbox.setRetryCount(outbox.getRetryCount() + 1);
                    outboxRepository.save(outbox);
                    return;
                }

                try{
                    UserRecommendJobMessage msg =
                            objectMapper.readValue(outbox.getPayload(), UserRecommendJobMessage.class);
                    outbox.setRetryCount(outbox.getRetryCount() + 1);
                    outboxRepository.save(outbox);

                    CorrelationData retryData = new CorrelationData(outbox.getId().toString());
                    rabbitTemplate.convertAndSend(exchangeName ,  routingKey, msg, retryData);
                }catch(Exception e){
                    outbox.setStatus("FAILED");
                    outbox.setRetryCount(outbox.getRetryCount() + 1);
                    outboxRepository.save(outbox);
                }
            });
        }
    }
}
