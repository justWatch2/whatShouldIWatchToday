package avengers.waffle.VO.posts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReplyVO{
    private int no;
    private String memberId;
    private String content;
    private String time;
    private int likeCount;
}
