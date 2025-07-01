package avengers.waffle.service.recommendationService;


import avengers.waffle.dto.mybatis.MoviesDTO;
import avengers.waffle.dto.querydslDTO.Tv_ShowsDTO;
import avengers.waffle.dto.requestDTO.ComplexRequestDTO;
import avengers.waffle.dto.responseDTO.ComplexResponseDTO;
import avengers.waffle.dto.responseDTO.ResponseDTO;
import avengers.waffle.dto.searchDTO.ComplexSearchDTO;
import avengers.waffle.repository.recommendationMovieRepository.RecommendationMovieRepository;
import avengers.waffle.repository.recommendationTVRepository.RecommendationTvRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationServiceImpl implements RecommendationService {

    private final RecommendationTvRepository recommendTv; //querydsl
    private final RecommendationMovieRepository recommendMovie; //Mybatis

    @Override
    @Transactional(readOnly = true)
    public ResponseDTO baseRecommend(List<String> movieGenres, List<String> tvGenres) {
        List<MoviesDTO> domesticMovies = recommendMovie.findMoviesByGenres(movieGenres,true);
        List<MoviesDTO>internationalMovies = recommendMovie.findMoviesByGenres(movieGenres,false);
        List<Tv_ShowsDTO> domesticTV = recommendTv.findTVshowsByGenres(tvGenres,true);
        List<Tv_ShowsDTO> internationalTV = recommendTv.findTVshowsByGenres(tvGenres,false);

            return ResponseDTO.builder()
                    .domesticMovies(domesticMovies)
                    .internationalMovies(internationalMovies)
                    .domesticTV(domesticTV)
                    .internationalTV(internationalTV)
                    .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ComplexResponseDTO complexRecommend(ComplexRequestDTO requestDTO) {

        ComplexSearchDTO searchDTO = ComplexSearchDTO.builder()
                .adult(requestDTO.getAgeRating().equals("adult"))
                .isDomestic(requestDTO.getRegion().equals("domestic"))
                .rating(requestDTO.getRating())
                .releaseYear(requestDTO.getReleaseYear())
                .selectedGenres(requestDTO.getSelectedGenres())
                .build();

        if(requestDTO.getMediaType().equals("movie")){
            List<MoviesDTO> complexMovies = recommendMovie.searchComplexMovie(searchDTO);
            return ComplexResponseDTO.builder()
                    .selectedList(complexMovies)
                    .build();
        }else{
            List<Tv_ShowsDTO> complexTv = recommendTv.searchComplexTv(searchDTO);
            log.info("complexTV: {} ", complexTv);
            return ComplexResponseDTO.builder()
                    .selectedList(complexTv)
                    .build();
        }
    }


}
