package avengers.waffle.controller.posts;

import avengers.waffle.VO.util.PageVO;
import avengers.waffle.VO.posts.PostVO;
import avengers.waffle.VO.posts.ReplyVO;
import avengers.waffle.controller.util.FileDataUtil;
import avengers.waffle.service.IF.posts.IF_GullService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class GullController {

    private final IF_GullService GullService;
    private final FileDataUtil fileDataUtil;

    @GetMapping("/api/Gullist")
    @ResponseBody
    public Map<String, Object> Gullist(@RequestParam int page, @RequestParam String category) {
        List<PostVO> list = GullService.getPostList(page, category);
        PageVO pageVO = GullService.getPage(page, category);
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("pageVO", pageVO);
        return map;
    }

    @GetMapping("/api/GullDetail")
    @ResponseBody
    public Map<String, Object> GullDetail(@RequestParam int no) {
        PostVO post = GullService.getPost(no);
        List<ReplyVO> replyList = GullService.getReplyList(no);
        Map<String, Object> map = new HashMap<>();
        map.put("post", post);
        map.put("replyList", replyList);
        return map;
    }

    @PostMapping(value = "/api/posts")
    public String Gullwrite(@ModelAttribute PostVO post, MultipartFile[] fileUrl) throws IOException {
        String[] fileName=fileDataUtil.fileUpload(fileUrl);
        GullService.addPost(post,fileName);
        return "success";
    }
}
