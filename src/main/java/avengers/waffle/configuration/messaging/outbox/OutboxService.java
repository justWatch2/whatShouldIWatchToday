package avengers.waffle.configuration.messaging.outbox;

import avengers.waffle.configuration.messaging.UserRecommendJobMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OutboxService {

    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void saveRecommendMessage(UserRecommendJobMessage msg) throws Exception{


        Outbox outbox = Outbox.builder()
                .eventType("USER_RECOMMEND_REQUEST")
                .payload(objectMapper.writeValueAsString(msg))
                .status("NEW")
                .retryCount(0)
                .createdAt(LocalDateTime.now())
                .build();

        Outbox saved = outboxRepository.save(outbox);
        eventPublisher.publishEvent(new OutboxCreatedEvent(saved.getId()));

    }

}
