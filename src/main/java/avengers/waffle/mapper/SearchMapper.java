package avengers.waffle.mapper;

import avengers.waffle.VO.search.SearchResultVO;
import avengers.waffle.VO.search.SearchVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SearchMapper {
    List<SearchResultVO> searchMovie(SearchVO movie);
    List<SearchResultVO> searchTV(SearchVO movie);
    int getPage(SearchVO movie);
    int getPageTV(SearchVO movie);

}
