package avengers.waffle.configuration.security.jwt;

import avengers.waffle.configuration.security.auth.PrincipalDetails;

import avengers.waffle.configuration.security.oauth2.JwtProperties;
import avengers.waffle.entity.MovieMember;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.concurrent.TimeUnit;


@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final StringRedisTemplate stringRedisTemplate;
    private final JwtProperties jwtProperties;

    // 이 메서드는 jwt발급을 위해서 한번만 콜된다.
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("로그인 시도: JwtAuthenticationFilter");

        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        try {
            //username, password 받아서 처리한다.
            ObjectMapper mapper = new ObjectMapper();
            MovieMember user = mapper.readValue(request.getInputStream(), MovieMember.class);

//            if (stringRedisTemplate.opsForValue().get(user.getMemberId()) != null){
//                System.out.println(" 들어오는지 확인!!" );
//                throw new AuthenticationServiceException("이미 로그인된 계정입니다. 다른 아이디로 로그인하세요.");
//            }
            //토큰 생성
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getMemberId(), user.getMemberPw());

            //이부분이 실제로 인증이 수행되는 핵심
            //1. Spring Security는 내부적으로 UserDetailsService를 호출해 DB에서 유저 정보를 찾는다.
            //2.  아이디가 존재하고 비밀번호가 일치하면:
            //
            //인증에 성공한 Authentication 객체를 반환
            //
            //이 객체는 내부적으로 UserDetails를 포함하고 있고
            //
            //인증 상태: isAuthenticated == true
            //
            //실패하면 예외 (BadCredentialsException) 발생
            Authentication authentication = authenticationManager.authenticate(token);
            //로그인한 정보를 다시 불러올려는 방법
//            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
//            System.out.println(principalDetails.getUsername());

            //위 방식보다는 아래 방식이 더잘 쓰인다
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//            if (authentication != null && authentication.isAuthenticated()) {
//                Object principal = authentication.getPrincipal();
//
//                if (principal instanceof UserDetails userDetails) {
//                    String username = userDetails.getUsername();
//                    // 이제 username으로 필요한 로직 수행
//                }
//            }
            return authentication;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //attemAuthentication 실행 후 인증이 정상적으로 되었으면 아래의 메서드 실행된다.
    //Jwt 토큰을 만들어서 request 요청한 사용자에게 jwt토큰을 response해주면된다.


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("successfulAuthentication 인증이 완료되었다는 뜻");

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
        System.out.println("principalDetails.getUsername() = " + principalDetails.getUsername());
        //RSA방식은 아니고 hash암호 방식
        String accessToken = JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + (jwtProperties.getExpirationTime()/6)))  //토큰 만료시간 60000 기준 1분
//                .withClaim("id",principalDetails.getId())  //withClaim은 키 벨류값설
                .withClaim("memberId", principalDetails.getUsername())
                .sign(Algorithm.HMAC512(jwtProperties.getSecret()));
        //application.yml 혹은 환경변수에서 JWT_SECRET 값으로 관리합니다. 위에 이야기임

        //Bearer 뒤에 한칸뛰어야 한다
        response.addHeader("Authorization", "Bearer " + accessToken);

        //refresh Token 생성 부분
        String refreshToken = JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 14)))  //14일
//                .withExpiresAt(new Date(System.currentTimeMillis() + (jwtProperties.getExpirationTime() * 5L)))  //5분
                .withClaim("memberId", principalDetails.getUsername())
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
        stringRedisTemplate.opsForValue().set(principalDetails.getUsername(), refreshToken, 14, TimeUnit.DAYS);

        //Key:   username
        //Value: abcd1234...  (실제 JWT 리프레시 토큰)
        //TTL:   14일 (1209600초)    TimeUnit.DAYs -> 일 의미함!

        System.out.println("cookie.toString() = " + cookie.toString());
        // 응답 헤더에 넣기 클라이언트도 가지고 있어야 되기 때문임
        response.addHeader("Set-Cookie", cookie.toString());

//      // ✅ JSON으로 응답 본문도 함께 전달
//    response.setContentType("application/json");
//    response.setCharacterEncoding("UTF-8");
//    response.getWriter().write("{\"token\": \"Bearer " + jwtToken + "\"}");
    }
}
