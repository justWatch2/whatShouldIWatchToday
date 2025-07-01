package avengers.waffle.mapper;

import avengers.waffle.dto.mybatis.MoviesDTO;
import avengers.waffle.dto.mybatis.UserRecommendMovieResultDTO;
import avengers.waffle.dto.searchDTO.ComplexSearchDTO;
import avengers.waffle.dto.searchDTO.UserRecommendSearchDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MovieMapper {
    List<MoviesDTO> findMoviesByGenres(List<String> genres, boolean isDomestic);

    List<MoviesDTO> searchComplexMovie(ComplexSearchDTO complexSearchDTO);

    List<UserRecommendMovieResultDTO>getMovieCandidates(UserRecommendSearchDTO userRecommendSearchDTO);
}
