package avengers.waffle.controller.posts;

import avengers.waffle.VO.posts.*;
//import avengers.waffle.VO.util.PageVO;
import avengers.waffle.entity.PostLikeList;
import avengers.waffle.service.IF.posts.IF_GullService;
import avengers.waffle.utils.FileDataUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class GullController {

    private final IF_GullService GullService;
    private final FileDataUtil fileDataUtil;

    @GetMapping("/getPosts")
    @ResponseBody
    public Map<String, Object> posts(@RequestParam int page, @RequestParam String category) {
        Page<Post4ListDTO> list = GullService.getPostList(page, category);
//        PageVO pageVO = GullService.getPage(page, category);
        System.out.println("totalElements = " + list.getTotalElements());
        System.out.println("totalPages = " + list.getTotalPages());
        System.out.println("size = " + list.getSize());
        System.out.println("first = " + list.isFirst());
        System.out.println("last = " + list.isLast());
//        System.out.println(pageVO.getPage());
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
//        map.put("pageVO", pageVO);
        return map;
    }

    @GetMapping("/getTopPosts")
    @ResponseBody
    public List<Post4ListDTO> topPosts(@RequestParam String category) {
        return GullService.getTopPosts(category);
    }


    @GetMapping("/getPost")
    @ResponseBody
    public Map<String, Object> GullDetail(@RequestParam int no, @RequestParam String id) {
        PostVO post = GullService.getPost(no);
        boolean likePost = GullService.getLikePost(no, id);
        List<ReplyVO> replyList = GullService.getReplyList(no);
        Map<String, Object> map = new HashMap<>();
        map.put("post", post);
        map.put("replys", replyList);
        map.put("likePost", likePost);
        return map;
    }
    @GetMapping("/getPost2")
    @ResponseBody
    public Map<String, Object> GullDetail2(@RequestParam int no) {
        PostVO post = GullService.getPost(no);
        Map<String, Object> map = new HashMap<>();
        map.put("post", post);
        return map;
    }

    @PostMapping(value = "/addPost")
    @ResponseBody
    public String Gullwrite(@ModelAttribute PostVO post, MultipartFile[] fileUrl) throws IOException {
        System.out.println("도착");
        if (fileUrl != null && fileUrl.length > 0) {
            String[] fileName = fileDataUtil.fileUpload(fileUrl);
            GullService.addPost(post, fileName);
        } else {
            GullService.addPost(post, null);
        }
        return "success";
    }

    @PostMapping("/updatePost")
    @ResponseBody
    public String updatePost(@ModelAttribute PostVO post, MultipartFile[] fileUrl, String[] existingFileUrl) throws IOException {
        System.out.println("수정 도착");
        System.out.println(Arrays.toString(existingFileUrl));
        if (fileUrl != null && fileUrl.length > 0) {
            String[] fileName = fileDataUtil.fileUpload(fileUrl);
            GullService.updatePostAndFile(post, fileName,existingFileUrl);

        } else {
            GullService.updatePostAndFile(post, null,existingFileUrl);
        }
        return "success";
    }

    @GetMapping(value = "/getReplys")
    @ResponseBody
    public List<ReplyVO> getReplys(@RequestParam int no) {
        List<ReplyVO> replyList = GullService.getReplyList(no);
        return replyList;
    }

    @PostMapping(value = "/saveReply")
    @ResponseBody
    public String saveReply(@ModelAttribute ReplyVO reply) {
        System.out.println(reply.getPostNo());
        System.out.println(reply.getName());
        System.out.println(reply.getContents());
        GullService.addReply(reply);
        return "success";
    }

    @PutMapping(value = "/likePost")
    @ResponseBody
    public String likePost(@ModelAttribute PostLikeDTO postLikeDTO, @RequestParam boolean param) {
        System.out.println(param);
        System.out.println(postLikeDTO.getMemberId());
        if (GullService.updateLikeList(postLikeDTO, param)) {
            GullService.updateLikeCount(postLikeDTO.getPostNo(), param);
            return "success";
        }
        return "fail";
    }

    @PutMapping(value="/likeReply")
    @ResponseBody
    public String likeReply(@ModelAttribute ReplyLikeDTO replyLikeDTO, @RequestParam boolean param) {
        System.out.println("likeReply: "+param);
        if(GullService.updateLikeList4Reply(replyLikeDTO, param)){
            GullService.updateLikeCount4Reply(replyLikeDTO.getReplyNo(), param);
            return "success";
        }
        return "fail";
    }


}
