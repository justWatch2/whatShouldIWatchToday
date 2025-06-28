package avengers.waffle.repository.recommendationTVRepository;

import avengers.waffle.dto.mybatis.UserRecommendTVResultDTO;
import avengers.waffle.dto.searchDTO.UserRecommendSearchDTO;

import java.util.List;

public interface UserRecommendTvRepository {
    List<UserRecommendTVResultDTO> getTvCandidates(UserRecommendSearchDTO userRecommendSearchDTO);
}
