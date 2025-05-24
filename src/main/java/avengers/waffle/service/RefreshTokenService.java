package avengers.waffle.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final StringRedisTemplate redisTemplate;

    // 저장
    public void saveRefreshToken(String userId, String refreshToken) {
        redisTemplate.opsForValue().set("RT:" + userId, refreshToken, Duration.ofDays(7)); // TTL 7일
    }

    // 조회
    public String getRefreshToken(String userId) {
        return redisTemplate.opsForValue().get("RT:" + userId);
    }

    // 삭제
    public void deleteRefreshToken(String userId) {
        redisTemplate.delete("RT:" + userId);
    }

    // 검증
    public boolean isTokenValid(String userId, String refreshToken) {
        String stored = getRefreshToken(userId);
        return stored != null && stored.equals(refreshToken);
    }
}
