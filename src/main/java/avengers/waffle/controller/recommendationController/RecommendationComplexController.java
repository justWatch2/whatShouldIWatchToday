package avengers.waffle.controller.recommendationController;


import avengers.waffle.dto.requestDTO.ComplexRequestDTO;
import avengers.waffle.dto.responseDTO.ComplexResponseDTO;
import avengers.waffle.service.recommendationService.RecommendationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
@RequestMapping("/rec")
@RequiredArgsConstructor
public class RecommendationComplexController {

    private final RecommendationService recommendationService;

    @PostMapping("/complex")
    public ResponseEntity<ComplexResponseDTO> complexRecommendation(@RequestBody ComplexRequestDTO requestDTO) {
        return complexRecommend(requestDTO);
    }



    private ResponseEntity<ComplexResponseDTO> complexRecommend(ComplexRequestDTO requestDTO) {

        ComplexResponseDTO response = recommendationService.complexRecommend(requestDTO);
        response.setMediatype(requestDTO.getMediaType());
        log.info(response.toString());
        log.info(String.valueOf(response.getSelectedList().size()));
        return ResponseEntity.ok(response);
    }


}
