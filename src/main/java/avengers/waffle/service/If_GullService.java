package avengers.waffle.service;

import avengers.waffle.VO.PageVO;
import avengers.waffle.VO.PostVO;
import avengers.waffle.VO.ReplyVO;
import avengers.waffle.entity.Post;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface If_GullService {

    List<PostVO> getPostList(int page, String category);

    PageVO getPage(int page, String category);

    Post getPost(int no);

    List<ReplyVO> getReplyList(int no);
}
