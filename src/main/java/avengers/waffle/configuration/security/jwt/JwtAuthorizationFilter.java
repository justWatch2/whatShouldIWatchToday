package avengers.waffle.configuration.security.jwt;

import avengers.waffle.configuration.security.auth.PrincipalDetails;
import avengers.waffle.configuration.security.oauth2.JwtProperties;
import avengers.waffle.entity.Member;
import avengers.waffle.repository.posts.MovieMemberRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;


//시큐리티가 filter를 가지고 있는데  그 필터중에서 BasicAuthenticationFilter라는 것이 있다.
//권한이나 인증이 필요한 특정한 주소를 요청했을 떄 위의 필터를 무조건 콜하게 되어있다.
//만약에 권한이 인증이 필요한 주소가 아니라면 이 필터를 거치지 않는다.

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private MovieMemberRepository movieMemberRepository;
    private final JwtProperties jwtProperties;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, MovieMemberRepository movieMemberRepository, JwtProperties jwtProperties) {

        super(authenticationManager);
        this.movieMemberRepository = movieMemberRepository;
        this.jwtProperties = jwtProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("인가 시작!!!");

        String jwtHeader = request.getHeader("Authorization");
        System.out.println("jwtHeader = " + jwtHeader);

        //refresh Token을 위해서
        String uri = request.getRequestURI();

        // refresh API는 제외
        if (uri.equals("/api/auth/refresh")) {
            chain.doFilter(request, response);
            return;
        }

        //header가 있는지 확인
        if (jwtHeader == null || !jwtHeader.startsWith("Bearer ")) {
            //다음 필터로 요청을 넘깁니다.
            chain.doFilter(request, response);
            return;
        }

//        //client에서 온 Jwt토큰을 검증해서 정상적인 사용자인지 확인
//        String jwtToken = request.getHeader("Authorization").replace("Bearer ", "");
//        String memberId = JWT.require(Algorithm.HMAC512("cos")).build().verify(jwtToken).getClaim("username").asString();

        //redis 떄문에 처리함
        // client에서 온 Jwt토큰을 검증해서 정상적인 사용자인지 확인
        String jwtToken = jwtHeader.replace("Bearer ", "");
        String memberId = null;
        try {
            memberId = JWT.require(Algorithm.HMAC512(jwtProperties.getSecret()))
                    .build()
                    .verify(jwtToken)
                    .getClaim("memberId").asString();
        } catch (TokenExpiredException e) {
            // 토큰 만료시 401 에러 + 에러 메시지
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":\"EXPIRED_ACCESS_TOKEN\", \"message\":\"AccessToken has expired.\"}");
            response.getWriter().flush();
            return;
        } catch (Exception e) {
            // 토큰 변조 또는 기타 오류 → 403
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":\"INVALID_TOKEN\", \"message\":\"Invalid or tampered token.\"}");
            response.getWriter().flush();
            return;
        }
        //서명이 정상적으로 된경우
        if (memberId != null) {
            System.out.println(" 인가쪽 제대로 시행된다는거지");
            System.out.println("memberId = " + memberId);
            // 이부분에서 username이 db에 있으면 찾아지는거니까 인증이됨
            Member userEntity = movieMemberRepository.findByMemberId(memberId);

            PrincipalDetails principalDetails = new PrincipalDetails(userEntity);

            //jwt 토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 만들어준다.
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
            // 강제로 시큐리티의 세션에 접근하여 Authentication객체를 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            //다음 필터로 요청을 넘깁니다.
            chain.doFilter(request, response);
        }

    }
}
