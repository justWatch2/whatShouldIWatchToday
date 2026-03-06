package avengers.waffle.configuration.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@Profile("web")
@RequiredArgsConstructor
public class RecommendationWebSocketConfig implements WebSocketConfigurer {

    private final RecommendationWebSocketHandler recommendationWebSocketHandler;

    @Value("${app.recommendation.websocket.allowed-origins:*}")
    private String allowedOrigins;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(recommendationWebSocketHandler, "/ws/recommend")
                .setAllowedOriginPatterns(allowedOrigins.split(","));
    }
}
