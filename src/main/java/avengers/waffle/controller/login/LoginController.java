package avengers.waffle.controller.login;

import avengers.waffle.VO.login.MemberDTO;
import avengers.waffle.repository.posts.MovieMemberRepository;
import avengers.waffle.service.IF.login.IF_LoginService;
import avengers.waffle.utils.FileDataUtil;
import avengers.waffle.utils.GetMemberId;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class LoginController {

    private final IF_LoginService loginService;
    private final FileDataUtil fileDataUtil;
    private final MovieMemberRepository memberRepository;
    private final GetMemberId getMemberId;

    @PostMapping("/signUp")
    @ResponseBody
    public String signUp(@ModelAttribute MemberDTO memberDTO, MultipartFile[] img) throws IOException {
        if (img != null && img[0] != null) {
            String[] imgName = fileDataUtil.fileUpload(img);
//            String url = "/img/download?filename=" + imgName[0];
            System.out.println(memberDTO.getPass());
            loginService.saveMember(memberDTO, imgName[0]);
            return "success";
        }
//        String imgRrl = "/static/images/logo.png";
        loginService.saveMember(memberDTO, null);
        return "success";
    }

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

    @GetMapping("/checkName")
    @ResponseBody
    public String checkName(@RequestParam String name) {
        System.out.println(name);
        boolean check = memberRepository.existsByMemberName(name);
        if (!check) {
            return "success";
        }
        return "fail";
    }

    @GetMapping("/getProfileImg")
    @ResponseBody
    public String getProfileImg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = getMemberId.getMemberId(request.getHeader("Authorization"));
        if(id == null){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return "fail";
        }
        return loginService.getImgUrl(id);
    }
}
