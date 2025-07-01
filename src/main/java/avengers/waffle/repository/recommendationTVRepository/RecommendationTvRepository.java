package avengers.waffle.repository.recommendationTVRepository;

import avengers.waffle.dto.querydslDTO.Tv_ShowsDTO;
import avengers.waffle.dto.searchDTO.ComplexSearchDTO;

import java.util.List;

public interface RecommendationTvRepository {
//    List<MoviesDTO> findMoviesByGenres(List<String> genres, boolean isDomestic);
    List<Tv_ShowsDTO> findTVshowsByGenres(List<String> genres, boolean isDomestic);

    List<Tv_ShowsDTO> searchComplexTv(ComplexSearchDTO complexSearchDTO);



}
