package avengers.waffle.service.userWatchWishService;

import avengers.waffle.dto.responseDTO.ContentResponseDTO;
import avengers.waffle.dto.responseDTO.PageResponseDto;
import avengers.waffle.dto.searchDTO.WishWatchSearchDTO;

import java.util.List;

public interface WishWatchService {

   PageResponseDto<ContentResponseDTO> getContents(WishWatchSearchDTO searchDTO);
}
