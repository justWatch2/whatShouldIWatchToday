package avengers.waffle.VO.posts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
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
    private List<String> fileUrl;

}
