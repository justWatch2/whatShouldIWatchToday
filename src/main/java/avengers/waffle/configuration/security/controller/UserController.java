package avengers.waffle.configuration.security.controller;

import avengers.waffle.configuration.security.auth.PrincipalDetails;
import avengers.waffle.entity.Member;
import avengers.waffle.repository.posts.MovieMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final MovieMemberRepository movieMemberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/")
    public String index(){
        return "index";
    }


    @GetMapping("/profile")
    @ResponseBody
    public String profile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails userDetails) {
                String username = userDetails.getUsername();
                System.out.println("!!!!!!!!!!!!!!!!!!!!username = " + username);
            }
        }
        return "ok";
    }


    @PostMapping("/join")
    public String join(@RequestBody Member user){
        Member member = new Member();
        member = Member.builder()
                .memberId(user.getMemberId())
                .memberPw(bCryptPasswordEncoder.encode(user.getMemberPw())) // 소셜 로그인은 비번 없음
                .roles("ROLE_USER")
                .build();
        movieMemberRepository.save(member);
        return "redirect:/";
    }

    // 로그인된 사용자 check
    @PostMapping("/user/check")
    @ResponseBody
    public String getUser(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails == null) {
            System.out.println("들어오나?");
            throw new RuntimeException("사용자 인증 정보가 없습니다.");
        }
        return "ok";
    }

    // 클라이언트가 JWT를 삭제하도록 안내
    @PostMapping("/logout")
    @ResponseBody
    public String logout() {

        return "클라이언트에서 JWT 삭제 필요";
    }
}