package avengers.waffle.utils;

import avengers.waffle.configuration.security.oauth2.JwtProperties;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class GetMemberId {
    private final JwtProperties jwtProperties;


    public String getMemberId(String authorizationHeader){

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return null;
        }
        String token = authorizationHeader.substring(7); // "Bearer " 이후의 토큰 부분
        String memberId;
        try {
            DecodedJWT jwt = JWT.require(Algorithm.HMAC512(jwtProperties.getSecret()))
                    .build()
                    .verify(token);

            memberId = jwt.getClaim("memberId").asString();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        return memberId;
    }

    public String getMemberId2(String authorizationHeader){

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return "none";
        }
        String token = authorizationHeader.substring(7); // "Bearer " 이후의 토큰 부분
        String memberId;
        try {
            DecodedJWT jwt = JWT.require(Algorithm.HMAC512(jwtProperties.getSecret()))
                    .build()
                    .verify(token);

            memberId = jwt.getClaim("memberId").asString();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        return memberId;
    }
}
