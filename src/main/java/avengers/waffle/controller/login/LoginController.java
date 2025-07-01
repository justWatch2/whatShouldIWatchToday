package avengers.waffle.controller.login;

import avengers.waffle.VO.posts.MemberDTO;
import avengers.waffle.repository.posts.MovieMemberRepository;
import avengers.waffle.service.IF.login.IF_LoginService;
import avengers.waffle.utils.FileDataUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final IF_LoginService loginService;
    private final FileDataUtil fileDataUtil;
    private final MovieMemberRepository memberRepository;


    @GetMapping("/checkId")
    @ResponseBody
    public String checkId(@RequestParam String id) {
        System.out.println(id);
        boolean check = memberRepository.existsByMemberId(id);
        if (!check) {
            return "success";
        }
        return "fail";
    }


    @PostMapping("/signUp")
    @ResponseBody
    public String signUp(@ModelAttribute MemberDTO memberDTO, MultipartFile[] img) throws IOException {

        if (img != null && img[0] != null) {
            String[] url = fileDataUtil.fileUpload(img);
            loginService.saveMember(memberDTO, url[0]);
            return "success";
        }
        String imgRrl = "/static/images/logo.png";
        loginService.saveMember(memberDTO, imgRrl);
        return "success";
    }
}
