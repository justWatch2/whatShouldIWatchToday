package avengers.waffle.controller.userinfoController;

import avengers.waffle.dto.userDTO.FriendDTO;
import avengers.waffle.dto.userDTO.MyProfileResponseDTO;
import avengers.waffle.dto.userDTO.ProfileUpdateRequestDTO;
import avengers.waffle.entity.Member;
import avengers.waffle.repository.mypageRepository.If_MypageService;
import avengers.waffle.repository.posts.MovieMemberRepository;
import avengers.waffle.service.impl.user.FriendService;
import avengers.waffle.utils.GetMemberId;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class UserInfoController {

    private final GetMemberId getMemberId;
    private final If_MypageService mypageService;
    private final FriendService friendService;
    private final MovieMemberRepository movieMemberRepository;

    @GetMapping("/profile")
    public ResponseEntity<MyProfileResponseDTO> getMyProfile(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String memberId = getMemberId.getMemberId(authorizationHeader);
        MyProfileResponseDTO myProfile = mypageService.getMyProfile(memberId);
        return ResponseEntity.ok(myProfile);
    }

    @PutMapping("/profile")
    public ResponseEntity<Map<String, Object>> updateMyProfile(
            @RequestPart("profileDto") ProfileUpdateRequestDTO requestDTO,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile,
            HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String memberId = getMemberId.getMemberId(authorizationHeader);
        System.out.println("!#!+@#+!#@+#!+@#+!@+$!+@$+!@%+!@+%!+@%+@@+%@%+%@+");
        System.out.println("!#!+@#+!#@+#!+@#+!@+$!+@$+!@%+!@+%!+@%+@@+%@%+%@+");
        System.out.println("!#!+@#+!#@+#!+@#+!@+$!+@$+!@%+!@+%!+@%+@@+%@%+%@+");

        System.out.println(memberId);

        mypageService.updateMyProfile(memberId, requestDTO, imageFile);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "프로필이 성공적으로 수정되었습니다.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/friends")
    public ResponseEntity<List<FriendDTO>> getFriends(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String memberId = getMemberId.getMemberId(authorizationHeader);
        System.out.println("1231231231231231231" + memberId);

        try {
            List<FriendDTO> friends = friendService.getFriends(memberId);
            System.out.println("===========================================12313182371298641987649127461897");
            System.out.println("friends size: " + friends.size());
            System.out.println("friends: " + friends);
            return ResponseEntity.ok(friends);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.emptyList()); // 에러 시 빈 리스트 반환
        }
    }

    @GetMapping("/memberName")
    public ResponseEntity<FriendDTO> getMemberName(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String memberId = getMemberId.getMemberId(authorizationHeader);

        Member member = movieMemberRepository.findByMemberId(memberId);
        String memberName = member.getMemberName();
        String img_url = member.getImgUrl();
        FriendDTO friendDTO = FriendDTO.builder()
                .memberId(null)
                .memberName(memberName)
                .imgUrl(img_url)
                .build();


        return ResponseEntity.ok(friendDTO);
    }
}