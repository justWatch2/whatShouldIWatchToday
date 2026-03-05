package avengers.waffle.configuration.redis;

import avengers.waffle.configuration.messaging.RecommendationResultSubscriber;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
public class RedisPubSubConfig {

    @Value("${app.recommendation.redis-channel:recommendation.result}")
    private String recommendationResultChannel;

    @Bean
    public ChannelTopic recommendationResultTopic() {
        return new ChannelTopic(recommendationResultChannel);
    }

    @Bean
    public RedisMessageListenerContainer recommendationResultListenerContainer(
            @Qualifier("cacheRedisConnectionFactory") RedisConnectionFactory redisConnectionFactory,
            RecommendationResultSubscriber recommendationResultSubscriber,
            ChannelTopic recommendationResultTopic
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(recommendationResultSubscriber, recommendationResultTopic);
        return container;
    }
}
