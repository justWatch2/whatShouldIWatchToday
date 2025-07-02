package avengers.waffle.controller.refreshToken;

import avengers.waffle.VO.security.MemberVO;
import avengers.waffle.configuration.security.oauth2.JwtProperties;
import avengers.waffle.entity.Member;
import avengers.waffle.repository.posts.MovieMemberRepository;
import avengers.waffle.utils.GetMemberId;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Arrays;
import java.util.Date;

@Controller
@RequiredArgsConstructor
public class RefreshTokenController {

    private final JwtProperties jwtProperties;

    @Qualifier("tokenRedisTemplate")
    private final StringRedisTemplate stringRedisTemplate;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MovieMemberRepository movieMemberRepository;
    private final GetMemberId getMemberId;




    @GetMapping("/api/protected/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Protected test endpoint accessed!");
    }


    @PostMapping("/api/auth/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {


        System.out.println("refrsh controller 까지는 왔다!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        //1. 쿠키에서 refresh token 꺼내기
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            System.out.println(" 여기 들어왔니?쿠키큌");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("중간에 쿠키가 지워졌습니다.");
        }
        for (int i = 0; i < cookies.length; i++) {
            System.out.println("cookies[0].getValue() = " + cookies[i].getValue());
            System.out.println("cookies[0].getName() = " + cookies[i].getName());
        }

        String refreshToken = Arrays.stream(cookies)
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);

        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No refresh token found");
            // 로그인 페이지로 보낼수 있도록 따로 처리해두기
        }

        // 2. 유효성 검사 + Redis 비교
        // 무엇을 검사? -> refresh Token 이 만료되었는지? 또는 redis에 있는것과 같은 것인지?
        //    2-1	refreshToken 파싱 + 서명 유효성 검사

        System.out.println("토큰 파싱 부분 시작할떄 !!!!refreshToken = " + refreshToken);
        System.out.println("현재 JWT Secret = " + jwtProperties.getSecret());
        DecodedJWT jwt;   //토큰 유효성 검증
        try {
            jwt = JWT.require(Algorithm.HMAC512(jwtProperties.getSecret()))
                    .build()
                    .verify(refreshToken);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token");
        }

        String memberId = jwt.getClaim("memberId").asString();
        //마이페이지쪽 소셜로그인 구별 위해서
        String provider = movieMemberRepository.findByMemberId(memberId).getProvider();

        System.out.println("memberId = " + memberId);
        //    2-2	Redis에서 해당 유저의 토큰과 일치하는지 확인
        String value = stringRedisTemplate.opsForValue().get(memberId);
        if (value != null) {
            if (!refreshToken.equals(value)) {
                System.out.println(" 리플레시 토큰이 일치하지 않는다는 말 !");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이미 로그인된 계정입니다. 다른 아이디로 로그인해주세요.");
            }
        }
        System.out.println(" 토큰이 일치한다는 말!!");

        // 3. 새 access token 발급 후 응답
        //RSA방식은 아니고 hash암호 방식
        //    3-1	새로운 accessToken 발급
        String accessToken = JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + (jwtProperties.getExpirationTime())))  //토큰 만료시간 60000 기준 1분
                .withClaim("memberId", memberId)
                .withClaim("provider", provider)
                .sign(Algorithm.HMAC512(jwtProperties.getSecret()));

        //    3-2	(선택) refreshToken도 재발급 후 Redis 갱신
        String newRefreshToken = JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtProperties.getExpirationTime() * 60L))
                .withClaim("memberId", memberId)
                .sign(Algorithm.HMAC512(jwtProperties.getSecret()));

        // Redis에 갱신
        stringRedisTemplate.opsForValue().set(memberId, newRefreshToken);

        // 3-2.1: 기존 refreshToken 쿠키를 만료시킴
        Cookie[] existingCookies = request.getCookies();
        for (Cookie cookie : existingCookies) {
            if ("refreshToken".equals(cookie.getName())) {
                // 기존 refreshToken 쿠키를 만료시킴
                cookie.setMaxAge(0);
                cookie.setPath("/");
                cookie.setHttpOnly(true);
                cookie.setSecure(false);  // https 일 경우 true 설정
                response.addCookie(cookie);  // 만료된 쿠키를 응답에 추가하여 클라이언트에 삭제를 지시
                break;
            }
        }

        //    3-3	클라이언트에 전달 (헤더 or JSON or 쿠키)
        ResponseCookie newRefreshCookie = ResponseCookie.from("refreshToken", newRefreshToken)
                .httpOnly(true)
                .secure(false)     //true :  https 통신일 경우에만 쿠키 전송 false : http 통신일 경우에만 쿠키 전송
                .path("/")
                .maxAge(Duration.ofDays(7))   //JWT의 refreshToken 만료 시간과 쿠키의 maxAge를 동일
                .sameSite("Lax")  //  sameSite 설정을 해줘야지 csrf보안 처리를 안할수 있고
//                .secure(true) // HTTPS일 경우
                .build();

        response.addHeader("Set-Cookie", newRefreshCookie.toString());

        System.out.println("refreshToken 끝까지 도착 했다는 이야기!!");
        // accessToken은 Authorization 헤더에 담아서 응답
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + accessToken)
                .build();
    }

    @PostMapping("/api/logout")
    @ResponseBody
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        String authorizationHeader = request.getHeader("Authorization");

        System.out.println("로그아웃을 위해서 authorizationHeader = " + authorizationHeader);

        String memberId = getMemberId.getMemberId(authorizationHeader);

        stringRedisTemplate.delete(memberId);

        //카카오톡 서버에서 받아온 자체 accessToken 도 삭제
        String kakaoAccessToken = memberId + ":kakaoAccessToken";
        stringRedisTemplate.delete(kakaoAccessToken);

        //기존 쿠키들도 삭제한다
        // 3-2.1: 기존 refreshToken 쿠키를 만료시킴
        Cookie[] existingCookies = request.getCookies();
        for (Cookie cookie : existingCookies) {
            if ("refreshToken".equals(cookie.getName())) {
                // 기존 refreshToken 쿠키를 만료시킴
                cookie.setMaxAge(0);
                cookie.setPath("/");
                cookie.setHttpOnly(true);
                cookie.setSecure(false);  // https 일 경우 true 설정
                response.addCookie(cookie);  // 만료된 쿠키를 응답에 추가하여 클라이언트에 삭제를 지시
                break;
            }
        }

        return ResponseEntity.ok().build();

    }

    @PostMapping("/api/join")
    public String join(@RequestBody MemberVO user) {
        System.out.println("잘하자");
        Member member = Member.builder()
                .memberId(user.getMemberId())
                .memberPw(bCryptPasswordEncoder.encode(user.getMemberPw())) // 소셜 로그인은 비번 없음
                .roles("ROLE_USER")
                .build();

        movieMemberRepository.save(member);
        return "redirect:/";
    }


}
