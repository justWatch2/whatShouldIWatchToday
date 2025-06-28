package avengers.waffle.dto.requestDTO;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class ComplexRequestDTO {
    private String mediaType;
    private String region;
    private String ageRating;
    private List<Integer> rating;
    private List<Integer> releaseYear;
    private List<String> selectedGenres;
}
