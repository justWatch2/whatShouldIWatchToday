package avengers.waffle.dto.responseDTO;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserRecommendResponseDTO {
    private List<?> userSelectedList;

}
