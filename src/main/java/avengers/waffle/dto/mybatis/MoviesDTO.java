package avengers.waffle.dto.mybatis;


import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class MoviesDTO {
    private int id;
    private String korean_title;
    private String poster_path;
    private String backdrop_path;
    private String overview;
    private float averageRating;
    private String movie;

}
