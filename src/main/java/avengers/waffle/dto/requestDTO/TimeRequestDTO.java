package avengers.waffle.dto.requestDTO;


import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class TimeRequestDTO {
    private List<String> genresMovies;
    private List<String> genresTV;
}
