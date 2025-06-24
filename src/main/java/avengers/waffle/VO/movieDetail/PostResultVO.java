package avengers.waffle.VO.movieDetail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
public class PostResultVO {
    private Integer no;
    private String title;
    private String contents;
}
