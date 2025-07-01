package avengers.waffle.service.IF.movieDetail;

import avengers.waffle.VO.movieDetail.PostResultVO;
import avengers.waffle.VO.movieDetail.VoteVO;

import java.util.List;
import java.util.Map;

public interface IF_DetailService {
    void delWish(String memberId, String category, Integer id);
    void addWish(String memberId, String category, Integer id);
    void delView(String memberId, String category, Integer id);
    void addView(String memberId, String category, Integer id);
    List<PostResultVO> PostLoad(String title,String korean_title);

    Map<String, Boolean> getDetail(String memberId, String category, Integer id);

    VoteVO getVote(String id);
}
