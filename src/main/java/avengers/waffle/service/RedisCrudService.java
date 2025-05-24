package avengers.waffle.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisCrudService {

    private final StringRedisTemplate redisTemplate;

    // 데이터 저장 (기본)
    public void save(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    // 데이터 저장 (TTL 설정)
    public void saveWithExpire(String key, String value, long timeoutInSeconds) {
        redisTemplate.opsForValue().set(key, value, timeoutInSeconds, TimeUnit.SECONDS);
    }

    // 데이터 조회
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // 데이터 업데이트
    public void update(String key, String newValue) {
        redisTemplate.opsForValue().set(key, newValue); // 기존 키에 다시 저장하면 덮어쓰기
    }

    // 데이터 삭제
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    // 존재 여부 확인
    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}

