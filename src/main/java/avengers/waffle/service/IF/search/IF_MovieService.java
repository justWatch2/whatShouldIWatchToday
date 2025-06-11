package avengers.waffle.service.IF.search;

import avengers.waffle.VO.search.MovieSearchVO;
import avengers.waffle.entity.Movies;

import java.util.List;

public interface IF_MovieService {
    public List<Movies> searchMovie(MovieSearchVO movieSearch);
}
