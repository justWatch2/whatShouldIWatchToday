package avengers.waffle.dto.requestDTO;


import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@Builder
public class UserRecommendRequestDTO {
private String userId;
private String mediaType;
private String region;
private String ageRating;
private List<String> selectedGenres;
}
