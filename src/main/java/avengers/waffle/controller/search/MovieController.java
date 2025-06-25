package avengers.waffle.controller.search;

import avengers.waffle.VO.search.MovieSearchVO;
import avengers.waffle.entity.Movies;
import avengers.waffle.service.IF.posts.IF_GullService;
import avengers.waffle.service.IF.search.IF_MovieService;
import avengers.waffle.service.impl.search.MovieServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MovieController {

    private final IF_MovieService movieService;
    private final IF_GullService GullService;

    @GetMapping("api/movie/search")
    public List<Movies> viewlist(@ModelAttribute MovieSearchVO movie) {
        System.out.println(movie.getTitle());
        System.out.println(movie.getGenres());
        System.out.println(movie.getYears().length);
        System.out.println(movie.isKorean());
        System.out.println(movie.isAdult());

        return movieService.searchMovie(movie);
    }
}
