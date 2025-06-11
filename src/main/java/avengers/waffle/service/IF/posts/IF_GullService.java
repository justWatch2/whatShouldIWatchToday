package avengers.waffle.service.IF.posts;

import avengers.waffle.VO.util.PageVO;
import avengers.waffle.VO.posts.PostVO;
import avengers.waffle.VO.posts.ReplyVO;
import avengers.waffle.entity.Member;
import avengers.waffle.entity.Post;
import avengers.waffle.entity.PostAttach;

import java.io.IOException;
import java.util.List;

public interface IF_GullService {

    List<PostVO> getPostList(int page, String category);

    PageVO getPage(int page, String category);

    PostVO getPost(int no);

    List<ReplyVO> getReplyList(int no);

    public void addPost(PostVO postVO, String[] fileUrl) throws IOException;
}
