package avengers.waffle.configuration.messaging.worker;

import avengers.waffle.configuration.messaging.UserRecommendJobMessage;
import avengers.waffle.dto.requestDTO.UserRecommendRequestDTO;
import avengers.waffle.dto.responseDTO.UserRecommendResponseDTO;
import avengers.waffle.service.recommendationService.UserRecommendService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
@Profile("worker")
@RequiredArgsConstructor
@Slf4j
public class UserRecommendJobConsumer {

    private final UserRecommendService userRecommendService;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> pubSubRedisTemplate;
    private final MeterRegistry meterRegistry;
    private final WorkerOutboxTxService workerOutboxTxService;
    private final AtomicInteger inflightJobs = new AtomicInteger(0);

    @Value("${app.recommendation.redis-channel:recommendation.result}")
    private String resultChannel;

    @Value("${spring.rabbitmq.listener.simple.concurrency:1}")
    private int listenerConcurrency;

    @Value("${app.worker.processing.max-retry:3}")
    private int maxRetry;

    @PostConstruct
    void registerWorkerConsumerMetrics() {
        Gauge.builder("worker_job_consumer_inflight", inflightJobs, AtomicInteger::get)
                .description("Current in-flight worker jobs")
                .register(meterRegistry);

        Gauge.builder("worker_job_consumer_usage_pct", inflightJobs,
                        v -> (listenerConcurrency <= 0) ? 0.0 : (v.get() * 100.0) / listenerConcurrency)
                .description("Worker job consumer utilization percentage")
                .register(meterRegistry);
    }
    @RabbitListener(
            id = "userRecommendJobConsumer",
            queues = "${app.recommendation.rabbit.queue:queue}"
    )
    public void handle(UserRecommendJobMessage msg) {
        inflightJobs.incrementAndGet();
        try {
            log.info("worker 메시지 수신! requestId: {}, memberId: {}, mediaType: {}",
                    msg.getRequestId(), msg.getUserId(), msg.getMediaType());

            boolean acquired = workerOutboxTxService.tryMarkProcessing(msg.getRequestId());
            if (!acquired) {
                log.info("worker 처리 스킵(상태/존재 불일치) requestId: {}", msg.getRequestId());
                return;
            }

            UserRecommendRequestDTO req = UserRecommendRequestDTO.builder()
                    .userId(msg.getUserId())
                    .mediaType(msg.getMediaType())
                    .region(msg.getRegion())
                    .ageRating(msg.getAgeRating())
                    .selectedGenres(msg.getSelectedGenres())
                    .build();

            try {
                UserRecommendResponseDTO result = userRecommendService.memberRecommend(req);
                log.info("worker 추천 계산 완료! requestId: {}", msg.getRequestId());

                String json = objectMapper.writeValueAsString(result);
                boolean persisted = workerOutboxTxService.saveResultAndDeleteOutbox(msg.getRequestId(), json);
                if (!persisted) {
                    log.warn("worker 결과 저장 스킵(상태/존재 불일치) requestId: {}", msg.getRequestId());
                    return;
                }

                log.info("outbox_result 테이블에 저장! requestId: {}", msg.getRequestId());

                pubSubRedisTemplate.convertAndSend(resultChannel, msg.getRequestId());
                log.info("redis pub/sub 발행 완료! channel: {}, requestId: {}", resultChannel, msg.getRequestId());
            } catch (Exception e) {
                WorkerOutboxTxService.FailureResult failureResult =
                        workerOutboxTxService.markFailureForRetry(msg.getRequestId(), maxRetry);

                if (failureResult == WorkerOutboxTxService.FailureResult.RETRY_PENDING) {
                    log.warn("worker 처리 실패 -> PENDING 복구(재처리 대상) requestId: {}, maxRetry: {}",
                            msg.getRequestId(), maxRetry, e);
                } else if (failureResult == WorkerOutboxTxService.FailureResult.FAILED) {
                    log.error("worker 처리 실패 -> FAILED 처리(재시도 초과) requestId: {}, maxRetry: {}",
                            msg.getRequestId(), maxRetry, e);
                } else {
                    log.warn("worker 처리 실패 상태 반영 스킵(상태/존재 불일치) requestId: {}",
                            msg.getRequestId(), e);
                }
            }

        } finally {
            inflightJobs.decrementAndGet();
        }
    }
}
