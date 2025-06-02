package avengers.waffle.service.impl;

import avengers.waffle.VO.PageVO;
import avengers.waffle.VO.PostVO;
import avengers.waffle.entity.Post;
import avengers.waffle.repository.PostRepository;
import avengers.waffle.service.If_GullService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GullServiceImpl implements If_GullService {
    private final PostRepository postRepository;
    private PageVO pageVO = new PageVO();


    @Override
    public List<PostVO> getPostList(int page, String category) {
        Pageable pageable = PageRequest.of(page, 20);
        Page<Post> posts = postRepository.findAllByCategoryOrderByIndateDesc(category,pageable);
//        pageVO.setPage(page);
//        int totalCount = postRepository.countByCategory(category);
//        pageVO.setTotalCount(totalCount);
        List<PostVO> postVOs = new ArrayList<>();
        for (Post post : posts) {
            postVOs.add(PostVO.builder()
                    .no(post.getNo())
                    .title(post.getTitle())
                    .name(post.getMovieMember().getMemberName())
                    .indate(post.getIndate())
                    .count(post.getCount())
                    .build()
            );
        }
        return postVOs;
    }

    @Override
    public PageVO getPage(int page, String category) {
        PageVO pageVO = new PageVO();
        pageVO.setPage(page);
        pageVO.setTotalCount(postRepository.CountByCategory(category));
        return pageVO;
    }


}
