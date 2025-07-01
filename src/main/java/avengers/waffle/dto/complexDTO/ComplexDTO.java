package avengers.waffle.dto.complexDTO;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ComplexDTO {
    private int id;
    private String title;
    private String poster_path;
    private String overview;
    @QueryProjection
    public ComplexDTO(int id, String title, String poster_path , String overview) {
        this.id = id;
        this.title = title;
        this.poster_path = poster_path;
        this.overview = overview;
    }
}
