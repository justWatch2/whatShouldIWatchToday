package avengers.waffle.repository.recommendationMovieRepository;

import avengers.waffle.dto.mybatis.MoviesDTO;
import avengers.waffle.dto.mybatis.UserRecommendMovieResultDTO;
import avengers.waffle.dto.searchDTO.ComplexSearchDTO;
import avengers.waffle.dto.searchDTO.UserRecommendSearchDTO;

import java.util.List;

public interface RecommendationMovieRepository {
    List<MoviesDTO> findMoviesByGenres(List<String> genres, boolean isDomestic);

    List<MoviesDTO> searchComplexMovie(ComplexSearchDTO complexSearchDTO);

    List<UserRecommendMovieResultDTO> getMovieCandidates(UserRecommendSearchDTO userRecommendSearchDTO);

}
