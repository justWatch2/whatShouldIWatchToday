package avengers.waffle.configuration.messaging;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserRecommendJobMessage {
    private String requestId;
    private String userId;
    private String mediaType;
    private String region;
    private String ageRating;
    private List<String> selectedGenres;
}
