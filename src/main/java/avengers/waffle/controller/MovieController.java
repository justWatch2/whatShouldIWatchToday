package avengers.waffle.controller;

import avengers.waffle.VO.MovieSearch;
import avengers.waffle.entity.Movies;
import avengers.waffle.service.If_GullService;
import avengers.waffle.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MovieController {

    private final MovieService movieService;
    private final If_GullService GullService;

    @GetMapping("team/search/movie/")
    public List<Movies> viewlist(@ModelAttribute MovieSearch movie) {
        System.out.println(movie.getTitle());
        System.out.println(movie.getGenres());
        System.out.println(movie.getYears().length);
        System.out.println(movie.isKorean());
        System.out.println(movie.isAdult());

        return movieService.searchMovie(movie);
    }
}
