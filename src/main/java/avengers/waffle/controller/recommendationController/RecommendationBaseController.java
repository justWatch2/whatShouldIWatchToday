package avengers.waffle.controller.recommendationController;


import avengers.waffle.dto.requestDTO.MbtiRequestDTO;
import avengers.waffle.dto.requestDTO.TimeRequestDTO;
import avengers.waffle.dto.requestDTO.WeatherRequestDTO;
import avengers.waffle.dto.responseDTO.ResponseDTO;
import avengers.waffle.service.recommendationService.RecommendationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/rec")
@RequiredArgsConstructor
@Slf4j
public class RecommendationBaseController {
    private final RecommendationService recommendationService;

    @PostMapping("/weather")
    public ResponseEntity<ResponseDTO> recommendByWeather(@RequestBody WeatherRequestDTO request) {
       log.info("++++++++++++++++++++++++++++++++++++++++++++++++++");
       log.info("qweqweqweqweqweqweqweqweqweqwe");
        System.out.println(request.getGenresMovies());
        System.out.println(request.getGenresTV());
        return baseRecommend(request.getGenresMovies(), request.getGenresTV());
    }
    @PostMapping("/time")
    public ResponseEntity<ResponseDTO> recommendByTime(@RequestBody TimeRequestDTO request) {
        System.out.println("asdadw하하하하하하하하ㅏ핳");
        return baseRecommend(request.getGenresMovies(), request.getGenresTV());
    }

    @PostMapping("/mbti")
    public ResponseEntity<ResponseDTO> recommendByMbti(@RequestBody MbtiRequestDTO request) {
        return baseRecommend(request.getGenresMovies(), request.getGenresTV());
    }


    private ResponseEntity<ResponseDTO> baseRecommend(List<String> movieGenres, List<String> tvGenres) {
        List<String> safeMovieGenres = movieGenres != null ? movieGenres : Collections.emptyList();
        log.info(safeMovieGenres.toString());
        List<String> safeTvGenres = tvGenres != null ? tvGenres : Collections.emptyList();
        ResponseDTO response = recommendationService.baseRecommend(safeMovieGenres, safeTvGenres);

        log.info("response: {}", response);
        return ResponseEntity.ok(response);
    }
}
