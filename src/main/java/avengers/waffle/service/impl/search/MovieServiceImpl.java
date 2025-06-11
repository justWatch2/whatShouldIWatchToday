package avengers.waffle.service.impl.search;


import avengers.waffle.VO.search.MovieSearchVO;
import avengers.waffle.entity.Movies;
import avengers.waffle.mapper.MovieMapper;
import avengers.waffle.service.IF.search.IF_MovieService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements IF_MovieService {
    private final MovieMapper movieMapper;

    @Transactional
    public List<Movies> searchMovie(MovieSearchVO movieSearch) {
        return movieMapper.search(movieSearch);
    }

}
