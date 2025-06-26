package avengers.waffle.service.impl.posts;

import avengers.waffle.VO.login.MemberDTO;
import avengers.waffle.VO.posts.*;
//import avengers.waffle.VO.util.PageVO;
import avengers.waffle.entity.*;
import avengers.waffle.mapper.PostMapper;

import avengers.waffle.repository.posts.*;
import avengers.waffle.repository.mapping.attachMapping;
import avengers.waffle.service.IF.posts.IF_GullService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GullServiceImpl implements IF_GullService {
    private final PostRepository postRepository;
    private final ReplyRepository replyRepository;
    private final PostAttachRepository postAttachRepository;
    private final PostLikeListRepository postLikeListRepository;
    private final ReplyLikeListRepository replyLikeListRepository;
    private final PostMapper postMapper;
    private final EntityManager em;

    private final BCryptPasswordEncoder passwordEncoder;


    @Override
    public Page<Post4ListDTO> getPostList(int page, String category) {
        Pageable pageable = PageRequest.of(page, 20);
        Page<Post> posts = postRepository.findAllByCategoryOrderByIndateDesc(category, pageable);
//        System.out.println(category);
//        System.out.println(posts.getTotalPages());
//        System.out.println("Page " + posts.getTotalElements());

        List<Post4ListDTO> post4ListDTOs = new ArrayList<>();
        for (Post post : posts) {
            post4ListDTOs.add(Post4ListDTO.builder()
                    .no(post.getNo())
                    .title(post.getTitle())
                    .name(post.getMember().getMemberId())
                    .indate(post.getIndate())
                    .count(post.getCount())
                    .build()
            );
        }

        return new PageImpl<>(post4ListDTOs, pageable, posts.getTotalElements());
    }

    @Override
    public PostVO getPost(int no) {
        Post post = getPostByNo(no);
        List<attachMapping> attachs = postAttachRepository.findAllByPost_no(no);
        List<String> attachments = new ArrayList<>();
        for (attachMapping mapping : attachs) {
            attachments.add(mapping.getFileUrl());
        }
        PostVO postVO = PostVO.builder()
                .title(post.getTitle())
                .category(post.getCategory())
                .name(post.getMember().getMemberId())
                .indate(post.getIndate())
                .count(post.getCount())
                .likeCount(post.getLikeCount())
                .contents(post.getContents())
                .fileUrllist(attachments)
                .build();
        return postVO;

    }

    public Post getPostByNo(int no) {
        Post post = em.find(Post.class, no);
        post.setCount(post.getCount() + 1);
        post.setPopularity(post.getPopularity() + 1);
        return post;
    }

    @Override
    public List<ReplyVO> getReplyList(int no) {
        List<ReplyVO> replyVOs = new ArrayList<>();
        List<Reply> replies = replyRepository.findAllByPost_No(no);
        for (Reply reply : replies) {
            ReplyVO replyVO = ReplyVO.builder()
                    .num((int) reply.getNo())
                    .name(reply.getMember().getMemberId())
                    .contents(reply.getContents())
                    .time(String.valueOf(reply.getIndate()))
                    .likeCount(reply.getLikeCount())
                    .liked(getLikeReply((int) reply.getNo(), reply.getMember().getMemberId()))
                    .build();
            replyVOs.add(replyVO);
        }
        return replyVOs;
    }

    @Override
    public void addPost(PostVO postVO, String[] fileUrl) throws IOException {
        Post post = Post.builder()
                .title(postVO.getTitle())
                .category(postVO.getCategory())
                .contents(postVO.getContents())
                .member(Member.builder().memberId(postVO.getName()).build())
                .build();
        postRepository.save(post);
        if (fileUrl != null) {
            for (String fileUrl1 : fileUrl) {
                postAttachRepository.save(PostAttach.builder()
                        .post(post)
                        .fileUrl(fileUrl1)
                        .build());
            }
        }
    }

    @Override
    public List<Post4ListDTO> getTopPosts(String category) {
        List<Post4ListDTO> post4ListDTOs = postMapper.top5Ranking(category);
        System.out.println("post4Lists= " + post4ListDTOs.size());
        return post4ListDTOs;
    }

    @Override
    public void addReply(ReplyVO replyVO) {
        Reply reply = Reply.builder()
                .post(Post.builder().no(Integer.parseInt(replyVO.getPostNo())).build())
                .member(Member.builder().memberId(replyVO.getName()).build())
                .contents(replyVO.getContents())
                .build();
        replyRepository.save(reply);
    }

    @Override
    public Post updateLikeCount(int no, boolean param) {
        Post post = em.find(Post.class, no);
        post.setLikeCount(param ? post.getLikeCount() + 1 : post.getLikeCount() - 1);
        post.setPopularity(param ? post.getPopularity() + 5 : post.getPopularity() - 5);
        return post;
    }

    @Override
    public boolean updateLikeList(PostLikeDTO postLikeDTO, boolean param) {
        boolean memberExist = getLikePost(postLikeDTO.getPostNo(), postLikeDTO.getMemberId());
        if (!memberExist & param) {
            postLikeListRepository.save(PostLikeList.builder()
                    .post(Post.builder().no(postLikeDTO.getPostNo()).build())
                    .member(Member.builder().memberId(postLikeDTO.getMemberId()).build())
                    .build());
            return true;
        } else {
            if (!param) {
                System.out.println("delete");
                postLikeListRepository.deleteByMember_memberIdAndPost_no(postLikeDTO.getMemberId(), postLikeDTO.getPostNo());
                return true;
            }
            return false;
        }
    }


    @Override
    public boolean getLikePost(int no, String id) {
        return postLikeListRepository.existsByMember_memberIdAndPost_no(id, no);
    }

    public Post updatePost(PostVO postVO) {
        Post post = em.find(Post.class, postVO.getNo());
        post.setTitle(postVO.getTitle());
        post.setCategory(postVO.getCategory());
        post.setContents(postVO.getContents());
        return post;
    }

    @Override
    public void updatePostAndFile(PostVO postVO, String[] fileUrl, String[] existingFileUrl) {
        Post post = updatePost(postVO);
        List<attachMapping> attachs = postAttachRepository.findAllByPost_no(post.getNo());
        List<String> existFiles = new ArrayList<>();
        for (attachMapping mapping : attachs) {
            existFiles.add(mapping.getFileUrl());
        }
        for (String url : existingFileUrl) {
            existFiles.remove(url);
        }
        for (String existFile : existFiles) {
            postAttachRepository.deleteByFileUrl(existFile);
        }
//            postAttachRepository.deleteByPost_no(postVO.getNo());
        if (fileUrl != null) {
            for (String fileUrl1 : fileUrl) {
                postAttachRepository.save(PostAttach.builder()
                        .post(post)
                        .fileUrl(fileUrl1)
                        .build());
            }
        }
    }

    @Override
    public boolean updateLikeList4Reply(ReplyLikeDTO replyLikeDTO, boolean param) {
        boolean memberExist = getLikeReply(replyLikeDTO.getReplyNo(), replyLikeDTO.getMemberId());
        if (!memberExist & param) {
            replyLikeListRepository.save(ReplyLikeList.builder()
                    .reply(Reply.builder().no(replyLikeDTO.getReplyNo()).build())
                    .member(Member.builder().memberId(replyLikeDTO.getMemberId()).build())
                    .build());
            return true;
        } else {
            if (!param) {
                replyLikeListRepository.deleteByReply_noAndMember_memberId(replyLikeDTO.getReplyNo(), replyLikeDTO.getMemberId());
                return true;
            }

        }
        return false;
    }

    private boolean getLikeReply(int no, String id) {
        return replyLikeListRepository.existsByReply_noAndMember_memberId(no, id);
    }

    @Override
    public void updateLikeCount4Reply(int replyNo, boolean param) {
        Reply reply = em.find(Reply.class, replyNo);
        reply.setLikeCount(param ? reply.getLikeCount() + 1 : reply.getLikeCount() - 1);
    }

    
}



