package avengers.waffle.configuration.messaging.worker;

import avengers.waffle.configuration.messaging.UserRecommendJobMessage;
import avengers.waffle.configuration.messaging.worker.woutbox.OutboxResult;
import avengers.waffle.configuration.messaging.worker.woutbox.OutboxResultRepository;
import avengers.waffle.dto.requestDTO.UserRecommendRequestDTO;
import avengers.waffle.dto.responseDTO.UserRecommendResponseDTO;
import avengers.waffle.service.recommendationService.UserRecommendService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Profile("worker")
@RequiredArgsConstructor
public class UserRecommendJobConsumer {

    private final UserRecommendService userRecommendService;
    private final ObjectMapper objectMapper;
    private final OutboxResultRepository outboxResultRepository;
    private final RedisTemplate<String, String> pubSubRedisTemplate;

    @Value("${app.recommendation.redis-channel:recommendation.result}")
    private String resultChannel;

    @RabbitListener(queues = "${app.recommendation.rabbit.queue:queue}")
    public void handle(UserRecommendJobMessage msg) throws Exception {
        UserRecommendRequestDTO req = UserRecommendRequestDTO.builder()
                .userId(msg.getUserId())
                .mediaType(msg.getMediaType())
                .region(msg.getRegion())
                .ageRating(msg.getAgeRating())
                .selectedGenres(msg.getSelectedGenres())
                .build();
        UserRecommendResponseDTO result = userRecommendService.memberRecommend(req);

        String json = objectMapper.writeValueAsString(result);

        OutboxResult outboxResult = OutboxResult.builder()
                .requestId(msg.getRequestId())
                .payload(json)
                .createdAt(LocalDateTime.now())
                .build();

        outboxResultRepository.save(outboxResult);

        //redis 채널에 publish
        pubSubRedisTemplate.convertAndSend(resultChannel, msg.getRequestId());

    }
}
