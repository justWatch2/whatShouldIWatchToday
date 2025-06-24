package avengers.waffle.service.IF.search;

import avengers.waffle.VO.search.SearchVO;

import java.util.List;
import java.util.Map;

public interface IF_SearchService {
    Map<String,Object> searchMovie(SearchVO movieSearch);
    Map<String,Object> searchTV(SearchVO movieSearch);
    List<String> setSerachList(String memberId,String title);
    List<String> getSearchList(String memberId);
}
