package avengers.waffle.service.IF.movieDetail;

import avengers.waffle.VO.movieDetail.PostResultVO;

import java.util.List;

public interface IF_DetailService {
    boolean getWish(String memberId, String category, Integer id);
    void delWish(String memberId, String category, Integer id);
    void addWish(String memberId, String category, Integer id);
    boolean getView(String memberId, String category, Integer id);
    void delView(String memberId, String category, Integer id);
    void addView(String memberId, String category, Integer id);
    List<PostResultVO> PostLoad(String title,String korean_title);
}
