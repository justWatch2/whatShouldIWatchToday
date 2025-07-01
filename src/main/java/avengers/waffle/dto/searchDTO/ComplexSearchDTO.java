package avengers.waffle.dto.searchDTO;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@Builder
public class ComplexSearchDTO {
    private boolean adult;
    private boolean isDomestic;
    private List<Integer> rating;
    private List<Integer> releaseYear;
    private List<String> selectedGenres;
}
