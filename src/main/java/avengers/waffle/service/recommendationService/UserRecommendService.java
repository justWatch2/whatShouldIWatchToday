package avengers.waffle.service.recommendationService;



import avengers.waffle.dto.requestDTO.UserRecommendRequestDTO;
import avengers.waffle.dto.responseDTO.UserRecommendResponseDTO;

public interface UserRecommendService {

UserRecommendResponseDTO memberRecommend(UserRecommendRequestDTO memberRecommendRequestDTO);

}
