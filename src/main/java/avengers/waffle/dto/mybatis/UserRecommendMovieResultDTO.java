package avengers.waffle.dto.mybatis;


import lombok.Data;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@ToString
//@Builder
public class UserRecommendMovieResultDTO {

    private int movieId;
    private String koreanTitle;
    private String posterPath;
    private String backdropPath;
    private String overview;
    private double averageRating;
    private Integer keywordsRanking;

    private String keywordsRaw;  // SQL의 GROUP_CONCAT 결과 (콤마로 구분된 문자열)
    private List<String> keywords;  // keywordsRaw를 파싱하여 List<String> 형태로 저장 (BFS에서 사용)


    // MyBatis가 keywordsRaw 필드에 값을 주입할 때 자동으로 호출되어 keywords 필드를 채워줌
    public void setKeywordsRaw(String keywordsRaw) {
        this.keywordsRaw = keywordsRaw;
        System.out.println("잘들어왔다!@!@!@!@!@!@!!@@!!@!@@!");
        if (keywordsRaw == null || keywordsRaw.isEmpty()) {
            this.keywords = List.of();
        } else {
            this.keywords = Arrays.stream(keywordsRaw.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
        }
    }
}
