package avengers.waffle.service.recommendationService;

import avengers.waffle.dto.requestDTO.ComplexRequestDTO;
import avengers.waffle.dto.responseDTO.ComplexResponseDTO;
import avengers.waffle.dto.responseDTO.ResponseDTO;

import java.util.List;

public interface RecommendationService {
    ResponseDTO baseRecommend(List<String> movieGenres, List<String> tvGenres);

    ComplexResponseDTO complexRecommend(ComplexRequestDTO requestDTO);

}
