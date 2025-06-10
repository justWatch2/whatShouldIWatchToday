package avengers.waffle.service;

import avengers.waffle.VO.PageVO;
import avengers.waffle.VO.PostVO;

import java.io.IOException;
import java.util.List;

public interface If_GullService {

    List<PostVO> getPostList(int page, String category);

    PageVO getPage(int page, String category);

    void addPost(PostVO post, String[] fileUrl) throws IOException;
}