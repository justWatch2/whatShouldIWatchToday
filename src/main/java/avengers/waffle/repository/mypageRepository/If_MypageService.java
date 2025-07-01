package avengers.waffle.repository.mypageRepository;

import avengers.waffle.dto.userDTO.MyProfileResponseDTO;
import avengers.waffle.dto.userDTO.ProfileUpdateRequestDTO;
import org.springframework.web.multipart.MultipartFile;

public interface If_MypageService {
    MyProfileResponseDTO getMyProfile(String userId);

    void updateMyProfile(String userId,ProfileUpdateRequestDTO requestDTO, MultipartFile imageFile);
}
