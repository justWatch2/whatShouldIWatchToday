package avengers.waffle.dto.searchDTO;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@Builder
public class UserRecommendSearchDTO {

private String userId;
private Boolean isDomestic;
private Boolean adult;
private List<String> selectedGenres;
}
