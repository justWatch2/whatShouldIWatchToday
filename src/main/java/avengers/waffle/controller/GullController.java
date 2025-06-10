package avengers.waffle.controller;

import avengers.waffle.VO.PageVO;
import avengers.waffle.VO.PostVO;
import avengers.waffle.VO.ReplyVO;
import avengers.waffle.entity.Post;
import avengers.waffle.entity.Reply;
import avengers.waffle.service.If_GullService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class GullController {

    private final If_GullService GullService;

    @GetMapping("/api/Gullist")
    @ResponseBody
    public Map<String,Object> Gullist(@RequestParam int page, @RequestParam String category) {
        List<PostVO> list = GullService.getPostList(page,category);
        PageVO pageVO = GullService.getPage(page,category);
        Map<String,Object> map = new HashMap<>();
        map.put("list",list);
        map.put("pageVO",pageVO);
        return map;
    }

    @GetMapping("/api/GullDetail")
    @ResponseBody
    public Map<String, Object> GullDetail(@RequestParam int no) {
        Post post = GullService.getPost(no);
        List<ReplyVO> replyList = GullService.getReplyList(no);
        Map<String,Object> map = new HashMap<>();
        map.put("post",post);
        map.put("replyList",replyList);
        return map;
    }
}
