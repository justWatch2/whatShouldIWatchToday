package avengers.waffle.dto.requestDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContentRequestDTO {
    private String status; // "wish", "watched"
    private String type;   // "movie", "tv"
    private  Integer cursor; // 마지막으로 조회된 콘텐츠의 ID (첫 페이지는 null)

    @Builder.Default
    private int size = 20; // 한 페이지에 보여줄 개수
}
