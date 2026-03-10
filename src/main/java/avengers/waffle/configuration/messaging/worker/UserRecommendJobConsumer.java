package avengers.waffle.configuration.messaging.worker;

import avengers.waffle.configuration.messaging.UserRecommendJobMessage;
import avengers.waffle.configuration.messaging.worker.woutbox.OutboxResult;
import avengers.waffle.configuration.messaging.worker.woutbox.OutboxResultRepository;
import avengers.waffle.dto.requestDTO.UserRecommendRequestDTO;
import avengers.waffle.dto.responseDTO.UserRecommendResponseDTO;
import avengers.waffle.service.recommendationService.UserRecommendService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Profile("worker")
@RequiredArgsConstructor
@Slf4j
public class UserRecommendJobConsumer {

    private final UserRecommendService userRecommendService;
    private final ObjectMapper objectMapper;
    private final OutboxResultRepository outboxResultRepository;
    private final RedisTemplate<String, String> pubSubRedisTemplate;

    @Value("${app.recommendation.redis-channel:recommendation.result}")
    private String resultChannel;

    @RabbitListener(queues = "${app.recommendation.rabbit.queue:queue}")
    public void handle(UserRecommendJobMessage msg) throws Exception {
        log.info("worker 메시지 수신! requestId: {}, memberId: {}, mediaType: {}",
                msg.getRequestId(), msg.getUserId(), msg.getMediaType());

        UserRecommendRequestDTO req = UserRecommendRequestDTO.builder()
                .userId(msg.getUserId())
                .mediaType(msg.getMediaType())
                .region(msg.getRegion())
                .ageRating(msg.getAgeRating())
                .selectedGenres(msg.getSelectedGenres())
                .build();

        UserRecommendResponseDTO result = userRecommendService.memberRecommend(req);
        log.info("worker 추천 계산 완료! requestId: {}", msg.getRequestId());

        String json = objectMapper.writeValueAsString(result);

        OutboxResult outboxResult = OutboxResult.builder()
                .requestId(msg.getRequestId())
                .payload(json)
                .createdAt(LocalDateTime.now())
                .build();

        outboxResultRepository.save(outboxResult);
        log.info("outbox_result 테이블에 저장! requestId: {}", msg.getRequestId());

        pubSubRedisTemplate.convertAndSend(resultChannel, msg.getRequestId());
        log.info("redis pub/sub 발행 완료! channel: {}, requestId: {}", resultChannel, msg.getRequestId());
    }
}
