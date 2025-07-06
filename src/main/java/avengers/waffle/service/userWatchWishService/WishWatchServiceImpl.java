package avengers.waffle.service.userWatchWishService;

import avengers.waffle.dto.responseDTO.ContentResponseDTO;
import avengers.waffle.dto.responseDTO.PageResponseDto;
import avengers.waffle.dto.searchDTO.WishWatchSearchDTO;
import avengers.waffle.mapper.MovieMapper;
import avengers.waffle.mapper.WishWatchMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishWatchServiceImpl implements WishWatchService {

     private final WishWatchMapper wishWatchMapper;

    @Override
    public PageResponseDto<ContentResponseDTO> getContents(WishWatchSearchDTO searchDTO) {
        WishWatchSearchDTO queryDto = WishWatchSearchDTO.builder()
                .memberId(searchDTO.getMemberId())
                .status(searchDTO.getStatus())
                .type(searchDTO.getType())
                .cursor(searchDTO.getCursor())
                .limit(searchDTO.getSize() + 1) // N+1 조회를 위해 limit 값을 설정합니다.
                .build();


        List<ContentResponseDTO> items;

        if ("movie".equals(searchDTO.getType())) {
            items = wishWatchMapper.findMovieContents(queryDto);
        } else if ("tv".equals(searchDTO.getType())) {
            items = wishWatchMapper.findTvContents(queryDto);
        } else {
            items = List.of();
        }

        Integer nextCursor = null;
        if (items.size() > searchDTO.getSize()) {
            ContentResponseDTO lastItem = items.remove(searchDTO.getSize().intValue());
            nextCursor = lastItem.getNo();
        }

        return new PageResponseDto<>(items, nextCursor);
    }
}
