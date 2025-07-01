package avengers.waffle.mapper;


import avengers.waffle.dto.mybatis.UserRecommendTVResultDTO;
import avengers.waffle.dto.searchDTO.UserRecommendSearchDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TvMapper {

    List<UserRecommendTVResultDTO> getTvCandidates(UserRecommendSearchDTO userRecommendSearchDTO);
}
