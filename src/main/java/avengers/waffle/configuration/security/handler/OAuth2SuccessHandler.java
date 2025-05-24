package avengers.waffle.configuration.security.handler;

import avengers.waffle.configuration.security.auth.PrincipalDetails;
import avengers.waffle.configuration.security.oauth2.JwtProperties;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


import java.io.IOException;
import java.util.Date;


@RequiredArgsConstructor
public class    OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProperties jwtProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        String jwtToken = JWT.create()
                .withSubject(principalDetails.getUsername())
                .withClaim("username", principalDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtProperties.getExpirationTime()))
                .sign(Algorithm.HMAC512(jwtProperties.getSecret()));

        System.out.println("successHandler로 들어간다!");
        // 프론트엔드로 리다이렉트하면서 토큰 전달  그리고 3000으로해놨음 리엑트 서버떄문에
        String redirectUrl = "http://localhost:3000/?token=" + "Bearer " + jwtToken;

        response.sendRedirect(redirectUrl);
    }
}

