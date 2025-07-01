package avengers.waffle.configuration.security.oauth2;


import avengers.waffle.configuration.security.auth.PrincipalDetails;

import avengers.waffle.configuration.security.oauth2.provider.*;
import avengers.waffle.entity.Member;
import avengers.waffle.repository.posts.MovieMemberRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MovieMemberRepository movieMemberRepository;

    public CustomOAuth2UserService(MovieMemberRepository movieMemberRepository) {
        this.movieMemberRepository = movieMemberRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("getClientRegistration.getRegistrationId() : " + userRequest.getClientRegistration().getRegistrationId()); // registrationId로 어떤 OAuth로 로그인했는지 확인가능.


        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 구글로그인 버튼 클릭 -> 구글로그인창 -> 로그인 완료 -> code를 리턴(OAuth-Client라이브러리) -> AccessToken요청
        // userRequest 정보 -> loadUser함수 호출 -> 구글로부터 회원프로필 받아준다.
        System.out.println("getAttributes sdfsafsaffasdfdsa: " + oAuth2User.getAttributes());

        //회원가입을 강제로 진행해볼 예정
        OAuth2UserInfo oAuth2UserInfo = null;
        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            System.out.println("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
            System.out.println("페이스북 로그인 요청");
            oAuth2UserInfo = new FacebookUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            System.out.println("네이버 로그인 요청");
            oAuth2UserInfo = new NaverUserInfo((Map) oAuth2User.getAttributes().get("response"));
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
            System.out.println("카카오 로그인 요청");
            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        } else {
            System.out.println("우리는 구글과 페이스북과 메이버만 지원합니다.");
        }


        //<바뀐 버젼>
        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getEmail();
        String memberId = provider + "_" + providerId;
        String role = "ROLE_USER";


        //db에 유저정보가 있는지 확인
        Member user = movieMemberRepository.findByMemberId(memberId);
        if (user == null) {
            user = Member.builder()
                    .memberId(memberId)
                    .memberPw("") // 소셜 로그인은 비번 없음
                    .roles(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            movieMemberRepository.save(user);
        }

        // 카카오서버 자체에서 오는 accessToken 저장하기
        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
        String accessToken = userRequest.getAccessToken().getTokenValue();
        attributes.put("access_token", accessToken);
        System.out.println("-------------------------------accessToken = " + accessToken);


        return (OAuth2User) new PrincipalDetails(user, attributes);
    }
}
