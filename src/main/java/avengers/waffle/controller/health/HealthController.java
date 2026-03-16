package avengers.waffle.controller.health;

import avengers.waffle.entity.Member;
import avengers.waffle.repository.posts.MovieMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class HealthController {

    private final MovieMemberRepository movieMemberRepository;

    @GetMapping("/api/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/api/non-member/healthcheck2")
    public ResponseEntity<Map<String, Object>> healthcheck2() {
        long started = System.currentTimeMillis();
        Member member = movieMemberRepository.findByMemberId("bb");
        long elapsedMs = System.currentTimeMillis() - started;

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("ok", true);
        body.put("exists", member != null);
        body.put("memberId", member != null ? member.getMemberId() : null);
        body.put("dbElapsedMs", elapsedMs);
        return ResponseEntity.ok(body);
    }
}
