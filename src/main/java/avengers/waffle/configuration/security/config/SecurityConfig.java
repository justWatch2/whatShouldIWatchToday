package avengers.waffle.configuration.security.config;



import avengers.waffle.configuration.security.handler.OAuth2SuccessHandler;
import avengers.waffle.configuration.security.jwt.JwtAuthenticationFilter;
import avengers.waffle.configuration.security.jwt.JwtAuthorizationFilter;
import avengers.waffle.configuration.security.oauth2.CustomOAuth2UserService;
import avengers.waffle.configuration.security.oauth2.JwtProperties;
import avengers.waffle.repository.MovieMemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final MovieMemberRepository movieMemberRepository;
    private final JwtProperties jwtProperties;

    //Spring Boot 3.x 이상에서는 AuthenticationManager를 직접 빌드해서 넣어줘야 필터에서 사용할 수 있음.
    //@Bean으로 분리해 두면 다른 클래스에서도 재사용 가능해서 더 좋다.
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }
    @Bean
    public CustomOAuth2UserService customOAuth2UserService() {
        return new CustomOAuth2UserService(movieMemberRepository);
    }

    @Bean
    public OAuth2SuccessHandler oAuth2SuccessHandler() {
        return new OAuth2SuccessHandler(jwtProperties);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {

        // JwtAuthenticationFilter 설정
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager);
        jwtAuthenticationFilter.setFilterProcessesUrl("/api/login"); // <- 이거 안 하면 필터가 동작 안 함 +  클라이언트 쪽에서
        //로그인을 할려고 /api/login으로 요청으로 보내면 필터가 작동할수 있도록 하는 메서드이다.

        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilter(jwtAuthenticationFilter)  // 이부분이 처음 로그인할때만 동작한다.
                .addFilter(new JwtAuthorizationFilter(authenticationManager, movieMemberRepository))
                .exceptionHandling(exception -> exception     // 예외처리 로그인
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("{\"error\": \"Unauthorized\"}");
                            //로그인 안되어있는데 로그인부분에 접근할려고 할시 다음과 같이 리다이랙션 해주면된다.
                            //response.sendRedirect("/");  // "/" 이거  말고 로그인 페이지로 알아서 바꾸면된다.
                        })
                )
                .authorizeHttpRequests(auth-> auth
                                .requestMatchers("/favicon.ico").permitAll() // ✅ favicon.ico 허용
                                .requestMatchers("/user/**").authenticated()  // 이부분은 사용자가 jwt토큰을 들고 요청하면 인증만되어있다고 확인이 된다면 통과 시키는 부분
                        .requestMatchers("/manager/**").hasAnyRole("MANAGER","ADMIN")  // 여러 권한중 하나만 있어도 허용
                        .requestMatchers("/admin/**").hasRole("ADMIN")  //  특정 하나의 권한만 허용

                        .requestMatchers( "/css/**", "/js/**", "/images/**", "/join", "/login2.html").permitAll() // 로그인 페이지, 정적 파일은 모두 허용
                                .requestMatchers("/").permitAll() // 기본 홈 페이지도 허용
                                .anyRequest().authenticated() // 나머지 요청은 인증이 필요
                        //             .anyRequest().permitAll()  이건 가장마지막에 위치해야된다.
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService())
                        )
                        .successHandler(oAuth2SuccessHandler())
//                        .defaultSuccessUrl("/", true) // 로그인 성공 후 이동할 페이지

                );
        return http.build();
    }

}
