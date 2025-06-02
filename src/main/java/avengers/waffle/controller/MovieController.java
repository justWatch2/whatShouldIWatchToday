package avengers.waffle.controller;

import avengers.waffle.VO.PostVO;
import avengers.waffle.entity.Post;
import avengers.waffle.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MovieController {
    private final PostRepository postRepository;

//    @GetMapping("/getGullist")
//    public List<PostVO> getGullist(@RequestParam int page) {
//        List<Post> postList = postRepository.findAll();
//        List<PostVO> postVOList = new ArrayList<>();
//        for (Post post : postList) {
//            PostVO postVo = PostVO.builder()
//                    .no(post.getNo())
//                    .title(post.getTitle())
//                    .name(post.getMovieMember().getMemberName())
//                    .indate(post.getIndate())
//                    .count(post.getCount())
//                    .build();
//            postVOList.add(postVo);
//        }
//        System.out.println(postList.get(1));
//        return postVOList;
//    }


}
