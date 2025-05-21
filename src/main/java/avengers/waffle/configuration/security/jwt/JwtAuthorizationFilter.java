package avengers.waffle.configuration.security.jwt;

import avengers.waffle.configuration.security.auth.PrincipalDetails;
import avengers.waffle.entity.MovieMember;
import avengers.waffle.repository.MovieMemberRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

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

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, MovieMemberRepository movieMemberRepository) {

        super(authenticationManager);
        this.movieMemberRepository = movieMemberRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("인증이나 권한이 필요한 주소 요청이 됨");

        String jwtHeader = request.getHeader("Authorization");
        System.out.println("jwtHeader = " + jwtHeader);

        //header가 있는지 확인
        if(jwtHeader == null || !jwtHeader.startsWith("Bearer ")){
            //다음 필터로 요청을 넘깁니다.
           chain.doFilter(request,response);
           return ;
        }
        System.out.println("로그인 체크하는데 여기까지 온다고????????");
        //client에서 온 Jwt토큰을 검증해서 정상적인 사용자인지 확인
        String jwtToken = request.getHeader("Authorization").replace("Bearer ", "");
        String memberId = JWT.require(Algorithm.HMAC512("cos")).build().verify(jwtToken).getClaim("username").asString();

        //서명이 정상적으로 된경우
        if (memberId != null){
            System.out.println("username =====================================" + memberId);
            // 이부분에서 username이 db에 있으면 찾아지는거니까 인증이됨
            MovieMember userEntity = movieMemberRepository.findByMemberId(memberId);

            PrincipalDetails principalDetails = new PrincipalDetails(userEntity);

            //jwt 토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 만들어준다.
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
            // 강제로 시큐리티의 세션에 접근하여 Authentication객체를 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            //다음 필터로 요청을 넘깁니다.
            chain.doFilter(request,response);
        }

    }
}
