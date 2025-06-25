package avengers.waffle.service.IF.posts;

import avengers.waffle.VO.posts.*;
//import avengers.waffle.VO.util.PageVO;
import avengers.waffle.entity.Post;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;

public interface IF_GullService {

    Page<Post4ListDTO> getPostList(int page, String category);

//    PageVO getPage(int page, String category);

    PostVO getPost(int no);

    List<ReplyVO> getReplyList(int no);

    void addPost(PostVO postVO, String[] fileUrl) throws IOException;

    List<Post4ListDTO> getTopPosts(String category);

    void addReply(ReplyVO replyVO);

    Post updateLikeCount(int no, boolean param);

    boolean updateLikeList(PostLikeDTO no, boolean param);

    boolean getLikePost(int no, String id);

    void updatePostAndFile(PostVO post, String[] fileName, String[] existingFileUrl);


    boolean updateLikeList4Reply(ReplyLikeDTO replyLikeDTO, boolean param);

    void updateLikeCount4Reply(int replyNo, boolean param);
}
