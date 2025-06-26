package avengers.waffle.mapper;

import avengers.waffle.VO.movieDetail.PostResultVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DetailMapper {
    List<PostResultVO> postLoad(String title);
}
