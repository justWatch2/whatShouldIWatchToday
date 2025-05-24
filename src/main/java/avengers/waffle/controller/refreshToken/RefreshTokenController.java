package avengers.waffle.controller.refreshToken;

import avengers.waffle.configuration.security.oauth2.JwtProperties;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.Duration;
import java.util.Arrays;
import java.util.Date;

@Controller
@RequiredArgsConstructor
public class RefreshTokenController {

    private final JwtProperties jwtProperties;
    private final StringRedisTemplate stringRedisTemplate;

    @PostMapping("/api/auth/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("controller 까지는 왔ㄷ다!!");
        //1. 쿠키에서 refresh token 꺼내기
        Cookie[] cookies = request.getCookies();
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
        }

        // 2. 유효성 검사 + Redis 비교
        // 무엇을 검사? -> refresh Token 이 만료되었는지? 또는 redis에 있는것과 같은 것인지?
        //    2-1	refreshToken 파싱 + 서명 유효성 검사

        System.out.println("토큰 파싱 부분 시작할떄 !!!!refreshToken = " + refreshToken);

        DecodedJWT jwt;   //토큰 유효성 검증
        try {
            jwt = JWT.require(Algorithm.HMAC512(jwtProperties.getSecret()))
                    .build()
                    .verify(refreshToken);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token");
        }

        String memberId = jwt.getClaim("memberId").asString();
        System.out.println("memberId = " + memberId);
        //    2-2	Redis에서 해당 유저의 토큰과 일치하는지 확인
        String value = stringRedisTemplate.opsForValue().get(memberId);
        if (value != null) {
            if(!refreshToken.equals(value)){
                System.out.println(" 리플레시 토큰이 일치하지 않는다는 말 !");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No refresh token found");
            }
        }
        System.out.println(" 토큰이 일치한다는 말!!" );

        // 3. 새 access token 발급 후 응답
        //RSA방식은 아니고 hash암호 방식
        //    3-1	새로운 accessToken 발급
        String accessToken = JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + (jwtProperties.getExpirationTime())))  //토큰 만료시간 60000 기준 1분
                .withClaim("memberId", memberId)
                .sign(Algorithm.HMAC512(jwtProperties.getSecret()));

        //    3-2	(선택) refreshToken도 재발급 후 Redis 갱신
        String newRefreshToken = JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtProperties.getExpirationTime()))
                .withClaim("memberId", memberId)
                .sign(Algorithm.HMAC512(jwtProperties.getSecret()));

        // Redis에 갱신
        stringRedisTemplate.opsForValue().set(memberId, newRefreshToken);

        //    3-3	클라이언트에 전달 (헤더 or JSON or 쿠키)
        ResponseCookie newRefreshCookie = ResponseCookie.from("refreshToken", newRefreshToken)
                .httpOnly(true)
                .secure(false)     //true :  https 통신일 경우에만 쿠키 전송 false : http 통신일 경우에만 쿠키 전송
                .path("/api/auth/refresh")
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



}
