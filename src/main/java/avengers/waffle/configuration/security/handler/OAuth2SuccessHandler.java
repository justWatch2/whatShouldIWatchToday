package avengers.waffle.configuration.security.handler;

import avengers.waffle.configuration.security.auth.PrincipalDetails;
import avengers.waffle.configuration.security.oauth2.JwtProperties;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.concurrent.TimeUnit;


@RequiredArgsConstructor
public class    OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProperties jwtProperties;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        String username = principalDetails.getUsername();
        String provider = principalDetails.getProvider();

        String jwtToken = JWT.create()
                .withSubject(username)
                .withClaim("memberId", username)
                .withClaim("provider",provider)
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtProperties.getExpirationTime()))
                .sign(Algorithm.HMAC512(jwtProperties.getSecret()));

        //
        //refresh Token 생성 부분
        String refreshToken = JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 14)))  //14일
//                .withExpiresAt(new Date(System.currentTimeMillis() + (jwtProperties.getExpirationTime() * 5L)))  //5분
                .withClaim("memberId", username)
                .sign(Algorithm.HMAC512(jwtProperties.getSecret()));

        // refresh Token 쿠키로 보내는 부분
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false)     //true :  https 통신일 경우에만 쿠키 전송 false : http 통신일 경우에만 쿠키 전송
                .path("/")
                .maxAge(Duration.ofDays(7)) //JWT의 refreshToken 만료 시간과 쿠키의 maxAge를 동일
                .sameSite("Lax")  //  sameSite 설정을 해줘야지 csrf보안 처리를 안할수 있고
//                .sameSite("None")
//                .secure(true) // HTTPS일 경우
                .build();
        //자동 전송 조건 정리:
        //1 요청 경로가 /api/auth/refresh 또는 그 하위일 것
        //2 도메인이 일치할 것 (예: api.example.com)
        //3 .httpOnly(true)면 JavaScript에서는 못 읽지만, 브라우저가 자동으로 쿠키를 전송함

        // refresh Token redis에 저장
        stringRedisTemplate.opsForValue().set(username, refreshToken, 14, TimeUnit.DAYS);

        //Key:   username
        //Value: abcd1234...  (실제 JWT 리프레시 토큰)
        //TTL:   14일 (1209600초)    TimeUnit.DAYs -> 일 의미함!


        // ✅ accessToken이 있으면 저장 (카카오 only)
        if (principalDetails.getAttributes().get("access_token") != null) {
            String kakaoAccessToken = (String) principalDetails.getAttributes().get("access_token");
            // 일반적으로 카카오 accessToken은 6시간 유효
            stringRedisTemplate.opsForValue().set(username + ":kakaoAccessToken", kakaoAccessToken, 6, TimeUnit.HOURS);
        }

        //팝업창으로 안하고 일반적으로 하는 방법!!
//        System.out.println("cookie.toString() = " + cookie.toString());
//        // 응답 헤더에 넣기 클라이언트도 가지고 있어야 되기 때문임
//        response.addHeader("Set-Cookie", cookie.toString());
//
////        // 프론트엔드로 리다이렉트하면서 토큰 전달  그리고 3000으로해놨음 리엑트 서버떄문에
////        String redirectUrl = "http://localhost:3000/?token=" + "Bearer " + jwtToken;
//
//        //Bearer 뒤에 한칸뛰어야 한다
//        response.addHeader("Authorization", "Bearer " + jwtToken);
//
//        // 프론트엔드로 리다이렉트하면서 토큰 전달  그리고 3000으로해놨음 리엑트 서버떄문에
//        String redirectUrl = "http://localhost:3000/";
//        response.sendRedirect(redirectUrl);
        response.setContentType("text/html;charset=UTF-8");
        response.addHeader("Set-Cookie", cookie.toString());

        // **팝업에서 부모창에 메시지 보내고 팝업 닫기 스크립트**
        String script = "<script>" +
                "window.opener.postMessage({" +
                "token: 'Bearer " + jwtToken + "'" +
                "}, '*');" +
                "window.close();" +
                "</script>";

        response.getWriter().write(script);
        response.getWriter().flush();


    }
}

