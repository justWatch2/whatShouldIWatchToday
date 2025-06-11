package avengers.waffle.controller;

import avengers.waffle.VO.PageVO;
import avengers.waffle.VO.PostVO;
import avengers.waffle.VO.ReplyVO;
import avengers.waffle.entity.Post;
import avengers.waffle.entity.Reply;
import avengers.waffle.service.If_GullService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

<<<<<<< HEAD
@RestController
=======
@Controller
>>>>>>> 717ac2530a1f92c433767ce7361f6046b03b8ead
@RequiredArgsConstructor
public class GullController {

    private final If_GullService GullService;
    private final FileDataUtil fileDataUtil;

<<<<<<< HEAD
    @GetMapping("team/viewlist")
    public Map<String,Object> Gullist(@RequestParam int page, @RequestParam String category) {
        List<PostVO> list = GullService.getPostList(page,category);
        PageVO pageVO = GullService.getPage(page,category);
        Map<String,Object> map = new HashMap<>();

        map.put("post",list);
        map.put("page",pageVO);
=======
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
        Post post = GullService.getPost(no);
        List<ReplyVO> replyList = GullService.getReplyList(no);
        Map<String, Object> map = new HashMap<>();
        map.put("post", post);
        map.put("replyList", replyList);
>>>>>>> 717ac2530a1f92c433767ce7361f6046b03b8ead
        return map;
    }

    @PostMapping(value = "team/posts")
    public String Gullwrite(@ModelAttribute PostVO post, MultipartFile[] fileUrl) throws IOException {
        String[] fileName=fileDataUtil.fileUpload(fileUrl);
        GullService.addPost(post,fileName);
        return "success";
    }

}
