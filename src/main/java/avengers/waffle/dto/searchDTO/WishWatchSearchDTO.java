package avengers.waffle.dto.searchDTO;


import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class WishWatchSearchDTO {
    private String memberId;
    private String status;
    private String type;
    private Integer cursor;
    private Integer size;   // 사용자가 요청한 원본 size
    private Integer limit;  // DB 조회에 사용할 size + 1 값

}
