package avengers.waffle.service;

import avengers.waffle.VO.PageVO;
import avengers.waffle.VO.PostVO;
<<<<<<< HEAD
=======
import avengers.waffle.VO.ReplyVO;
import avengers.waffle.entity.Post;
import org.springframework.data.domain.Pageable;
>>>>>>> 717ac2530a1f92c433767ce7361f6046b03b8ead

import java.io.IOException;
import java.util.List;

public interface If_GullService {

    List<PostVO> getPostList(int page, String category);

    PageVO getPage(int page, String category);

<<<<<<< HEAD
    void addPost(PostVO post, String[] fileUrl) throws IOException;
}
=======
    Post getPost(int no);

    List<ReplyVO> getReplyList(int no);
}
>>>>>>> 717ac2530a1f92c433767ce7361f6046b03b8ead
