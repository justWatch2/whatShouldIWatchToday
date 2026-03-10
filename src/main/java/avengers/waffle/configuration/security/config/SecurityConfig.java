package avengers.waffle.configuration.security.config;

import avengers.waffle.configuration.security.handler.OAuth2SuccessHandler;
import avengers.waffle.configuration.security.jwt.CustomAuthenticationFailureHandler;
import avengers.waffle.configuration.security.jwt.JwtAuthenticationEntryPoint;
import avengers.waffle.configuration.security.jwt.JwtAuthenticationFilter;
import avengers.waffle.configuration.security.jwt.JwtAuthorizationFilter;
import avengers.waffle.configuration.security.oauth2.CustomOAuth2UserService;
import avengers.waffle.configuration.security.oauth2.JwtProperties;
import avengers.waffle.repository.posts.MovieMemberRepository;
import avengers.waffle.utils.GetUrlToImage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final MovieMemberRepository movieMemberRepository;
    private final JwtProperties jwtProperties;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final GetUrlToImage getUrlToImage;

    @Qualifier("tokenRedisTemplate")
    private final StringRedisTemplate stringRedisTemplate;

    @Value("${app.cors.allowed-origins:*}")
    private String allowedOrigins;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }

    @Bean
    public CustomOAuth2UserService customOAuth2UserService() {
        return new CustomOAuth2UserService(movieMemberRepository, getUrlToImage);
    }

    @Bean
    public OAuth2SuccessHandler oAuth2SuccessHandler() {
        return new OAuth2SuccessHandler(jwtProperties, stringRedisTemplate);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter =
                new JwtAuthenticationFilter(authenticationManager, stringRedisTemplate, jwtProperties);
        jwtAuthenticationFilter.setFilterProcessesUrl("/api/login");
        jwtAuthenticationFilter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilter(jwtAuthenticationFilter)
                .addFilter(new JwtAuthorizationFilter(authenticationManager, movieMemberRepository, jwtProperties))
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/favicon.ico").permitAll()
                        .requestMatchers("/user/**").authenticated()
                        .requestMatchers("/manager/**").hasAnyRole("MANAGER", "ADMIN")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers(
                                "/api/auth/refresh", "/css/**", "/js/**", "/images/**",
                                "/join", "/api/join", "/api/non-member/**", "/static/**",
                                "/api/checkId", "/api/signUp", "/api/getProfileImg", "/img/**", "/rec/**",
                                "/ws/**", "/actuator/**",
                                "/api/checkName"
                        ).permitAll()
                        .requestMatchers("/").permitAll()
                        .requestMatchers("*").permitAll()
                        .requestMatchers("/*").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService()))
                        .successHandler(oAuth2SuccessHandler())
                );
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        for (String origin : allowedOrigins.split(",")) {
            String trimmed = origin.trim();
            if (!trimmed.isEmpty()) {
                configuration.addAllowedOriginPattern(trimmed);
            }
        }
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        configuration.addExposedHeader("Authorization");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
