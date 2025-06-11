package avengers.waffle.VO.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
public class MovieSearchVO {
    private String title;
    private String genres;
    private String[] years;
    private boolean adult;
    private boolean korea;
    private boolean korean;



}
