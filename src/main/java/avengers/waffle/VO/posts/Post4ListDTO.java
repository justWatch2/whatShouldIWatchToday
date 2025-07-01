package avengers.waffle.VO.posts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class Post4ListDTO {
    long no = 0;
    String title = "";
    String id = "";
    String name = "";
    LocalDateTime indate = null;
    int count = 0;
}
