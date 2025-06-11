package avengers.waffle.controller.kakao;

import avengers.waffle.VO.kakao.KakaoDTO;
import avengers.waffle.VO.kakao.UuidDTO;
import avengers.waffle.configuration.security.oauth2.JwtProperties;
import avengers.waffle.service.kakao.KakaoService;
import avengers.waffle.utils.GetMemberId;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class KakaoController {
    private final KakaoService kakaoService;
    private final JwtProperties jwtProperties;
    private final GetMemberId getMemberId;


//    @PostMapping("/api/kakao/recommendFriends")
//    public ResponseEntity<KakaoDTO> findFriends(HttpServletRequest request){
//
//        //이거 메서드화 시켜서 불러와서 사용하기 너무 길다
//        String authorizationHeader = request.getHeader("Authorization");
//
//        String memberId =  getMemberId.getMemberId(authorizationHeader);
//
//        //친구 목록 리스트 불러오기
//        KakaoDTO result = kakaoService.findFriends(memberId);
//        System.out.println(" 네번째 오류?");
//        System.out.println("result = " + result);
//        return ResponseEntity.ok(result);
//    }

    @PostMapping("/api/invite/create")
    @ResponseBody
    public String createInviteUrl(HttpServletRequest request){

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
