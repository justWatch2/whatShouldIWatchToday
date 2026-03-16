package avengers.waffle.configuration.messaging.outbox;

import avengers.waffle.configuration.messaging.UserRecommendJobMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxService {

    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void saveRecommendMessage(UserRecommendJobMessage msg) throws Exception {
        Outbox outbox = Outbox.builder()
                .requestId(msg.getRequestId())
                .eventType("USER_RECOMMEND_REQUEST")
                .payload(objectMapper.writeValueAsString(msg))
                .status("PENDING")
                .retryCount(0)
                .createdAt(LocalDateTime.now())
                .build();

        Outbox saved = outboxRepository.save(outbox);
        log.info("outbox 테이블에 저장! requestId: {}", saved.getRequestId());

        eventPublisher.publishEvent(new OutboxCreatedEvent(saved.getRequestId()));
        log.info("outbox 이벤트 발행! requestId: {}", saved.getRequestId());
    }
}
