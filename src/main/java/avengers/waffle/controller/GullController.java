package avengers.waffle.controller;

import avengers.waffle.VO.PageVO;
import avengers.waffle.VO.PostVO;
import avengers.waffle.service.If_GullService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class GullController {

    private final If_GullService GullService;
    private final FileDataUtil fileDataUtil;

    @GetMapping("team/viewlist")
    public Map<String,Object> Gullist(@RequestParam int page, @RequestParam String category) {
        List<PostVO> list = GullService.getPostList(page,category);
        PageVO pageVO = GullService.getPage(page,category);
        Map<String,Object> map = new HashMap<>();

        map.put("post",list);
        map.put("page",pageVO);
        return map;
    }

    @PostMapping(value = "team/posts")
    public String Gullwrite(@ModelAttribute PostVO post, MultipartFile[] fileUrl) throws IOException {
        String[] fileName=fileDataUtil.fileUpload(fileUrl);
        GullService.addPost(post,fileName);
        return "success";
    }

}
