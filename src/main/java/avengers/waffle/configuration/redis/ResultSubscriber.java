package avengers.waffle.configuration.redis;

import avengers.waffle.configuration.messaging.worker.woutbox.OutboxResult;
import avengers.waffle.configuration.messaging.worker.woutbox.OutboxResultRepository;
import avengers.waffle.configuration.websocket.RecommendationWebSocketRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Profile("web")
@RequiredArgsConstructor
@Slf4j
public class ResultSubscriber implements MessageListener {

    private final OutboxResultRepository outboxResultRepository;
    private final RecommendationWebSocketRegistry registry;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String requestId = new String(message.getBody(), StandardCharsets.UTF_8);
        if (requestId.isBlank()) {
            return;
        }

        Optional<OutboxResult> outboxResult = outboxResultRepository.findById(requestId);
        if (outboxResult.isEmpty()) {
            return;
        }

        WebSocketSession session = registry.getSession(requestId);
        if (session == null || !session.isOpen()) {
            return;
        }

        try {
            Map<String, Object> wrapper = new HashMap<>();
            wrapper.put("type", "result");
            wrapper.put("requestId", requestId);
            wrapper.put("data", objectMapper.readTree(outboxResult.get().getPayload()));
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(wrapper)));
        } catch (Exception ex) {
            log.warn("Failed to push result requestId={}", requestId, ex);
        }
    }
}
