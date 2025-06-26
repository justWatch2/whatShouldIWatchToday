package avengers.waffle.VO.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
public class SearchResultVO implements Serializable {
    private String id;
    private String poster_path;
    private String overview;
    private String korean_title;
    private String title;
    private Float numVotes;
    private Float averagerating;

    @Override
    public String toString() {
        return "SearchResultVO{" +
                "id='" + id + '\'' +
                ", poster_path='" + poster_path + '\'' +
                ", overview='" + overview + '\'' +
                ", korean_title='" + korean_title + '\'' +
                ", title='" + title + '\'' +
                ", vote='" + numVotes + '\'' +
                '}';
    }
}
