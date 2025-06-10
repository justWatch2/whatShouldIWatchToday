package avengers.waffle.service.impl;

import avengers.waffle.VO.PageVO;
import avengers.waffle.VO.PostVO;
import avengers.waffle.entity.MovieMember;
import avengers.waffle.entity.Post;
import avengers.waffle.entity.PostAttach;
import avengers.waffle.repository.PostAttachRepository;
import avengers.waffle.repository.PostJpaRepository;
import avengers.waffle.service.If_GullService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GullServiceImpl implements If_GullService {
    private final PostJpaRepository postRepository;
    private final PostAttachRepository postAttachRepository;
    private PageVO pageVO = new PageVO();




    @Override
    public List<PostVO> getPostList(int page, String category) {
        Pageable pageable = PageRequest.of(page-1, 20);
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
        pageVO.setTotalCount(postRepository.countByCategory(category));
        return pageVO;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public void addPost(PostVO postVO, String[] fileUrl) throws IOException {
        Post post = Post.builder()
                .title(postVO.getTitle())
                .category(postVO.getCategory())
                .contents(postVO.getContents())
                .movieMember(MovieMember.builder().memberId(postVO.getMemberId()).build())
                .build();
        postRepository.save(post);

        for (String fileUrl1 : fileUrl) {
            postAttachRepository.save(new PostAttach(post,fileUrl1));
        }
    }
}