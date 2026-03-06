package avengers.waffle.configuration.websocket;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Profile("web")
public class RecommendationWebSocketRegistry {

    private final ConcurrentHashMap<String, WebSocketSession> requestToSession = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Set<String>> sessionToRequests = new ConcurrentHashMap<>();

    public void bind(String requestId, WebSocketSession session) {
        requestToSession.put(requestId, session);
        sessionToRequests.computeIfAbsent(session.getId(), key -> ConcurrentHashMap.newKeySet())
                .add(requestId);
    }

    public WebSocketSession getSession(String requestId) {
        return requestToSession.get(requestId);
    }

    public void removeSession(WebSocketSession session) {
        Set<String> requestIds = sessionToRequests.remove(session.getId());
        if (requestIds == null) {
            return;
        }
        for (String requestId : requestIds) {
            WebSocketSession mappedSession = requestToSession.get(requestId);
            if (mappedSession != null && mappedSession.getId().equals(session.getId())) {
                requestToSession.remove(requestId);
            }
        }
    }
}
