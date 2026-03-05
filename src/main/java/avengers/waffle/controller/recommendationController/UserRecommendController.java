package avengers.waffle.controller.recommendationController;


import avengers.waffle.configuration.messaging.UserRecommendJobMessage;
import avengers.waffle.configuration.messaging.outbox.OutboxService;
import avengers.waffle.dto.requestDTO.UserRecommendRequestDTO;
import avengers.waffle.dto.responseDTO.UserRecommendAsyncResponseDTO;
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
    private final OutboxService outboxService;

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

    @PostMapping("/userrec/async")
    public ResponseEntity<UserRecommendAsyncResponseDTO> userRecommendMoviesAsync(
            @RequestBody UserRecommendRequestDTO requestDTO, HttpServletRequest request) throws Exception {
        String authorizationHeader = request.getHeader("Authorization");
        String memberId = getMemberId.getMemberId(authorizationHeader);
        requestDTO.setUserId(memberId);

        String requestId = java.util.UUID.randomUUID().toString();
        UserRecommendJobMessage msg = UserRecommendJobMessage.builder()
                .requestId(requestId)
                .userId(memberId)
                .mediaType(requestDTO.getMediaType())
                .region(requestDTO.getRegion())
                .ageRating(requestDTO.getAgeRating())
                .selectedGenres(requestDTO.getSelectedGenres())
                .build();

        outboxService.saveRecommendMessage(msg);



        return ResponseEntity.accepted()
                .body(UserRecommendAsyncResponseDTO.builder().requestId(requestId).build());
    }


    private ResponseEntity<UserRecommendResponseDTO> userRecommend(UserRecommendRequestDTO requestDTO){
        UserRecommendResponseDTO response = userRecommendService.memberRecommend(requestDTO);
        log.info("response: {}", response);
        return ResponseEntity.ok(response);

    }

}
