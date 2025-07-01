package avengers.waffle.dto.querydslDTO;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Tv_ShowsDTO {
    private int id;
    private String koreanName;
    private String poster_path;
    private String backdrop_path;
    private String overview;
    private float vote_average;

    @QueryProjection
    public Tv_ShowsDTO(int id, String koreanName, String poster_path , String backdrop_path, String overview,float vote_average) {
        this.id = id;
        this.koreanName = koreanName;
        this.poster_path = poster_path;
        this.backdrop_path = backdrop_path;
        this.overview = overview;
        this.vote_average = vote_average;
    }
}
