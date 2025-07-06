package avengers.waffle.mapper;

import avengers.waffle.dto.responseDTO.ContentResponseDTO;
import avengers.waffle.dto.searchDTO.WishWatchSearchDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WishWatchMapper {

    List<ContentResponseDTO> findMovieContents(WishWatchSearchDTO wishWatchSearchDTO);

    List<ContentResponseDTO> findTvContents(WishWatchSearchDTO wishWatchSearchDTO);
}
