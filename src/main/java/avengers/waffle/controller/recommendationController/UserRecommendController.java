package avengers.waffle.controller.recommendationController;



import avengers.waffle.dto.requestDTO.UserRecommendRequestDTO;
import avengers.waffle.dto.responseDTO.UserRecommendResponseDTO;
import avengers.waffle.service.recommendationService.UserRecommendService;
import avengers.waffle.utils.GetMemberId;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserRecommendController {
    private final UserRecommendService userRecommendService;
    private final GetMemberId getMemberId;

    @PostMapping("/userrec")
    public ResponseEntity<UserRecommendResponseDTO> userRecommendMovies(
            @RequestBody UserRecommendRequestDTO requestDTO , HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");
        System.out.println(authorizationHeader);
        String memberId = getMemberId.getMemberId(authorizationHeader);
        log.info("memberId:{}", memberId);
        requestDTO.setUserId(memberId);

        log.info("***** userRecommendMovies requestDTO: {}", requestDTO);
            return userRecommend(requestDTO);
    }


    private ResponseEntity<UserRecommendResponseDTO> userRecommend(UserRecommendRequestDTO requestDTO){
        UserRecommendResponseDTO response = userRecommendService.memberRecommend(requestDTO);
        log.info("response: {}", response);
        return ResponseEntity.ok(response);

    }

}
