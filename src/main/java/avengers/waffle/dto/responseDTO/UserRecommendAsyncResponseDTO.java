package avengers.waffle.dto.responseDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRecommendAsyncResponseDTO {
    private String requestId;
}
