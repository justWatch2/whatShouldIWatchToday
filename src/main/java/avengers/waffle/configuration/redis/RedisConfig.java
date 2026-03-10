package avengers.waffle.configuration.redis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    private RedisStandaloneConfiguration baseConfig() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisHost);
        config.setPort(redisPort);
        return config;
    }

    // --- Refresh Token 용 RedisTemplate (DB port 6379) ---
    @Bean
    public RedisConnectionFactory tokenRedisConnectionFactory() {
        return new LettuceConnectionFactory(baseConfig());
    }
    @Bean
    public StringRedisTemplate tokenRedisTemplate(@Qualifier("tokenRedisConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
        return new StringRedisTemplate(redisConnectionFactory);
    }

    // --- Search Cache 용 RedisTemplate (DB port 6380) ---
    @Bean
    public RedisConnectionFactory cacheRedisConnectionFactory() {
        return new LettuceConnectionFactory(baseConfig());
    }
//    @Bean
//    public RedisTemplate<String, Object> cacheRedisTemplate() {
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
//        template.setConnectionFactory(cacheRedisConnectionFactory());
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//        return template;
//    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(@Qualifier("cacheRedisConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    public RedisTemplate<String, String> pubSubRedisTemplate(
            @Qualifier("cacheRedisConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setValueSerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setHashValueSerializer(stringSerializer);
        template.setDefaultSerializer(stringSerializer);
        template.afterPropertiesSet();
        return template;
    }


}
