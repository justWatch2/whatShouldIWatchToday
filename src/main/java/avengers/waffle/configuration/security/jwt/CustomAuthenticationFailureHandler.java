package avengers.waffle.configuration.security.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

//    @Override
//    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
//                                        AuthenticationException exception) throws IOException {
//
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.setContentType("application/json;charset=UTF-8");
//
//        Map<String, String> result = new HashMap<>();
//        result.put("error", exception.getMessage());
//
//        response.getWriter().write(objectMapper.writeValueAsString(result));
//    }
@Override
public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                    AuthenticationException exception) throws IOException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json;charset=UTF-8");

    Map<String, String> result = new HashMap<>();
    String msg = exception.getMessage().contains("Bad credentials")
            ? "아이디 또는 비밀번호가 잘못되었습니다."
            : exception.getMessage();

    result.put("message", msg);

    response.getWriter().write(objectMapper.writeValueAsString(result));
}

}

