package avengers.waffle.service.impl.posts;

import avengers.waffle.VO.util.PageVO;
import avengers.waffle.VO.posts.PostVO;
import avengers.waffle.VO.posts.ReplyVO;
import avengers.waffle.entity.Member;
import avengers.waffle.entity.Post;
import avengers.waffle.entity.PostAttach;
import avengers.waffle.entity.Reply;
import avengers.waffle.repository.posts.PostAttachRepository;
import avengers.waffle.repository.posts.PostRepository;
import avengers.waffle.repository.posts.ReplyRepository;
import avengers.waffle.repository.mapping.attachMapping;
import avengers.waffle.service.IF.posts.IF_GullService;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GullServiceImpl implements IF_GullService {
    private final PostRepository postRepository;
    private final ReplyRepository replyRepository;
    private final PostAttachRepository postAttachRepository;


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
                    .name(post.getMember().getMemberName())
                    .indate(post.getIndate())
                    .count(post.getCount())
                    .build()
            );
        }
        System.out.println("1111");
        return postVOs;
    }

    @Override
    public PageVO getPage(int page, String category) {
        PageVO pageVO = new PageVO();
        pageVO.setPage(page);
        pageVO.setTotalCount(postRepository.countByCategory(category));
        return pageVO;
    }

    @Override
    public PostVO getPost(int no) {
        Optional<Post> optional = postRepository.findByNo(no);
        List<attachMapping> attachs = postAttachRepository.findAllByPost_no(no);
        List<String> attachments = new ArrayList<>();
        for(attachMapping mapping : attachs) {
            attachments.add(mapping.getFileUrl());
        }
        if (optional.isPresent()) {
            Post post = optional.get();
            PostVO pvo = PostVO.builder()
                    .title(post.getTitle())
                    .name(post.getMember().getMemberId())
                    .indate(post.getIndate())
                    .count(post.getCount())
                    .likeCount(post.getLikeCount())
                    .fileUrl(attachments)
                    .contents(post.getContents())
                    .build();
            return pvo;
        }
        System.out.println("1111");
        System.out.println("2222");
        System.out.println("3333");
        return null;
    }

    @Override
    public List<ReplyVO> getReplyList(int no) {
        List<ReplyVO> replyVOs = new ArrayList<>();
        List<Reply> replies = replyRepository.findAllByPost_No(no);
        for (Reply reply : replies) {
            ReplyVO replyVO = ReplyVO.builder()
                    .no((int) reply.getReplyNum())
                    .memberId(reply.getMember().getMemberId())
                    .content(reply.getContents())
                    .time(String.valueOf(reply.getIndate()))
                    .likeCount(reply.getLikeCount())
                    .build();
            replyVOs.add(replyVO);
        }
        return replyVOs;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public void addPost(PostVO postVO, String[] fileUrl) throws IOException {
        Post post = Post.builder()
                .title(postVO.getTitle())
                .category(postVO.getCategory())
                .contents(postVO.getContents())
                .member(Member.builder().memberId(postVO.getMemberId()).build())
                .build();
        postRepository.save(post);

        for (String fileUrl1 : fileUrl) {
            postAttachRepository.save(PostAttach.builder()
                    .post(post)
                    .fileUrl(fileUrl1)
                    .build());
        }
    }
}
