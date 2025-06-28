package avengers.waffle.repository.recommendationTVRepository;

import avengers.waffle.dto.mybatis.UserRecommendTVResultDTO;
import avengers.waffle.dto.searchDTO.UserRecommendSearchDTO;
import avengers.waffle.mapper.TvMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRecommendTvRepositoryImpl implements UserRecommendTvRepository {
    private final TvMapper tvMapper;

    @Override
    public List<UserRecommendTVResultDTO> getTvCandidates(UserRecommendSearchDTO userRecommendSearchDTO) {
        System.out.println("111111111111111" + userRecommendSearchDTO.toString());
        return tvMapper.getTvCandidates(userRecommendSearchDTO);
    }
}
