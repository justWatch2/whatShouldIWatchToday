package avengers.waffle.repository.mypageRepository;

import avengers.waffle.dto.userDTO.MyProfileResponseDTO;

public interface If_MypageService {
    MyProfileResponseDTO getMyProfile(String userId);
}
