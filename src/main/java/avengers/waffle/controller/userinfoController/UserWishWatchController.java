package avengers.waffle.controller.userinfoController;


import avengers.waffle.dto.requestDTO.ContentRequestDTO;
import avengers.waffle.dto.responseDTO.ContentResponseDTO;
import avengers.waffle.dto.responseDTO.PageResponseDto;
import avengers.waffle.dto.searchDTO.WishWatchSearchDTO;
import avengers.waffle.service.userWatchWishService.WishWatchService;
import avengers.waffle.utils.GetMemberId;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member")
public class UserWishWatchController {

    private final GetMemberId getMemberId;
    private final WishWatchService wishWatchService;

    @GetMapping("/contents")
    public ResponseEntity<PageResponseDto<ContentResponseDTO>> getMyContents(
            @ModelAttribute ContentRequestDTO requestDto,
            HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String memberId = getMemberId.getMemberId(authorizationHeader);
        log.info("memberId = " + memberId);
        System.out.println("1231231231232123");
        WishWatchSearchDTO searchDTO = WishWatchSearchDTO.builder()
                .memberId(memberId)
                .status(requestDto.getStatus())
                .type(requestDto.getType())
                .cursor(requestDto.getCursor())
                .size(requestDto.getSize())
                .build();

        PageResponseDto<ContentResponseDTO> responseData = wishWatchService.getContents(searchDTO);
        return ResponseEntity.ok(responseData);
    }
}