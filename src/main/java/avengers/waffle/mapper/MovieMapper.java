package avengers.waffle.mapper;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface MovieMapper {

<<<<<<< HEAD
import avengers.waffle.VO.MovieSearch;
import avengers.waffle.entity.Movies;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MovieMapper {
    List<Movies> search(MovieSearch movie);
=======
>>>>>>> 717ac2530a1f92c433767ce7361f6046b03b8ead
}
