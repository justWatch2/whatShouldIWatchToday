package avengers.waffle.service.kakao;

import avengers.waffle.VO.kakao.KakaoDTO;
import avengers.waffle.entity.MovieMember;
import avengers.waffle.repository.MovieMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class KakaoService {

    private final StringRedisTemplate stringRedisTemplate;
    private final RestTemplate restTemplate;
    private final MovieMemberRepository movieMemberRepository;

    public KakaoDTO findFriends(String memberId) {

        String kakaoAccessToken  = stringRedisTemplate.opsForValue().get(memberId + ":kakaoAccessToken");


        //HttpHeaders -> javaspring프레임워크 3번째 것으로 import
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(kakaoAccessToken);
        headers.set("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<KakaoDTO> response = restTemplate.exchange("https://kapi.kakao.com/v1/api/talk/friends", HttpMethod.GET, entity, KakaoDTO.class);

        return response.getBody();
    }

    public String createURL(String memberId) {
        String uuid = UUID.randomUUID().toString();

        stringRedisTemplate.opsForValue().set("invite:" + uuid, memberId, 7, TimeUnit.DAYS);

        //이부분에 링크 주소를 바꿔주면된다.
        String inviteUrl = "http://localhost:3000/?uuid=" + uuid;
        return inviteUrl;
    }

    public String checkUuid(String uuid, String memberId) {
        System.out.println("초대 링크 받고 나서 사이트 접속시 uuid  = " + uuid + " memberId = " + memberId);

        // 1. uuid check redis에서
        String redisKey = "invite:" + uuid;
        String inviterId = stringRedisTemplate.opsForValue().get(redisKey);

        System.out.println("링크통해서 초대한사람id = " + inviterId);



        if (inviterId == null) {
            return null;
        }


        // 3. 나랑 친구가 되어 있는지 그친구가 나랑 친구 되어 있는지 체크
        // 우선 초대한사람 아이디 있는지 부터 체크후
        MovieMember inviter = movieMemberRepository.findByMemberId(inviterId);
        if (inviter == null) {
            System.out.println(" 초대한 사람이 없어요!!");
            stringRedisTemplate.delete(redisKey);
            return null;
        }


        // 친구로 되어있는지 체크해야된다.  초대한사람 친구 목록에 초대받은 사람이 있는지 체크!
        List<String> friendList = inviter.getFriendList();
        if (friendList != null && friendList.contains(memberId)){
            System.out.println("이미 친구 입니다.");
            stringRedisTemplate.delete(redisKey);
            return null;
        }

        // 4. 친구 추가하기
        //초대한 친구 목록에 추가
        inviter.getFriendList().add(memberId);
        movieMemberRepository.save(inviter);
        //초대받은 친구 목록에 추가
        MovieMember member = movieMemberRepository.findByMemberId(memberId);
        member.getFriendList().add(inviterId);
        movieMemberRepository.save(member);

        // 2. redis에 있는 uuid 지우기 ( 어차피 어떤경우든 간에 지운다)
        stringRedisTemplate.delete(redisKey);

        return  ResponseEntity.ok("success").getBody();
    }


}
