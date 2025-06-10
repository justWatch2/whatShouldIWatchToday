package avengers.waffle.VO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class PostVO {
    private long no;
    private String title;
    private String name;
    private LocalDateTime indate;
    private int count;
}
