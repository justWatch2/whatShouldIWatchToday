package avengers.waffle.controller.kakao;

import avengers.waffle.VO.kakao.KakaoDTO;
import avengers.waffle.VO.kakao.UuidDTO;
import avengers.waffle.configuration.security.oauth2.JwtProperties;
import avengers.waffle.service.IF.kakao.IF_KakaoService;
import avengers.waffle.utils.GetMemberId;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class KakaoController {
    private final IF_KakaoService kakaoService;
    private final GetMemberId getMemberId;

    @PostMapping("/api/invite/create")
    @ResponseBody
    public String createInviteUrl(HttpServletRequest request, @AuthenticationPrincipal UserDetails userDetails) {

        String authorizationHeader = request.getHeader("Authorization");
        String memberId =  getMemberId.getMemberId(authorizationHeader);
        return (kakaoService.createURL(memberId));
    }

    @PostMapping("/api/friend/invite")
    public ResponseEntity<?> checkInviteUrl(@RequestBody UuidDTO uuidDTO, HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");
        String memberId  =  getMemberId.getMemberId(authorizationHeader);
        String result = kakaoService.checkUuid(uuidDTO.getUuid(), memberId);
        //에러시 not_acceptable로 406에러를 발생시켜 구분시켰다 일부러
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
        return ResponseEntity.ok().build();
    }


}
