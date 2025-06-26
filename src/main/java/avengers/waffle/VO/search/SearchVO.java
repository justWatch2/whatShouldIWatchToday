package avengers.waffle.VO.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.Arrays;

@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
public class SearchVO implements Serializable {
    private String title;
    private String[] genres;
    private String[] years;
    private boolean adult;
    private boolean korea;
    private boolean korean;
    private Integer page;


    @Override
    public String toString() {
        return "SearchVO{" +
                "title='" + title + '\'' +
                ", genres='" + Arrays.toString(genres) + '\'' +
                ", years='" + Arrays.toString(years) + '\'' +
                ", adult='" + adult + '\'' +
                ", korea='" + korea + '\'' +
                ", korean='" + korean + '\'' +
                ", page='" + page + '\'' +
                '}';
    }
}
