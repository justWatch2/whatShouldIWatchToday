package avengers.waffle.repository.recommendationMovieRepository;

import avengers.waffle.dto.mybatis.MoviesDTO;
import avengers.waffle.dto.mybatis.UserRecommendMovieResultDTO;
import avengers.waffle.dto.searchDTO.ComplexSearchDTO;
import avengers.waffle.dto.searchDTO.UserRecommendSearchDTO;
import avengers.waffle.mapper.MovieMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RecommendationMovieRepositoryImpl implements RecommendationMovieRepository {

    private final MovieMapper movieMapper;
    @Override
    public List<MoviesDTO> findMoviesByGenres(List<String> genres, boolean isDomestic) {
        return movieMapper.findMoviesByGenres(genres, isDomestic);
    }

    @Override
    public List<MoviesDTO> searchComplexMovie(ComplexSearchDTO complexSearchDTO) {
        return movieMapper.searchComplexMovie(complexSearchDTO);
    }

    @Override
    public List<UserRecommendMovieResultDTO> getMovieCandidates(UserRecommendSearchDTO userRecommendSearchDTO) {
        return movieMapper.getMovieCandidates(userRecommendSearchDTO);
    }

}
