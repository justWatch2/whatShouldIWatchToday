package avengers.waffle.VO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class PostVO {
    private long no;
    private String title;
    private String name;
    private LocalDateTime indate;
    private int count;
    private List<String> fileUrl;
    private int likeCount;
    private String contents;

}
