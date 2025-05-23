package avengers.waffle.configuration.security.jwt;

import avengers.waffle.configuration.security.auth.PrincipalDetails;

import avengers.waffle.entity.MovieMember;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;


@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;


    // 이 메서드는 jwt발급을 위해서 한번만 콜된다.
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException{
        System.out.println("로그인 시도: JwtAuthenticationFilter");

        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }


        try {


            //username, password 받아서 처리한다.
            ObjectMapper mapper = new ObjectMapper();
            MovieMember user = mapper.readValue(request.getInputStream(),MovieMember.class);

            //토큰 생성
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getMemberId(),user.getMemberPw());

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
        }catch (IOException e){
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
        String jwtToken =  JWT.create()
//        Secret Key는 외부에 노출하지 않습니다. 바로 밑에 "cos토큰"이라고 하는거처럼?
                .withSubject("cos토큰")
                .withExpiresAt(new Date(System.currentTimeMillis() + (60000*10)))  //토큰 만료시간 60000 기준 1분
//                .withClaim("id",principalDetails.getId())  //withClaim은 키 벨류값설
                .withClaim("username",principalDetails.getUsername())
                .sign(Algorithm.HMAC512("cos"));
        //application.yml 혹은 환경변수에서 JWT_SECRET 값으로 관리합니다. 위에 이야기임
        //Bearer 뒤에 한칸뛰어야 한다
        response.addHeader("Authorization", "Bearer " + jwtToken);

//      // ✅ JSON으로 응답 본문도 함께 전달
//    response.setContentType("application/json");
//    response.setCharacterEncoding("UTF-8");
//    response.getWriter().write("{\"token\": \"Bearer " + jwtToken + "\"}");


    }
}
