package avengers.waffle.VO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class PostVO {
    private Long no;
    private String category;
    private String name;
    private String memberId;
    private String title;
    private LocalDateTime indate;
    private String contents;
    private Integer count;
    private Integer likeCount;

}
