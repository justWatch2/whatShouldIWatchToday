package avengers.waffle.VO.posts;

import avengers.waffle.entity.Post;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class PostVO {
    private long no;
    private String title;
    private String id;
    private String name;
    private String writer;
    private String category;
    private LocalDateTime indate;
    private int count;
    private int likeCount;
    private String contents;
    private List<String> fileUrllist;

}
