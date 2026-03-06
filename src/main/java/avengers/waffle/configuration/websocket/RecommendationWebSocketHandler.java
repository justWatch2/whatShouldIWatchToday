package avengers.waffle.configuration.websocket;

import avengers.waffle.configuration.messaging.worker.woutbox.OutboxResultRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Profile("web")
@RequiredArgsConstructor
@Slf4j
public class RecommendationWebSocketHandler extends TextWebSocketHandler {

    private final RecommendationWebSocketRegistry registry;
    private final ObjectMapper objectMapper;
    private final OutboxResultRepository outboxResultRepository;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        Map<String, String> payload = new HashMap<>();
        payload.put("type", "session");
        payload.put("sessionId", session.getId());
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(payload)));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String payload = message.getPayload();
        JsonNode root;
        try {
            root = objectMapper.readTree(payload);
        } catch (Exception ex) {
            session.sendMessage(new TextMessage("{\"type\":\"error\",\"message\":\"invalid_json\"}"));
            return;
        }

        String type = root.path("type").asText("");
        if ("result_ack".equals(type)) {
            String requestId = root.path("requestId").asText("");
            if (requestId.isBlank()) {
                session.sendMessage(new TextMessage("{\"type\":\"error\",\"message\":\"missing_requestId\"}"));
                return;
            }
            outboxResultRepository.deleteById(requestId);
            session.sendMessage(new TextMessage("{\"type\":\"ack\",\"requestId\":\"" + requestId + "\"}"));
            return;
        }

        if (!"bind".equals(type)) {
            session.sendMessage(new TextMessage("{\"type\":\"error\",\"message\":\"unknown_type\"}"));
            return;
        }

        String requestId = root.path("requestId").asText("");
        if (requestId.isBlank()) {
            session.sendMessage(new TextMessage("{\"type\":\"error\",\"message\":\"missing_requestId\"}"));
            return;
        }

        registry.bind(requestId, session);
        session.sendMessage(new TextMessage("{\"type\":\"ack\",\"requestId\":\"" + requestId + "\"}"));
        log.info("WebSocket bound requestId={} to session={}", requestId, session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        registry.removeSession(session);
        log.info("WebSocket closed session={} status={}", session.getId(), status);
    }
}
