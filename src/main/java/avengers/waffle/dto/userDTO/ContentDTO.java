package avengers.waffle.dto.userDTO;


import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class ContentDTO {
    private int id;
    private String koreanTitle;
    private String posterPath;
    private String backdropPath;
    private String overview;
    private double averageRating;
}
