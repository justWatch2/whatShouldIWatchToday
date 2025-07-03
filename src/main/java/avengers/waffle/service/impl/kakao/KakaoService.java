package avengers.waffle.service.impl.kakao;

import avengers.waffle.entity.Member;
import avengers.waffle.repository.posts.MovieMemberRepository;
import avengers.waffle.service.IF.kakao.IF_KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class KakaoService implements IF_KakaoService {

    private final StringRedisTemplate stringRedisTemplate;
    private final MovieMemberRepository movieMemberRepository;

    public String createURL(String memberId) {
        String uuid = UUID.randomUUID().toString();
        System.out.println(memberId + " asssssafasdfafdafasdff");
        stringRedisTemplate.opsForValue().set("invite:" + uuid, memberId, 7, TimeUnit.DAYS);

        //이부분에 링크 주소를 바꿔주면된다.
        String inviteUrl = "http://localhost:3000/?uuid=" + uuid;
        return inviteUrl;
    }

    public Map<String, String> getNicknamesByUuids(List<String> uuids) {
        Map<String, String> result = new HashMap<>();

        for (String rawUuid : uuids) {
            // uuid= 부분 제거
            String redisKey = rawUuid.replaceFirst("^uuid=", "invite:");
            // Redis 키 포맷 맞춤

            System.out.println("이름 체크 할떄        ㅇㄴㄹㄴㅇㄹ redisKey = " + redisKey);
            String inviterId = stringRedisTemplate.opsForValue().get(redisKey);

            System.out.println("sdlakfjlakfsjalskfjlsdkjfalksklfj   inviterId = " + inviterId);
            if (inviterId != null) {
                result.put(rawUuid, inviterId);  // key는 원래의 uuid=포함된 값으로 넣어야 프론트에서 매칭됨
            } else {
                result.put(rawUuid, "알 수 없음");
            }
        }
        return result;
    }

    public String checkUuid(String uuid, String memberId) {
        System.out.println("초대 링크 받고 나서 사이트 접속시 uuid  = " + uuid + " memberId = " + memberId);

        String redisKey = uuid.replaceFirst("^uuid=", "invite:");
        // 1. uuid check redis에서

        System.out.println("이놈이 범인이야???      redisKey = " + redisKey);

        String inviterId = stringRedisTemplate.opsForValue().get(redisKey);

        System.out.println("링크통해서 초대한사람id = " + inviterId);

        if (inviterId == null) {
            return "reject";
        }

        // 3. 나랑 친구가 되어 있는지 그친구가 나랑 친구 되어 있는지 체크
        // 우선 초대한사람 아이디 있는지 부터 체크후
        Member inviter = movieMemberRepository.findByMemberId(inviterId);
        if (inviter == null) {
            System.out.println(" 초대한 사람이 없어요!!");
//            stringRedisTemplate.delete(redisKey);
            return null;
        }


        // 친구로 되어있는지 체크해야된다.  초대한사람 친구 목록에 초대받은 사람이 있는지 체크!
        List<String> friendList = inviter.getFriendList();
        if (friendList != null && friendList.contains(memberId)){
            System.out.println("이미 친구 입니다.");
//            stringRedisTemplate.delete(redisKey);
            return null;
        }

        // 4. 친구 추가하기
//        초대한 친구 목록에 추가
        inviter.addFriendList(memberId);
        movieMemberRepository.save(inviter);
        //초대받은 친구 목록에 추가
        Member member = movieMemberRepository.findByMemberId(memberId);
        member.addFriendList(inviterId);
        movieMemberRepository.save(member);

        // 2. redis에 있는 uuid 지우기 ( 어차피 어떤경우든 간에 지운다)
//        stringRedisTemplate.delete(redisKey);

        return  ResponseEntity.ok("success").getBody();
    }

    public void rejectUuid(String uuid, String memberId){
        String redisKey = "invite:" + uuid;
//        boolean deleted = stringRedisTemplate.delete(redisKey);

    }


}
