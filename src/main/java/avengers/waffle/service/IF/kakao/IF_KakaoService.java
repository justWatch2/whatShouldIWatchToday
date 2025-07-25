package avengers.waffle.service.IF.kakao;

import avengers.waffle.entity.Member;
import avengers.waffle.repository.posts.MovieMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


public interface IF_KakaoService {

    public String createURL(String memberId);

    public String checkUuid(String uuid, String memberId);

    //카카오 자동 친구 추가 파트
    public Map<String, String> getNicknamesByUuids(List<String> uuids);

    //카카오 친구 거절
    public void rejectUuid(String uuid, String memberId);

}
