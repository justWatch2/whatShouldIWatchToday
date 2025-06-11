package avengers.waffle.VO.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
public class MovieSearchResultVO {
    private String id;
    private String poster_path;
    private String imdb_id;
    private String overview;
    private String korean_title;
    private String title;
}
