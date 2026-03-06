package avengers.waffle.configuration.redis;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
@Profile("web")
public class RedisPubSubConfig {

    @Value("${app.recommendation.redis-channel:recommendation.result}")
    private String resultChannel;

    @Bean
    public ChannelTopic resultTopic() {
        return new ChannelTopic(resultChannel);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            @Qualifier("cacheRedisConnectionFactory") RedisConnectionFactory redisConnectionFactory,
            ResultSubscriber resultSubscriber,
            ChannelTopic resultTopic
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(resultSubscriber, resultTopic);
        return container;
    }
}
