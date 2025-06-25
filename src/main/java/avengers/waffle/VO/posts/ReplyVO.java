package avengers.waffle.VO.posts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReplyVO{
    private Integer num;
    private String postNo;
    private String name;
    private String contents;
    private String time;
    private Integer likeCount;
    private Boolean liked;
}
