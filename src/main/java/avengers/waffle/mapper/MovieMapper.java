package avengers.waffle.mapper;

import avengers.waffle.VO.MovieSearch;
import avengers.waffle.entity.Movies;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MovieMapper {
    List<Movies> search(MovieSearch movie);
}
